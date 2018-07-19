/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evolution.Coevolution;

import java.util.*;
import lt.lb.commons.Misc.Pair;

public class CompleteRelative implements PairingProducer {

    @Override
    public Collection<Pair<PairingInfo>> producePairs(Integer... sizes) {

        ArrayList<Pair<PairingInfo>> pairs = new ArrayList<>();

        if (sizes.length == 1) {
            int size = sizes[0];
            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    pairs.add(new Pair(new PairingInfo(0, i), new PairingInfo(0, j)));
                }
            }
            return pairs;
        }

        for (int i = 0; i < sizes.length; i++) {
            for (int j = i + 1; j < sizes.length; j++) {
                pairs.addAll(pairUp(i, j, sizes[i], sizes[j], 0, 0));
            }
        }

        return pairs;
    }

    public List<Pair<PairingInfo>> pairUp(int sub1, int sub2, int size1, int size2, int offset1, int offset2) {
        LinkedList<Pair<PairingInfo>> list = new LinkedList<>();
        for (int i = offset1; i < size1; i++) {
            for (int j = offset2; j < size2; j++) {
                Pair<PairingInfo> p = new Pair<>(new PairingInfo(sub1, i), new PairingInfo(sub2, j));
                list.add(p);
            }
        }
        return list;
    }

}
