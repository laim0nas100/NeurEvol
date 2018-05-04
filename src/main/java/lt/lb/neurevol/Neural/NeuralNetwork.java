/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Neural;

import LibraryLB.Log;
import java.util.*;

/**
 *
 * @author Lemmin
 */
public class NeuralNetwork {

    public int inputs, outputs;
    public HashMap<Integer, Neuron> neurons = new HashMap<>();

    public NeuralNetwork(int inputs, int outputs, Collection<? extends Synapse> genes, Collection<NeuronInfo> biases) {
        this(inputs, outputs, genes, biases, new HashMap<>(), (d) -> Math.tanh(d));
    }

    public NeuralNetwork(int inputs, int outputs, Collection<? extends Synapse> links, Collection<NeuronInfo> biases,
            Map<Integer, ActivationFunction> activationMap, ActivationFunction defaultActivation) {
        this.inputs = inputs;
        this.outputs = outputs;
        int neuronID = 0;
        for (NeuronInfo val : biases) {
            Neuron n = new Neuron(neuronID++);
            n.bias = val.bias;
            neurons.put(n.ID, n);
            if (activationMap.containsKey(val.afType)) {
                n.af = activationMap.get(val.afType);
            } else {
                n.af = defaultActivation;
            }
        }
        for (Synapse g : links) {
            Neuron get = neurons.get(g.out);
            if (get == null) {
                Log.print("NULL NEURON");
            } else {
                get.addLink(g);
            }

        }
    }

    public Double[] evaluate(Double[] inputs) {
        Double[] output = new Double[this.outputs];
        for (Neuron n : neurons.values()) {
            n.value = null;
        }
        ArrayList<Neuron> in = getInputs();
        for (int i = 0; i < this.inputs; i++) {
            Neuron n = in.get(i);
            n.value = n.af.activate(inputs[i] + n.bias);
        }
        ArrayList<Neuron> out = getOutputs();
        for (int i = 0; i < this.outputs; i++) {
            output[i] = out.get(i).resolve(neurons);
        }
        return output;
    }

    public final ArrayList<Neuron> getInputs() {
        ArrayList<Neuron> list = new ArrayList<>();
        for (int i = 0; i < inputs; i++) {
            list.add(neurons.get(i));
        }
        return list;
    }

    public final ArrayList<Neuron> getOutputs() {
        ArrayList<Neuron> list = new ArrayList<>();
        for (int i = 0; i < outputs; i++) {
            list.add(neurons.get(i + inputs));
        }
        return list;
    }

    public HashSet<Integer> getParentSet(int ID) {
        HashSet<Integer> newSet = new HashSet<>();
        HashSet<Integer> visitNextIteration = new HashSet<>();
        HashSet<Integer> parentSet = new HashSet<>();
        visitNextIteration.add(ID);
        while (!visitNextIteration.isEmpty()) {
            newSet.clear();
            for (Integer up : visitNextIteration) {
                if (parentSet.contains(up)) {
                    continue;
                }
                parentSet.add(up);
                Neuron parent = neurons.get(up);
                newSet.addAll(parent.input.keySet());
            }
            visitNextIteration.clear();
            visitNextIteration.addAll(newSet);
        }
        return parentSet;
    }

}
