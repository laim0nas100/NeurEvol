/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.Coevolution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lt.lb.neurevol.Evoliution.NEAT.Genome;
import lt.lb.neurevol.Evoliution.NEAT.interfaces.Pool;
import lt.lb.neurevol.Misc.Pair;

/**
 *
 * @author Laimonas-Beniusis-PC
 */
public class CompleteRelativeSubpopulation implements PairingProducer {

    @Override
    public Collection<Pair<Genome>> producePairs(Pool pool) {
        if (pool.getPopulation().isEmpty()) {
            throw new IllegalStateException("Population size is empty");
        }
        ArrayList<Pair<Genome>> list = new ArrayList<>();
        List<List<Genome>> subpopulations = pool.getSubpopulations();
        for (int i = 0; i < subpopulations.size(); i++) {
            for (int j = i + 1; j < subpopulations.size(); j++) {
                list.addAll(this.pairPopulation(subpopulations.get(i), subpopulations.get(j)));
            }
        }

        return list;

    }

    private Collection<Pair<Genome>> pairPopulation(List<Genome> list1, List<Genome> list2) {
        ArrayList<Pair<Genome>> list = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                list.add(new Pair<>(list1.get(i), list2.get(j)));
            }
        }
        return list;
    }

}
