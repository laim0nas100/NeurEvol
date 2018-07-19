package lt.lb.neurevol.Evolution.NEAT;

import lt.lb.commons.ArrayBasedCounter;
import lt.lb.neurevol.Neural.Synapse;

public class Gene extends Synapse implements Comparable, Cloneable {

    public boolean en = true;
    public int[] inn = null;

    public Gene(int in, int out) {
        this.in = in;
        this.out = out;
    }

    public Gene() {
    }

    @Override
    public Object clone() {
        final Gene gene;
        gene = (Gene) super.clone();

        return gene;
    }

    @Override
    public int compareTo(Object o) {
        Gene other = (Gene) o;
        return ArrayBasedCounter.compareCounterAscending.compare(inn, other.inn);
    }

}
