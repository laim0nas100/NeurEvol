/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.HyperNEAT.imp;

import lt.lb.neurevol.evolution.NEAT.HyperNEAT.ConnectionProducer;
import lt.lb.neurevol.evolution.NEAT.HyperNEAT.HyperNeuron;
import lt.lb.neurevol.neural.NeuronInfo;
import lt.lb.neurevol.neural.Synapse;

public class ConnectionProducerImpl implements ConnectionProducer {

    double threshold;
    int use;

    @Override
    public Synapse produce(NeuronInfo in, NeuronInfo to, Double[] weigths) {
        double w = weigths[this.use];

        HyperNeuron hIn = (HyperNeuron) in;
        HyperNeuron hTo = (HyperNeuron) to;
        if (Math.abs(w) > threshold) {
            return new Synapse(hIn.id, hTo.id, w);
        }

        return null;

    }

}
