/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT;

import lt.lb.neurevol.neural.NeuralNetwork;
import lt.lb.neurevol.neural.ActivationFunction;
import lt.lb.neurevol.neural.NeuronInfo;
import java.util.*;
import lt.lb.commons.misc.F;
import lt.lb.neurevol.evolution.NEAT.HyperNEAT.HGenome;
import lt.lb.neurevol.evolution.NEAT.interfaces.Fitness;

/**
 *
 * @author Lemmin
 */
public class Genome implements Cloneable {

    public static Map<Integer, ActivationFunction> activationMap = HGenome.getDefaultActivationMap();

    public PriorityQueue<Gene> genes = new PriorityQueue<>();
    protected transient NeuralNetwork network;
    public ArrayList<NeuronInfo> bias = new ArrayList<>();

    public String id;

    public Fitness fitness;
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

}
