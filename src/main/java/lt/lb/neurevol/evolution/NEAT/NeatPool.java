/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import lt.lb.commons.ArrayBasedCounter;
import lt.lb.commons.Log;
import lt.lb.commons.containers.Value;
import lt.lb.commons.misc.Interval;
import lt.lb.commons.threads.Promise;
import lt.lb.neurevol.evolution.NEAT.interfaces.Pool;
import lt.lb.neurevol.evolution.Control.NEATConfig;

/**
 *
 * @author laim0nas100
 */
public class NeatPool<T extends Agent> implements Serializable, Pool<T> {

    public transient NEATConfig<T> conf;

    public String poolID = System.currentTimeMillis() + "";

    public double SELECTION = 0.3;

    public double similarity = 1;

    public boolean strictSimilarity;

    public Interval similarities;
    public double similarityChangeRate = 0.12;
    public int distinctSpecies = 5;

    public int maxStaleness = 20;
    public int populationSize = 200;
    public int generation = 0;

    public T allTimeBest;

    public LinkedList<Agent> bestAgents;
    public ArrayBasedCounter innovation;
    public ArrayList<Species> species;

    private void addBest(T g) {

        Comparator<T> cmp = conf.getSorter().getComparator();
        if (this.allTimeBest == null || cmp.compare(g, this.allTimeBest) < 0) {
            if (this.allTimeBest != null) {
                Log.print("Replace best was:" + this.allTimeBest.fitness + " now:" + g.fitness);
            }

            this.allTimeBest = g;

        }
        bestAgents.addLast(g);
    }

    public Agent getCurrentBest() {
        return bestAgents.getLast();
    }

    public void assignToSpecies(T g) {
        int toAssign = -1;
        Double bestSimilarity = Double.MAX_VALUE;
        double[] sim = new double[species.size()];

        int index = 0;
        if (sim.length > 0) {
            LinkedList<Promise> promises = new LinkedList<>();
            for (Species<T> s : species) {
                final int i = index++;
                new Promise(() -> {

                    sim[i] = conf.getSimilarityEvaluator().similarity(s.getLeader(), g);
                }).collect(promises).execute(conf.getExecutor());

            }
            resolve(new Promise().waitFor(promises).execute(conf.getExecutor()));

            for (int i = 0; i < sim.length; i++) {
                double d = sim[i];
                this.similarities = this.similarities.expand(d);
                if (d < this.similarity && bestSimilarity > d) {
                    bestSimilarity = d;
                    toAssign = i;
                }
            }
        }

        if (toAssign == -1) {
            Species<T> spe = conf.newSpecies();
            spe.agents.add(g);
            species.add(spe);
        } else {
            species.get(toAssign).agents.add(g);
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

        for (Agent g : this.getPopulation()) {
            if (g.fitness == null) {
                throw new AssertionError("Agent :" + g.toString() + " fitness is null");
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

            if (s.agents.isEmpty()) {
                continue;
            }

            new Promise(() -> {
                List<Agent> cullSpecies = s.cullSpecies(SELECTION, false);
                for (Agent g : cullSpecies) {
                    Log.print("Dead:" + g.fitness);
                    if (g.globalRank == populationSize) {
                        new Exception("Kill best genome").printStackTrace();
                    }
                }
            }).collect(promises).execute(conf.getExecutor());

        }

        resolve(new Promise().waitFor(promises).execute(conf.getExecutor()));

        for (Species s : species) {
            if (!s.agents.isEmpty()) {

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
        T best = null;
        for (Species<T> s : species) {
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

        conf.getSorter().rankGlobaly(this.getPopulation());
        promises.clear();
        for (Species s : species) {

            new Promise(() -> {
                return s.calculateAverageRank();
            }).collect(promises).execute(conf.getExecutor());

        }
        resolve(new Promise().waitFor(promises).execute(conf.getExecutor()));
        ArrayList<T> leaders = new ArrayList<>();

        Log.print("Remove weak");
        //remove weak
        double sum = this.totalSpeciesAvgRank();

        for (Species<T> s : species) {

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

        Collections.sort(leaders, conf.getSorter().getComparator());
//        Collections.sort(leaders, Genome.fitnessDescending);

        ConcurrentLinkedDeque<T> newGeneration = new ConcurrentLinkedDeque<>();

        promises.clear();
        Log.print("New generation");
        for (Species s : species) {
            double breed = Math.floor(s.avgRank / sum * this.populationSize) - 1;
            Log.print(s.id, "Child breed:", breed);
            LinkedList<Promise> breedPromises = new LinkedList<>();
            for (int i = 0; i < breed;) {
//                this.breedChild(s.genomes);
                List<T> spawnlings = conf.getBreeder().breedChild(s.agents);
                for (T child : spawnlings) {
                    if (i >= breed) {
                        break;
                    }
                    if (child != null) {
                        conf.getMutator().mutate(child);
                        newGeneration.add(child);
                        i++;

                    }
                }
            }
            Promise culler = new Promise(() -> {
                List<Agent> cullSpecies = s.cullSpecies(0, true);
                for (Agent g : cullSpecies) {
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
        for (int i = 0; i < left;) {
            List<T> spawnlings = conf.getBreeder().breedChild(leaders);
            for (T child : spawnlings) {
                if (i >= left) {
                    break;
                }
                conf.getMutator().mutate(child);
                newGeneration.add(child);
                i++;
            }

        }
        resolve(new Promise().waitFor(promises).execute(conf.getExecutor()));
        Log.print("Assign new generation");
        for (T g : newGeneration) {
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
            for (T g : leaders) {
                assignToSpecies(g);
            }
            for (T g : newGeneration) {
                assignToSpecies(g);
            }
        }

        int genomeID = 1;
        for (Agent g : newGeneration) {
            g.id = this.poolID.substring(this.poolID.length() - 5) + " " + genomeID++;
        }
    }

    public void recreateSpecies(Collection<T> ancestors) {
        species.clear();
        for (T ancestor : ancestors) {
            Species s = new Species();
            s.agents.add(ancestor);
            species.add(s);
        }
    }

    public void newGeneration(T ancestor) {
        ArrayList<T> list = new ArrayList<>(1);
        list.add(ancestor);
        newGeneration(list);
    }

    public void newGeneration(Collection<T> ancestors) {
        this.bestAgents.clear();
        recreateSpecies(ancestors);
        equalCloning(ancestors);

    }

    public void equalCloning(Collection<T> ancestors) {
        int size = ancestors.size();
        int sizePerSpecies = Math.max(this.populationSize / size, 1);
        int left = this.populationSize - size;

        while (left > 0) {
            for (T ancestor : ancestors) {
                int create = Math.min(sizePerSpecies, left);
                for (int i = 0; i < create; i++) {
                    T g = (T) ancestor.clone();
                    conf.getMutator().mutate(g);
                    this.assignToSpecies(g);
                }
                left -= create;
                if (left == 0) {
                    break;
                }
            }
        }
    }

    @Override
    public List<T> getPopulation() {
        ArrayList<T> pop = new ArrayList<>(this.populationSize);
        for (Species s : species) {
            pop.addAll(s.agents);
        }
        return pop;
    }

    public NeatPool(NEATConfig conf) {
        this();
        this.conf = conf;
        Collection<T> gen = conf.getMaker().initializeGeneration();
        this.populationSize = gen.size();
        int i = 0;
        for (T g : gen) {
            Log.print("Mutating genome " + i);
            i++;
            conf.getMutator().mutate(g);
            assignToSpecies(g);
        }

    }

    public NeatPool() {
        species = new ArrayList<>();
        bestAgents = new LinkedList<>();
        similarities = Interval.newExtendable();
        this.innovation = new ArrayBasedCounter(1);
    }

    @Override
    public List<List<T>> getSubpopulations() {
        List<List<T>> list = new ArrayList<>();
        for (Species spec : this.species) {
            ArrayList<T> genomes = new ArrayList<>();
            genomes.addAll(spec.agents);
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
