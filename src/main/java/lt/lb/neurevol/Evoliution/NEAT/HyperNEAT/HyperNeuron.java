/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.HyperNEAT;

import lt.lb.neurevol.Neural.CPPN.Pos;
import lt.lb.neurevol.Neural.NeuronInfo;

/**
 *
 * @author Lemmin
 */
public class HyperNeuron extends NeuronInfo implements Cloneable {

    public Pos position;

    public HyperNeuron(Number... coordinates) {
        super();
        position = new Pos(coordinates);
    }

    @Override
    public Object clone() {

        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
}
