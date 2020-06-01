/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.imp;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import lt.lb.commons.containers.tuples.Pair;
import lt.lb.commons.misc.rng.RandomDistribution;
import lt.lb.neurevol.evolution.NEAT.*;
import lt.lb.neurevol.neural.NeuronInfo;
import lt.lb.neurevol.evolution.NEAT.interfaces.AgentBreeder;

public abstract class DefaultGenomeBreeder implements AgentBreeder<Genome> {

    public RandomDistribution rnd;
    public DefaultGenomeBreeder(RandomDistribution dist){
        rnd = dist;
    }  
    public double CROSSOVER = 0.7;

    private Genome crossover(List<Genome> list) {
        Genome child;
        LinkedList<Genome> parents = rnd.pickRandomPreferLow(list, 2, list.size(), 1);
        child = (Genome) parents.peekFirst().clone();
        child.bias.clear();
        child.genes.clear();
        CrossoverList cross = new CrossoverList(parents.peekFirst(), parents.peekLast());
        for (Pair<? extends NeuronInfo> pair : cross.biasList) {
            child.bias.add((NeuronInfo) pair.getRandomPreferNotNull(rnd.getBooleanSupplier()).clone());
        }
        for (Pair<? extends Gene> pair : cross.geneList) {
            child.genes.add(pair.getRandomPreferNotNull(rnd.getBooleanSupplier()));
        }
        return child;
    }

    @Override
    public List<Genome> breedChild(List<Genome> list) {
        int size = list.size();
        Genome child;
        if (size > 1 && rnd.nextDouble() < CROSSOVER) {
            child = crossover(list);
        } else {
            child = (Genome) list.get(rnd.nextInt(size)).clone();
        }
        return Arrays.asList(child);
    }

    @Override
    public abstract Collection<Genome> initializeGeneration();

}
