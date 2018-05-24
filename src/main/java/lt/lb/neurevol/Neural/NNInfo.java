/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Neural;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Lemmin
 */
public class NNInfo {

    public int inputs, outputs;
    public Map<Integer, Neuron> neurons;
    public Collection<Synapse> links;
    public Collection<NeuronInfo> biases;
    public transient Map<Integer, ActivationFunction> activationMap;
    public transient ActivationFunction defaultActivation;

}
