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
    
    protected HyperNeuron(HyperNeuron neur){
        super(neur);
        this.id = neur.id;
        this.position = neur.position;
        
    }

    public HyperNeuron clone() {
        return new HyperNeuron(this);
    }
}
