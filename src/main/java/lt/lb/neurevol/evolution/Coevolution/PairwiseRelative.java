/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.Coevolution;

import java.util.*;
import lt.lb.commons.containers.tuples.Pair;

public class PairwiseRelative implements PairingProducer {

    @Override
    public Collection<Pair<PairingInfo>> producePairs(Integer... sizes) {

        if (sizes.length != 2 && sizes.length != 1) {
            throw new IllegalStateException("Populations is not 2 or 1");
        }

        if (sizes[0] % 2 != 0) {
            throw new IllegalStateException("Population is not multiple of 2");
        }
        if (sizes.length == 2) {
            if (!Objects.equals(sizes[0], sizes[1])) {
                throw new IllegalStateException("Populations sizes are not equal");
            }
        }
        ArrayList<Pair<PairingInfo>> pairs = new ArrayList<>();

        if (sizes.length == 1) {
            for (int i = 0; i < sizes[0]; i += 2) {
                pairs.add(new Pair<>(new PairingInfo(0, i), new PairingInfo(0, i + 1)));
            }
        } else {
            for (int i = 0; i < sizes[0]; i += 1) {
                pairs.add(new Pair<>(new PairingInfo(0, i), new PairingInfo(1, i)));
            }
        }

        return pairs;
    }

}
