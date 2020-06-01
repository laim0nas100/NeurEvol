package lt.lb.neurevol.neural;

import lt.lb.commons.interfaces.CloneSupport;

/**
 *
 * @author laim0nas100
 */
public class NeuronInfo implements CloneSupport<NeuronInfo> {

    public Double bias;
    public Integer afType;

    public NeuronInfo() {
        bias = 0d;
        afType = null;
    }
    
    protected NeuronInfo(NeuronInfo neur){
        this.bias = neur.bias;
        this.afType = neur.afType;
    }

    @Override
    public NeuronInfo clone() {
        return new NeuronInfo(this);
    }

}
