/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evolution.NEAT.HyperNEAT.imp;

import java.util.*;
import lt.lb.commons.containers.Pair;
import lt.lb.commons.misc.F;
import lt.lb.commons.misc.Interval;
import lt.lb.commons.misc.Pos;
import lt.lb.neurevol.Evolution.NEAT.HyperNEAT.*;
import lt.lb.neurevol.Neural.*;
  
public class SubstrateToNNInfoProducerImpl implements SubstrateToNNInfoProducer {

    public Interval normalizationRange;

    
    @Override
    public NNInfo produce(Substrate subs, NeuralNetwork net, ConnectionProducer prod) {

        NNInfo info = new NNInfo();
        info.activationMap = HGenome.getDefaultActivationMap();
        int inputs = 0;
        int outputs = 0;
        info.defaultActivation = F::sigmoid;

        for (SubstrateLayer layer : subs.getTypeLayers(SubstrateLayer.SLayerType.INPUT)) {
            SubstrateNeuronLayer l = (SubstrateNeuronLayer) layer;
            inputs += l.neurons.size();
        }
        for (SubstrateLayer layer : subs.getTypeLayers(SubstrateLayer.SLayerType.OUTPUT)) {
            SubstrateNeuronLayer l = (SubstrateNeuronLayer) layer;
            outputs += l.neurons.size();
        }
        info.inputs = inputs;
        info.outputs = outputs;

        for (HyperNeuron n : subs.getNeuronMap().values()) {
            info.biases.add(n);
        }
        List<Pair<HyperNeuron>> links = subs.produceLinks();

        Collection<Synapse> finalLinks = new ArrayDeque<>();

        for (Pair<HyperNeuron> pair : links) {

            Pos posFrom = pair.g1.position;
            Pos posTo = pair.g2.position;
            int inputDim = posTo.dim() + posFrom.dim();
            Double[] input = new Double[inputDim];
            Double[] normalizedFrom = posFrom.normalized(pair.g1.getSpaceDimensions(), this.normalizationRange.min, this.normalizationRange.max);
            int posFromDim = posFrom.dim();
            for (int i = 0; i < posFromDim; i++) {
                input[i] = normalizedFrom[i];
            }
            Double[] normalizedTo = posTo.normalized(pair.g2.getSpaceDimensions(), this.normalizationRange.min, this.normalizationRange.max);
            for (int i = posFromDim; i < inputDim; i++) {
                input[i] = normalizedTo[i - posFromDim];
            }

            Double[] eval = net.evaluate(input);
            Synapse syn = prod.produce(pair.g1, pair.g2, eval);
            if (syn != null) {
                finalLinks.add(syn);
            }

        }
        info.links = finalLinks;
        return info;

    }

}
