package lt.lb.neurevol.evolution.NEAT;

import lt.lb.commons.ArrayBasedCounter;
import lt.lb.commons.ArrayOp;
import lt.lb.neurevol.neural.Synapse;

public class Gene extends Synapse implements Comparable {

    public boolean en = true;
    public int[] inn = null;

    public Gene(int in, int out) {
        this(in, out, 0);
    }

    public Gene(int in, int out, double w) {
        super(in, out, w);
    }

    protected Gene(Gene gene) {
        super(gene);
        this.en = gene.en;
        this.inn = ArrayOp.clone(gene.inn);

    }

    public Gene() {
    }

    @Override
    public Gene clone() {
        return new Gene(this);
    }

    @Override
    public int compareTo(Object o) {
        Gene other = (Gene) o;
        return ArrayBasedCounter.compareCounterAscending.compare(inn, other.inn);
    }

}
