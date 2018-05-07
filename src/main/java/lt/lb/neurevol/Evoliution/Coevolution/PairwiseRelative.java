/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.Coevolution;

import java.util.ArrayList;
import java.util.Collection;
import lt.lb.neurevol.Evoliution.NEAT.Genome;
import lt.lb.neurevol.Evoliution.NEAT.interfaces.Pool;
import lt.lb.neurevol.Misc.Pair;

public class PairwiseRelative implements PairingProducer {

    @Override
    public Collection<Pair<Genome>> producePairs(Pool pool) {
        ArrayList<Genome> population = new ArrayList<>(pool.getPopulation());
        int size = population.size();
        if (population.size() % 2 != 0 || population.isEmpty()) {
            throw new IllegalStateException("Population size is not a multiple of 2, got:" + population.size());
        }

        ArrayList<Pair<Genome>> pairs = new ArrayList<>();
        for (int i = 0; i < size; i += 2) {
            pairs.add(new Pair<>(population.get(i), population.get(i + 1)));

        }

        return pairs;
    }

}
