/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT;

import java.util.*;
import lt.lb.neurevol.Misc.F;
import lt.lb.neurevol.Neural.*;

/**
 *
 * @author Lemmin
 */
public class Genome implements Cloneable {

    public static Comparator<Genome> fitnessAscending = (Genome o1, Genome o2) -> Double.compare(o1.fitness, o2.fitness);
    public static Comparator<Genome> fitnessDescending = (Genome o1, Genome o2) -> Double.compare(o2.fitness, o1.fitness);
    public static Map<Integer, ActivationFunction> activationMap = F.getDefaultActivationMap();

    public PriorityQueue<Gene> genes = new PriorityQueue<>();
    protected transient NeuralNetwork network;
    public ArrayList<NeuronInfo> bias = new ArrayList<>();

    public float fitness;
    public transient int globalRank;
    public int input, output;
    public transient boolean needUpdate = false;

    public Genome(int input, int output) {
        this.input = input;
        this.output = output;
        for (int i = 0; i < input + output; i++) {
            bias.add(new NeuronInfo());
        }
    }

    public Genome() {
    }

    @Override
    public Object clone() {

        try {
            Genome genome = (Genome) super.clone();
            genome.genes = new PriorityQueue<>();
            genome.bias = new ArrayList<>();
            for (Gene g : new ArrayList<>(this.genes)) {
                genome.genes.add((Gene) g.clone());
            }
            for (NeuronInfo info : this.bias) {
                genome.bias.add((NeuronInfo) info.clone());
            }
            return genome;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    public NeuralNetwork getNetwork() {
        if (needUpdate || network == null) {
            this.generateNetwork();
        }
        return network;
    }

    public NeuralNetwork generateNetwork() {
        if (bias.size() < input + output) {
            for (int i = 0; i < input + output; i++) {
                bias.add(new NeuronInfo());
            }
        }
        network = new NeuralNetwork(input, output, new ArrayList<>(genes), bias, activationMap, F::sigmoid);
        needUpdate = false;
        return network;
    }

    public Double[] evaluate(Double[] input) {
        if (network == null) {
            generateNetwork();
        }
        return network.evaluate(input);
    }

    public double[] evaluate(double[] input) {
        Double[] input1 = new Double[input.length];
        for (int i = 0; i < input.length; i++) {
            input1[i] = input[i];
        }

        Double[] output1 = evaluate(input1);
        double[] outputar = new double[output1.length];
        for (int i = 0; i < output1.length; i++) {
            outputar[i] = output1[i];
        }
        return outputar;

    }

}
