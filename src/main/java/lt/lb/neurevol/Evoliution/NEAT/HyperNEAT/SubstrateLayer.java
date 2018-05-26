/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.HyperNEAT;

import java.util.HashMap;
import java.util.Map;
import lt.lb.commons.UUIDgenerator;

/**
 *
 * @author Lemmin
 */
public class SubstrateLayer {

    public static enum SLayerType {
        INPUT, OUTPUT, HIDDEN;
    }

    public SLayerType type;

    public String ID = UUIDgenerator.nextUUID("SubstrateLayer");

    public Map<String, SubstrateLayer> connectedTo = new HashMap<>();
    public Map<String, SubstrateLayer> connectedFrom = new HashMap<>();

    public void connectTo(SubstrateLayer layer) {
        layer.connectedFrom.put(ID, this);
        this.connectedTo.put(layer.ID, layer);
    }

}
