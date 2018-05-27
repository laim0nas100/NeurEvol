/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.HyperNEAT.imp;

import java.util.ArrayList;
import lt.lb.neurevol.Evoliution.NEAT.HyperNEAT.*;
import lt.lb.neurevol.Misc.Pos;

public class HyperSpaceToSubstrateLayerTransformerImpl implements HyperSpaceToSubstrateLayerTransformer {

    @Override
    public SubstrateNeuronLayer produce(HyperSpace space) {

        SubstrateNeuronLayer layer = new SubstrateNeuronLayer();
        for (ArrayList<Pos> posList : space.layers) {
            for (Pos pos : posList) {
                HyperNeuron neuron = new HyperNeuron(pos);
                layer.neurons.add(neuron);
            }
        }
        return layer;
    }

}
