/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.neural;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author laim0nas100
 */
public class Neuron {

    public int ID;
    public double bias;
    public Double value = null;
    public ActivationFunction af;
    public Map<Integer, Double> input = new HashMap<>();

    public Neuron(int i) {
        ID = i;
    }

    public void addLink(Synapse g) {
        if (g.out == ID) {
            input.put(g.in, g.w);
        }
    }

    public double resolve(Map<Integer, Neuron> neurons) {
        if (value == null) {
            value = 0d;
            double d = 0;
            for (Map.Entry<Integer, Double> entry : input.entrySet()) {
                d += entry.getValue() * neurons.get(entry.getKey()).resolve(neurons);
            }
            value = af.activate(d + bias);
        }
        return value;
    }
}
