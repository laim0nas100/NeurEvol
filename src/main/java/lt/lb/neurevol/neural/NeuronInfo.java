/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.neural;

/**
 *
 * @author Lemmin
 */
public class NeuronInfo implements Cloneable {

    public Double bias;
    public Integer afType;

    public NeuronInfo() {
        bias = 0d;
        afType = null;
    }

    @Override
    public Object clone() {
        NeuronInfo b = null;
        try {
            b = (NeuronInfo) super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return b;
    }

}
