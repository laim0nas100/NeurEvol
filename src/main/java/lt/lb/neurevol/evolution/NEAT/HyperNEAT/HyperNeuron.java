/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.HyperNEAT;

import lt.lb.commons.F;
import lt.lb.commons.misc.*;
import lt.lb.neurevol.neural.NeuronInfo;

/**
 *
 * @author laim0nas100
 */
public class HyperNeuron extends NeuronInfo implements Cloneable {

    public Pos position;
    public Integer id;
    public transient SubstrateLayer substrateLayer;

    public Interval[] getSpaceDimensions() {
        if (this.substrateLayer instanceof DimentionInfo) {
            DimentionInfo cast = F.cast(this.substrateLayer);

            return cast.getDimentions();
        }
        throw new UnsupportedOperationException("Layer must implement " + DimentionInfo.class + " interface");
    }

    public HyperNeuron(Number... coordinates) {
        super();
        position = new Pos(coordinates);
    }

    public HyperNeuron(Pos pos) {
        super();
        this.position = pos;
    }

    @Override
    public Object clone() {

        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
}
