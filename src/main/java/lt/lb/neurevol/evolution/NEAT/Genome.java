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
import lt.lb.commons.F;
import lt.lb.commons.containers.tuples.Tuple;
import lt.lb.neurevol.evolution.Control.Func;
import lt.lb.neurevol.evolution.NEAT.HyperNEAT.HGenome;
import lt.lb.neurevol.evolution.NEAT.interfaces.Fitness;

/**
 *
 * @author laim0nas100
 */
public class Genome extends Agent implements Cloneable {

    public static Map<Integer, ActivationFunction> activationMap = HGenome.getDefaultActivationMap();
    public static ActivationFunction defaultActivationFunction = Func::sigmoid;

    public PriorityQueue<Gene> genes = new PriorityQueue<>();
    protected transient NeuralNetwork network;
    public ArrayList<NeuronInfo> bias = new ArrayList<>();

    public int input, output;
    public transient boolean needUpdate = false;
    public transient Tuple<Map<Integer,ActivationFunction>,ActivationFunction> functions = new Tuple<>(Genome.activationMap,Func::sigmoid);

    public Genome(int input, int output) {
        this(input, output,new Tuple<>(Genome.activationMap,Func::sigmoid));
    }
    
    public Genome(int input, int output, Tuple<Map<Integer,ActivationFunction>,ActivationFunction> functions) {
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
        network = new NeuralNetwork(input, output, new ArrayList<>(genes), bias, activationMap, defaultActivationFunction);
        needUpdate = false;
        return network;
    }

}
