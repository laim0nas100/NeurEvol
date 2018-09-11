/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.HyperNEAT.imp;

import java.util.ArrayList;
import lt.lb.commons.misc.Interval;
import lt.lb.commons.misc.Pos;
import lt.lb.neurevol.evolution.NEAT.HyperNEAT.*;

public class HyperSpaceToSubstrateLayerTransformerImpl implements HyperSpaceToSubstrateLayerTransformer {

    @Override
    public SubstrateNeuronLayer produce(HyperSpace space) {

        Interval[] layerMinMax = new Interval[space.dimensions.length - 1];
        for (int i = 0; i < layerMinMax.length; i++) {
            layerMinMax[i] = new Interval(0, space.dimensions[i]);
        }
        SubstrateNeuronLayer layer = new SubstrateNeuronLayer();
        layer.layerMinMax = layerMinMax;
        for (ArrayList<Pos> posList : space.layers) {
            for (Pos pos : posList) {
                HyperNeuron neuron = new HyperNeuron(pos);
                layer.neurons.add(neuron);
            }
        }
        return layer;
    }

}
