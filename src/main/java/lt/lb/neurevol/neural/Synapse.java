package lt.lb.neurevol.neural;

import lt.lb.commons.interfaces.CloneSupport;

/**
 *
 * @author laim0nas100
 */
public class Synapse implements CloneSupport<Synapse> {

    public int in = -1;
    public int out = -1;
    public double w = 0;

    public Synapse() {
    }
    
    protected Synapse(Synapse syn){
        this.in = syn.in;
        this.out = syn.out;
        this.w = syn.w;
    }

    public Synapse(int in, int out, double w) {
        this.in = in;
        this.out = out;
        this.w = w;
    }

    @Override
    public Synapse clone() {
        return new Synapse(this);
    }

    @Override
    public String toString() {
        return in + "[" + w + "]" + out;
    }

}
