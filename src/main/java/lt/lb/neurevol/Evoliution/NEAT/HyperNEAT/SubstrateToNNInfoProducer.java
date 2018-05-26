/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.HyperNEAT;

import lt.lb.neurevol.Neural.NNInfo;
import lt.lb.neurevol.Neural.NeuralNetwork;

/**
 *
 * @author Lemmin
 */
public interface SubstrateToNNInfoProducer {

    public NNInfo produce(Substrate subs, NeuralNetwork linksNetwork, ConnectionProducer prod);
}
