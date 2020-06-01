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
import lt.lb.commons.containers.tuples.Tuple;
import lt.lb.commons.interfaces.CloneSupport;
import lt.lb.neurevol.evolution.Control.Func;
import lt.lb.neurevol.evolution.NEAT.HyperNEAT.HGenome;

/**
 *
 * @author laim0nas100
 */
public class Genome extends Agent {

    public static Map<Integer, ActivationFunction> activationMap = HGenome.getDefaultActivationMap();
    public static ActivationFunction defaultActivationFunction = Func::sigmoid;

    public PriorityQueue<Gene> genes = new PriorityQueue<>();
    protected transient NeuralNetwork network;
    public ArrayList<NeuronInfo> bias = new ArrayList<>();

    public int input, output;
    public transient boolean needUpdate = false;
    

    public Genome(int input, int output) {
        this(input, output, new Tuple<>(Genome.activationMap, Func::sigmoid));
    }

    public Genome(int input, int output, Tuple<Map<Integer, ActivationFunction>, ActivationFunction> functions) {
        this.input = input;
        this.output = output;
        for (int i = 0; i < input + output; i++) {
            bias.add(new NeuronInfo());
        }
    }

    public Genome() {
    }

    protected Genome(Genome gen) {
        super(gen);
        this.genes = CloneSupport.cloneCollectionCast(gen.genes, PriorityQueue::new);
        this.bias = CloneSupport.cloneCollection(gen.bias, ArrayList::new);
        this.input = gen.input;
        this.output = gen.output;
        
    }

    @Override
    public Genome clone() {
        return new Genome(this);
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
