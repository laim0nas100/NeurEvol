/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.HyperNEAT.imp;

import java.util.*;
import lt.lb.neurevol.Evoliution.NEAT.HyperNEAT.*;
import lt.lb.neurevol.Misc.*;
import lt.lb.neurevol.Neural.*;

public class SubstrateToNNInfoProducerImpl implements SubstrateToNNInfoProducer {

    public double threshold = 0.2;
    public Interval mm = new Interval(-1, 1);
    public Interval[] minmax = new Interval[5];

    @Override
    public NNInfo produce(Substrate subs, NeuralNetwork net, ConnectionProducer prod) {

        NNInfo info = new NNInfo();
        info.activationMap = F.getDefaultActivationMap();
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
        for (int i = 0; i < this.minmax.length; i++) {
            this.minmax[i] = this.mm;
        }
        List<Pair<HyperNeuron>> links = subs.produceLinks();

        Collection<Synapse> finalLinks = new ArrayDeque<>();

        for (Pair<HyperNeuron> pair : links) {

            Pos posFrom = pair.g1.position;
            Pos posTo = pair.g2.position;
            int inputDim = posTo.dim() + posFrom.dim();
            Double[] input = new Double[inputDim];
            Double[] normalizedFrom = posFrom.normalized(minmax);
            int posFromDim = posFrom.dim();
            for (int i = 0; i < posFromDim; i++) {
                input[i] = normalizedFrom[i];
            }
            Double[] normalizedTo = posFrom.normalized(minmax);
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
