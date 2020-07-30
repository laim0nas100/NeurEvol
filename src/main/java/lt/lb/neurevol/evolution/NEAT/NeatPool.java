/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import lt.lb.commons.misc.ArrayBasedCounter;
import lt.lb.commons.F;
import lt.lb.commons.misc.UUIDgenerator;
import lt.lb.commons.containers.values.Value;
import lt.lb.commons.interfaces.StringBuilderActions.ILineAppender;
import lt.lb.commons.misc.ExtComparator;
import lt.lb.commons.misc.Interval;
import lt.lb.commons.threads.Promise;
import lt.lb.neurevol.evolution.NEAT.interfaces.Pool;
import lt.lb.neurevol.evolution.Control.NEATConfig;

/**
 *
 * @author laim0nas100
 */
public class NeatPool<T extends Agent> implements Serializable, Pool<T> {

    public ILineAppender debug = ILineAppender.empty;

    public transient NEATConfig<T> conf;

    public String poolID = System.currentTimeMillis() + "";

    public double SELECTION = 0.3;

    public double similarity = 1;

    public boolean strictSimilarity;

    public Interval similarities = Interval.ZERO_ONE;
    public double similarityChangeRate = 0.12;
    public int distinctSpecies = 5;

    public int maxSpecies = 20;

    public int maxStaleness = 20;
    public int populationSize = 200;
    public int generation = 0;

    public T allTimeBest;

    public LinkedList<T> bestAgents;
    public ArrayBasedCounter innovation;
    public ArrayList<Species<T>> species;

    private void addBest(T g) {

        ExtComparator<T> cmp = ExtComparator.of(conf.getSorter().getComparator());
        if (this.allTimeBest == null || cmp.greaterThan(g, this.allTimeBest)) {
            if (this.allTimeBest != null) {
                debug.appendLine(this.getGeneration() + " Replace best was:" + this.allTimeBest + " now:" + g);
            }

            this.allTimeBest = g;

        }
        bestAgents.addLast(g);
    }

    public T getCurrentBest() {
        return bestAgents.getLast();
    }

    private void writeID(T g, Species<T> s) {
        if (g.id != null) {
            return;
        }
        String pid = this.poolID.substring(this.poolID.length() - 5);
        g.id = "Pool" + pid + " Gen>" + this.getGeneration() + " Spec>" + s.id + " ID>" + UUIDgenerator.nextUUID("Agent");
    }

    public void assignToSpecies(T g) {

        if (distinctSpecies == 1 && species.size() == 1) {
            species.get(0).agents.add(g);
            writeID(g, species.get(0));
            return;
        }

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
                Interval expand = this.similarities.expand(d);
                this.similarities = expand;

                if (bestSimilarity > d) {
                    bestSimilarity = d;
                    toAssign = i;
                }
            }
        }

        if (toAssign == -1 || (bestSimilarity > this.similarity && sim.length < this.maxSpecies)) {
            Species<T> spe = conf.newSpecies();
            spe.agents.add(g);
            species.add(spe);
            writeID(g, spe);
        } else {
            species.get(toAssign).agents.add(g);
            writeID(g, species.get(toAssign));
        }
//        debug.appendLine("similarity:",bestSimilarity);

    }

    protected double totalSpeciesAvgRank() {
        double total = 0;
        for (final Species<T> s : species) {
            total += s.avgInfluence.get();
        }
        return total;
    }

    protected double calculateSimilarityThreshold() {
        if (species.size() > this.distinctSpecies) {
            this.similarity *= (1 + this.similarityChangeRate);
        } else if (species.size() < this.distinctSpecies) {
            this.similarity *= (1 - this.similarityChangeRate);
        }
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
        ExtComparator<T> cmp = ExtComparator.of(conf.getSorter().getComparator());
        Value<Species<T>> bestSpecies = new Value<>();

//        for (Agent g : this.getPopulation()) {
//            if (g.fitness == null) {
//                throw new AssertionError("Agent :" + g.toString() + " fitness is null");
//            }
//        }
        bestSpecies.set(species.get(0));

        this.generation++;
        conf.getSorter().startGeneration(generation);
        conf.getSorter().rankGlobaly(generation, this.getPopulation());
        ConcurrentLinkedDeque<Species<T>> survived = new ConcurrentLinkedDeque<>();

        LinkedList<Promise> promises = new LinkedList<>();
        //cull species
        int index = 0;
        debug.appendLine("Cull species");
        for (Species<T> s : species) {
            s.id = index++;

            if (s.agents.isEmpty()) {
                continue;
            }

            new Promise(() -> {
                List<T> cullSpecies = s.cullSpecies(SELECTION, false);
                for (T g : cullSpecies) {
                    debug.appendLine("Dead:" + g);
                    if (conf.getSorter().globalRank(g) == populationSize) {
                        debug.appendLine("Kill best genome.");
                    }
                }
                for (T g : s.agents) {
                    debug.appendLine("Alive:" + g);

                }
            }).collect(promises).execute(conf.getExecutor());

        }

        resolve(new Promise()
                .waitFor(promises)
                .execute(conf.getExecutor())
        );

        for (Species<T> s : species) {
            if (!s.agents.isEmpty()) {
                if (s.best == null || cmp.lessThan(s.getLeader(), allTimeBest)) {
                    s.staleness = 0;
                    s.best = s.getLeader();
                } else {
                    s.staleness++;
                }
                if (bestSpecies.get().best == null || cmp.lessThan(bestSpecies.get().best, s.best)) {
                    bestSpecies.set(s);
                }
                survived.add(s);
            }
        }

        species.clear();
        species.addAll(survived);
        survived.clear();
        debug.appendLine("Survived species", species.size());

        debug.appendLine("Remove stale");
        T best = null;

        for (Species<T> s : species) {
            if (s.staleness < this.maxStaleness || cmp.greaterThan(getCurrentBest(), s.best) || s.id == bestSpecies.get().id) {
                survived.add(s);

                if (best == null || cmp.greaterThan(s.getLeader(), best)) {
                    best = s.getLeader();
                }
            }
        }
        this.addBest(best);
        species.clear();
        species.addAll(survived);
        survived.clear();

        debug.appendLine("Survived species", species.size());

        conf.getSorter().rankGlobaly(generation, this.getPopulation());
        promises.clear();
        ArrayList<T> leaders = new ArrayList<>();

        debug.appendLine("Remove weak");
        //remove weak
        double sum = this.totalSpeciesAvgRank();

        for (Species<T> s : species) {

            double breed = Math.floor(s.avgInfluence.get() / sum * this.populationSize);
            if (breed >= 1.0 || s.id == bestSpecies.get().id) {
                survived.add(s);
                leaders.add(s.getLeader());
            }
        }

        species.clear();
        species.addAll(survived);
        survived.clear();

        debug.appendLine("Survived species", species.size());

        Collections.sort(leaders, conf.getSorter().getComparator().reversed());
//        Collections.sort(leaders, Genome.fitnessDescending);

        ConcurrentLinkedDeque<T> newGeneration = new ConcurrentLinkedDeque<>();

        promises.clear();

        beforeNewGeneration();
        debug.appendLine("New generation");
        for (Species<T> s : species) {
            double breed = Math.floor(s.avgInfluence.get() / sum * this.populationSize) - 1;
//            debug.appendLine(s.id, "Child breed:", breed);
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
                List<T> cullSpecies = s.cullSpecies(0, true);
                for (T g : cullSpecies) {
                    if (conf.getSorter().globalRank(g) == populationSize) {
                        new Exception("Kill best genome").printStackTrace();

                    }
                    debug.appendLine("Dead:" + g);
                }

            }).waitFor(breedPromises).execute(conf.getExecutor());
            promises.add(culler);

        }
        resolve(new Promise().waitFor(promises).execute(conf.getExecutor()));

        int left = this.populationSize - leaders.size() - newGeneration.size();
        debug.appendLine("Leaders", leaders.size(), "Left to breed", left);

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
        if (!newGeneration.isEmpty()) {
            debug.appendLine("Assign new generation");
            for (T g : newGeneration) {
                this.assignToSpecies(g);
            }

            int specCount = species.size();
            debug.appendLine("Species:", specCount, "Sim:", new DecimalFormat("0.00").format(this.similarity), this.similarities);

            this.calculateSimilarityThreshold();

            double ratio = Math.max(specCount, distinctSpecies);
            ratio /= Math.min(specCount, distinctSpecies);
            if (ratio > 1.5) {//reassign
                debug.appendLine("Species reassign");
//            recreateSpecies(leaders);
                species.clear();
                for (T g : leaders) {
                    assignToSpecies(g);
                }
                for (T g : newGeneration) {
                    assignToSpecies(g);
                }

                debug.appendLine("After Reassign Species:", specCount, "Sim:", new DecimalFormat("0.00").format(this.similarity), this.similarities);
            }
        }

//        int genomeID = 1;
//        for (T g : newGeneration) {
//            g.id = this.poolID.substring(this.poolID.length() - 5) + " " + genomeID++;
//        }
//        genomeID = 1;
//        for (T g : newGeneration) {
//            String id = g.id;
//            g.id = "Gen>" + this.getGeneration() + " Spec>" + id + " ID>" + genomeID++;
//        }

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
        Collection<T> gen = conf.getBreeder().initializeGeneration();
        this.populationSize = gen.size();
        int i = 0;
        for (T g : gen) {
            debug.appendLine("Mutating genome " + i);
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
        for (Species<T> spec : this.species) {
            list.add(spec.agents);
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
