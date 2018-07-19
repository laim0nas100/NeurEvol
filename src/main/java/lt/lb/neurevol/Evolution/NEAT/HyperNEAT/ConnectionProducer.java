/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evolution.NEAT.HyperNEAT;

import lt.lb.neurevol.Neural.NeuronInfo;
import lt.lb.neurevol.Neural.Synapse;

/**
 *
 * @author Lemmin
 */
public interface ConnectionProducer {

    public Synapse produce(NeuronInfo in, NeuronInfo to, Double[] weigths);
}
