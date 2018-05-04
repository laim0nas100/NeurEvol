/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Neural;

/**
 *
 * @author Lemmin
 */
public class Synapse implements Cloneable {

    public int in = -1;
    public int out = -1;
    public double w = 0;

    public Synapse() {
    }

    @Override
    public Object clone() {
        Synapse cloned = null;
        try {
            cloned = (Synapse) super.clone();
        } catch (CloneNotSupportedException e) {
            // Should not happen
            throw new AssertionError();
        }
        return cloned;
    }

    @Override
    public String toString() {
        return in + "[" + w + "]" + out;
    }

}
