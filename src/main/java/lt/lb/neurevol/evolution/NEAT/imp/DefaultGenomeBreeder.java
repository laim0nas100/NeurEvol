/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.imp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lt.lb.commons.containers.Pair;
import lt.lb.commons.misc.F;
import lt.lb.neurevol.evolution.NEAT.*;
import lt.lb.neurevol.neural.NeuronInfo;
import lt.lb.neurevol.evolution.NEAT.interfaces.AgentBreeder;

public class DefaultGenomeBreeder implements AgentBreeder<Genome> {

    public double CROSSOVER = 0.7;

    private Genome crossover(List<Genome> list) {
        Genome child;
        LinkedList<Genome> parents = F.RND.pickRandomPreferLow(F.RND.RND,list, 2, list.size(), 1);
        child = (Genome) parents.peekFirst().clone();
        child.bias.clear();
        child.genes.clear();
        CrossoverList cross = new CrossoverList(parents.peekFirst(), parents.peekLast());
        for (Pair<? extends NeuronInfo> pair : cross.biasList) {
            child.bias.add((NeuronInfo) pair.getRandomPreferNotNull(F.RND.RND).clone());
        }
        for (Pair<? extends Gene> pair : cross.geneList) {
            child.genes.add(pair.getRandomPreferNotNull(F.RND.RND));
        }
        return child;
    }

    @Override
    public Genome breedChild(List<Genome> list) {
        int size = list.size();
        Genome child;
        if (size > 1 && F.RND.RND.nextDouble() < CROSSOVER) {
            ArrayList<Genome> castList = new ArrayList<>(list.size());
            F.addCast(list, castList);
            child = crossover(castList);
        } else {
            child = (Genome) list.get(F.RND.RND.nextInt(size)).clone();
        }
        return child;
    }

}
