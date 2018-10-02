/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.neural.learn.imp;

import java.util.*;
import java.util.function.Predicate;
import lt.lb.commons.containers.Value;
import lt.lb.commons.F;
import lt.lb.neurevol.neural.*;
import lt.lb.neurevol.neural.learn.NeuralNetworkChanger;

/**
 *
 * @author laim0nas100
 */
public class BackpropagationChanger implements NeuralNetworkChanger {

    private Map<Integer, ActivationFunction> functionDerivatives;
    private Map<Integer, Double> output;
    private Map<Integer, Double> goal;
    private ActivationFunction defaultDerivative;

    public BackpropagationChanger(Map<Integer, ActivationFunction> derivatives, Map<Integer, Double> output, Map<Integer, Double> goal, ActivationFunction defaultDer) {
        this.functionDerivatives = derivatives;
        this.output = output;
        this.goal = goal;
        this.defaultDerivative = defaultDer;
    }

    @Override
    public NNInfo apply(NNInfo type) {
        Map<Integer, BackNeuron> neurons = new HashMap<>();
        F.iterate(type.biases, (i, n) -> {
              BackNeuron bn = new BackNeuron(i);
              bn.af = type.activationMap.getOrDefault(n.afType, type.defaultActivation);
              bn.derivative = this.functionDerivatives.getOrDefault(n.afType, defaultDerivative);
              bn.bias = n.bias;
              neurons.put(bn.ID, bn);
          });

        F.iterate(type.links, (i, s) -> {
              BackNeuron get = neurons.get(s.out);
              get.addLink(s);
          });

        List<BackNeuron> evaluationOrder = new ArrayList<>();

        Predicate<BackNeuron> freeToMark = (n) -> {
            if (n.output.isEmpty()) {
                return true;
            } else {
                return n.output.keySet().stream().allMatch((i) -> neurons.get(i).marked);
            }

        };

        while (true) {

            Value<Boolean> change = new Value<>(false);
            neurons.values().stream().filter(freeToMark).forEach(n -> {
                n.marked = true;
                evaluationOrder.add(n);
                change.set(true);
            });
            if (!change.get()) {
                break;
            }
        }

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static class BackNeuron extends Neuron {

        ActivationFunction derivative;
        boolean marked;

        Map<Integer, Double> output = new HashMap<>();

        public BackNeuron(int i) {
            super(i);
        }

        @Override
        public void addLink(Synapse s) {
            super.addLink(s);
            if (s.in == ID) {
                output.put(s.out, s.w);
            }
        }
    }


}
