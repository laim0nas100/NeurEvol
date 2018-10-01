/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.HyperNEAT;

import lt.lb.neurevol.neural.NNInfo;
import lt.lb.neurevol.neural.NeuralNetwork;

/**
 *
 * @author laim0nas100
 */
public interface SubstrateToNNInfoProducer {

    public NNInfo produce(Substrate subs, NeuralNetwork linksNetwork, ConnectionProducer prod);
}
