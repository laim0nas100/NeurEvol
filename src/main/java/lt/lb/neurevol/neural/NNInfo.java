/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.neural;

import java.util.*;

/**
 *
 * @author laim0nas100
 */
public class NNInfo {

    public int inputs, outputs;
    public Collection<Synapse> links = new ArrayList<>();
    public Collection<NeuronInfo> biases = new ArrayList<>();
    public transient Map<Integer, ActivationFunction> activationMap = new HashMap<>();
    public transient ActivationFunction defaultActivation;

}
