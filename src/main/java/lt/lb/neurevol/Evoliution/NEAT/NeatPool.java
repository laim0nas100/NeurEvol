/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT;

import Misc.Interval;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import lt.lb.commons.ArrayBasedCounter;
import lt.lb.commons.Containers.Value;
import lt.lb.commons.Log;
import lt.lb.commons.Threads.Promise;
import lt.lb.neurevol.Evoliution.Control.Config;
import lt.lb.neurevol.Evoliution.NEAT.interfaces.Pool;

/**
 *
 * @author Lemmin
 */
public class NeatPool implements Serializable, Pool {

    public transient Config conf;

    public String poolID = System.currentTimeMillis() + "";

    public double SELECTION = 0.3;

    public double similarity = 1;

    public boolean strictSimilarity;

//    public transient double[] similarities;
    public transient Interval similarities;
    public double similarityChangeRate = 0.12;
    public int distinctSpecies = 5;

    public int maxStaleness = 20;
    public int populationSize = 200;
    public int generation = 0;

    public Genome allTimeBest;

    public LinkedList<Genome> bestGenomes;
    public ArrayBasedCounter innovation;
    public ArrayList<Species> species;

    private void addBest(Genome g) {

        Comparator<Genome> cmp = conf.getGenomeSorter().getComparator();
        if (this.allTimeBest == null || cmp.compare(g, this.allTimeBest) < 0) {
            if (this.allTimeBest != null) {
                Log.print("Replace best was:" + this.allTimeBest.fitness + " now:" + g.fitness);
            }

            this.allTimeBest = g;

        }
        bestGenomes.addLast(g);
    }

    public Genome getCurrentBest() {
        return bestGenomes.getLast();
    }

    public void assignToSpecies(Genome g) {
        int toAssign = -1;
        Double bestSimilarity = Double.MAX_VALUE;
        double[] sim = new double[species.size()];

        int index = 0;
        if (sim.length > 0) {
            LinkedList<Promise> promises = new LinkedList<>();
            for (Species s : species) {
                final int i = index++;
                new Promise(() -> {

                    sim[i] = conf.getGenomeSimilarityEvaluator().similarity(s.getLeader(), g);
                }).collect(promises).execute(conf.getExecutor());

            }
            resolve(new Promise().waitFor(promises).execute(conf.getExecutor()));

            for (int i = 0; i < sim.length; i++) {
                double d = sim[i];
                this.similarities.expand(d);
                if (d < this.similarity && bestSimilarity > d) {
                    bestSimilarity = d;
                    toAssign = i;
                }
            }
        }

        if (toAssign == -1) {
            Species spe = conf.newSpecies();
            spe.genomes.add(g);
            species.add(spe);
        } else {
            species.get(toAssign).genomes.add(g);
        }
//        Log.print("similarity:",bestSimilarity);

    }

    protected double totalSpeciesAvgRank() {
        double total = 0;
        for (final Species s : species) {
            total += s.avgRank;
        }
        return total;
    }

    protected double calculateSimilarityThreshold() {
        if (species.size() > this.distinctSpecies) {
            this.similarity *= (1 + this.similarityChangeRate);
        } else if (species.size() < this.distinctSpecies) {
            this.similarity *= (1 - this.similarityChangeRate);
        }
        similarity = similarities.clamp(similarity);
        return similarity;
    }

    private <Type> Type resolve(Promise<Type> promise) {
        try {
            return promise.get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void newGeneration() {
        Value<Species> bestSpecies = new Value<>();

        for (Genome g : this.getPopulation()) {
            if (g.fitness == null) {
                throw new AssertionError("Genome :" + g.toString() + " fitness is null");
            }
        }
        bestSpecies.set(species.get(0));

        this.generation++;
        this.similarities = Interval.newExtendable();
        ConcurrentLinkedDeque<Species> survived = new ConcurrentLinkedDeque<>();

        LinkedList<Promise> promises = new LinkedList<>();
        //cull species
        int index = 0;
        Log.print("Cull species");
        for (Species s : species) {
            s.id = index++;

            if (s.genomes.isEmpty()) {
                continue;
            }

            new Promise(() -> {
                List<Genome> cullSpecies = s.cullSpecies(SELECTION, false);
                for (Genome g : cullSpecies) {
                    Log.print("Dead:" + g.fitness);
                    if (g.globalRank == populationSize) {
                        new Exception("Kill best genome").printStackTrace();
                    }
                }
            }).collect(promises).execute(conf.getExecutor());

        }

        resolve(new Promise().waitFor(promises).execute(conf.getExecutor()));

        for (Species s : species) {
            if (!s.genomes.isEmpty()) {

                if (s.bestFitness == null || s.getLeader().fitness.compareTo(s.bestFitness) > 0) {
                    s.staleness = 0;
                    s.bestFitness = s.getLeader().fitness;
                } else {
                    s.staleness++;
                }
                if (bestSpecies.get().bestFitness == null || bestSpecies.get().bestFitness.compareTo(s.bestFitness) < 0) {
                    bestSpecies.set(s);
                }
                survived.add(s);
            }
        }

        species.clear();
        species.addAll(survived);
        survived.clear();
        Log.print("Survived species", species.size());

        Log.print("Remove stale");
        Genome best = null;
        for (Species s : species) {
            if (s.staleness < this.maxStaleness || s.bestFitness.compareTo(this.getCurrentBest().fitness) >= 0 || s.id == bestSpecies.get().id) {
                survived.add(s);

                if (best == null || s.getLeader().fitness.compareTo(best.fitness) < 0) {
                    best = s.getLeader();
                }
            }
        }
        this.addBest(best);
        species.clear();
        species.addAll(survived);
        survived.clear();

        Log.print("Survived species", species.size());

        conf.getGenomeSorter().rankGlobaly(this.getPopulation());
        promises.clear();
        for (Species s : species) {

            new Promise(() -> {
                return s.calculateAverageRank();
            }).collect(promises).execute(conf.getExecutor());

        }
        resolve(new Promise().waitFor(promises).execute(conf.getExecutor()));
        ArrayList<Genome> leaders = new ArrayList<>();

        Log.print("Remove weak");
        //remove weak
        double sum = this.totalSpeciesAvgRank();

        for (Species s : species) {

            double breed = Math.floor(s.avgRank / sum * this.populationSize);
            if (breed >= 1.0 || s.id == bestSpecies.get().id) {
                survived.add(s);
                leaders.add(s.getLeader());
            }
        }

        species.clear();
        species.addAll(survived);
        survived.clear();

        Log.print("Survived species", species.size());

        Collections.sort(leaders, conf.getGenomeSorter().getComparator());
//        Collections.sort(leaders, Genome.fitnessDescending);

        ConcurrentLinkedDeque<Genome> newGeneration = new ConcurrentLinkedDeque<>();

        promises.clear();
        Log.print("New generation");
        for (Species s : species) {
            double breed = Math.floor(s.avgRank / sum * this.populationSize) - 1;
            Log.print(s.id, "Child breed:", breed);
            LinkedList<Promise> breedPromises = new LinkedList<>();
            for (int i = 0; i < breed; i++) {
//                this.breedChild(s.genomes);
                new Promise(() -> {
                    Genome breedChild = conf.getGenomeBreeder().breedChild(s.genomes);
                    conf.getGenomeMutator().mutate(breedChild);

                    newGeneration.add(breedChild);
                }).collect(breedPromises).execute(conf.getSequentialExecutor());

            }
            Promise culler = new Promise(() -> {
                List<Genome> cullSpecies = s.cullSpecies(0, true);
                for (Genome g : cullSpecies) {
                    if (g.globalRank == populationSize) {
                        new Exception("Kill best genome").printStackTrace();

                    }
                    Log.print("Dead:" + g.fitness);
                }

            }).waitFor(breedPromises).execute(conf.getExecutor());
            promises.add(culler);

        }
        resolve(new Promise().waitFor(promises).execute(conf.getExecutor()));

        int left = this.populationSize - leaders.size() - newGeneration.size();
        Log.print("Leaders", leaders.size(), "Left to breed", left);

        promises.clear();
        //crossover leaders
        for (int i = 0; i < left; i++) {
            new Promise(() -> {
                Genome breedChild = conf.getGenomeBreeder().breedChild(leaders);
                conf.getGenomeMutator().mutate(breedChild);
                newGeneration.add(breedChild);
            }).collect(promises).execute(conf.getSequentialExecutor());
        }
        resolve(new Promise().waitFor(promises).execute(conf.getExecutor()));
        Log.print("Assign new generation");
        for (Genome g : newGeneration) {
            this.assignToSpecies(g);
        }

        int specCount = species.size();
        Log.print("Species:", specCount, "Sim:", this.similarity);

        this.calculateSimilarityThreshold();

        double ratio = Math.max(specCount, distinctSpecies);
        ratio /= Math.min(specCount, distinctSpecies);
        if (ratio > 1.5) {//reassign
            Log.print("Species reassign");
//            recreateSpecies(leaders);
            species.clear();
            for (Genome g : leaders) {
                assignToSpecies(g);
            }
            for (Genome g : newGeneration) {
                assignToSpecies(g);
            }
        }

        int genomeID = 1;
        for (Genome g : newGeneration) {
            g.id = this.poolID.substring(this.poolID.length() - 5) + " " + genomeID++;
        }
    }

    public void recreateSpecies(Collection<Genome> ancestors) {
        species.clear();
        for (Genome ancestor : ancestors) {
            Species s = new Species();
            s.genomes.add(ancestor);
            species.add(s);
        }
    }

    public void newGeneration(Genome ancestor) {
        ArrayList<Genome> list = new ArrayList<>(1);
        list.add(ancestor);
        newGeneration(list);
    }

    public void newGeneration(Collection<Genome> ancestors) {
        this.bestGenomes.clear();
        recreateSpecies(ancestors);
        equalCloning(ancestors);

    }

    public void equalCloning(Collection<Genome> ancestors) {
        int size = ancestors.size();
        int sizePerSpecies = Math.max(this.populationSize / size, 1);
        int left = this.populationSize - size;

        while (left > 0) {
            for (Genome ancestor : ancestors) {
                int create = Math.min(sizePerSpecies, left);
                for (int i = 0; i < create; i++) {
                    Genome g = (Genome) ancestor.clone();
                    conf.getGenomeMutator().mutate(g);
                    this.assignToSpecies(g);
                    Log.print("In cloning Bias:" + ancestor.bias.size());
                    Log.print("Cloning class" + ancestor.getClass());
                }
                left -= create;
                if (left == 0) {
                    break;
                }
            }
        }
    }

    @Override
    public List<Genome> getPopulation() {
        ArrayList<Genome> pop = new ArrayList<>(this.populationSize);
        for (Species s : species) {
            pop.addAll(s.genomes);
        }
        return pop;
    }

    public NeatPool(Config conf) {
        this();
        this.conf = conf;
        Collection<Genome> gen = conf.getGenomeMaker().initializeGeneration();
        this.populationSize = gen.size();
        int i = 0;
        for (Genome g : gen) {
            Log.print("Mutating genome " + i);
            i++;
            conf.getGenomeMutator().mutate(g);
            assignToSpecies(g);
        }

    }

    public NeatPool() {
        species = new ArrayList<>();
        bestGenomes = new LinkedList<>();
        similarities = Interval.newExtendable();
        this.innovation = new ArrayBasedCounter(1);
    }

    @Override
    public List<List<Genome>> getSubpopulations() {
        List<List<Genome>> list = new ArrayList<>();
        for (Species spec : this.species) {
            ArrayList<Genome> genomes = new ArrayList<>();
            genomes.addAll(spec.genomes);
            list.add(genomes);
        }
        return list;
    }

    @Override
    public int getGeneration() {
        return this.generation;
    }

    @Override
    public void setGeneration(int generation) {
        this.generation = generation;
    }

}
