/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT;

import LibraryLB.ArrayBasedCounter;
import LibraryLB.Log;
import java.util.ArrayDeque;
import java.util.ArrayList;
import lt.lb.neurevol.Misc.F;
import lt.lb.neurevol.Neural.NeuronInfo;

/**
 *
 * @author Lemmin
 */
public class CrossoverList {

    public int excessIndexStart = -1;
    public int matchingGene = 0;
    public double matchingWeightSum = 0.0;
    public int disjointBias = 0;
    public double biasSum = 0.0;
    public ArrayDeque<Pair<? extends Gene>> geneList = new ArrayDeque<>();
    public ArrayList<Pair<? extends NeuronInfo>> biasList = new ArrayList<>();

    public static class Pair<T> {

        public T g1 = null;
        public T g2 = null;

        public Pair(T g1, T g2) {
            this.g1 = g2;
            this.g2 = g2;
        }

        public Pair() {

        }

        public boolean full() {
            return this.g1 != null && this.g2 != null;
        }

        public T getRandom() {
            if (full()) {
                if (F.RND.nextBoolean()) {
                    return g1;
                } else {
                    return g2;
                }
            } else {
                if (g1 == null) {
                    return g2;
                } else {
                    return g1;
                }
            }
        }
    }

    public CrossoverList(Genome net1, Genome net2) {

        ArrayList<Gene> list1 = new ArrayList<>();
        ArrayList<Gene> list2 = new ArrayList<>();
        prepareList(net1, list1);
        prepareList(net2, list2);
        int i1 = 0;
        int i2 = 0;
        int combinedSize = list1.size() + list2.size();
        while (i1 + i2 < combinedSize) {
            Pair<Gene> pair = new Pair<>();
            if (i1 >= list1.size()) {
                if (excessIndexStart == -1) {
                    excessIndexStart = geneList.size();
                }
                pair.g2 = list2.get(i2);
                i2++;
            } else if (i2 >= list2.size()) {
                if (excessIndexStart == -1) {
                    excessIndexStart = geneList.size();
                }
                pair.g1 = list1.get(i1);
                i1++;
            } else {
                Gene g1 = list1.get(i1);
                Gene g2 = list2.get(i2);
                int cmp = ArrayBasedCounter.compareCounterAscending.compare(g1.inn, g2.inn);
                if (cmp == 0) {
                    pair.g1 = g1;
                    pair.g2 = g2;
                    i1++;
                    i2++;
                    matchingGene++;
                    this.matchingWeightSum += Math.abs(g1.w - g2.w);
                } else if (cmp < 0) {
                    pair.g1 = g1;
                    i1++;
                } else {
                    pair.g2 = g2;
                    i2++;
                }
            }
            if (pair.g1 == null && pair.g2 == null) {
                Log.print("NULL GENE PAIR", i1, i2);
            }
            geneList.add(pair);
        }

        //BIAS pairing
        ArrayList<? extends NeuronInfo> bigger, smaller;
        if (net1.bias.size() >= net2.bias.size()) {
            bigger = net1.bias;
            smaller = net2.bias;
        } else {
            bigger = net2.bias;
            smaller = net1.bias;
        }
        for (int i = 0; i < smaller.size(); i++) {
            Pair<NeuronInfo> pair = new Pair<>(bigger.get(i), smaller.get(i));
            this.biasList.add(pair);
        }
        for (int i = smaller.size(); i < bigger.size(); i++) {
            Pair<NeuronInfo> pair = new Pair<>();
            pair.g1 = bigger.get(i);
            this.biasList.add(pair);
        }
        this.disjointBias = bigger.size() - smaller.size();
    }

    private void prepareList(Genome net, ArrayList list) {
        list.clear();
        for (Gene l : net.genes) {
            list.add(l.clone());
        }
//        Collections.sort(list, (Gene o1, Gene o2) -> (F.StringNumCompare(o1.innovation, o2.innovation)));
    }

}
