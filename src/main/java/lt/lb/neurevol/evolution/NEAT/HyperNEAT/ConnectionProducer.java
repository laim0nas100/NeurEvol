/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.HyperNEAT;

import lt.lb.neurevol.neural.NeuronInfo;
import lt.lb.neurevol.neural.Synapse;

/**
 *
 * @author laim0nas100
 */
public interface ConnectionProducer {

    public Synapse produce(NeuronInfo in, NeuronInfo to, Double[] weigths);
}
