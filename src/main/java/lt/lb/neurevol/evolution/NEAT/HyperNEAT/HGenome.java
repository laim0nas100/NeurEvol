/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.HyperNEAT;

import java.util.HashMap;
import java.util.Map;
import lt.lb.neurevol.evolution.Control.Func;
import lt.lb.neurevol.evolution.NEAT.Genome;
import lt.lb.neurevol.neural.ActivationFunction;
import lt.lb.neurevol.neural.NNInfo;
import lt.lb.neurevol.neural.NeuralNetwork;

/**
 *
 * @author laim0nas100
 */
public class HGenome extends Genome {

    public transient Substrate subs;
    public transient SubstrateToNNInfoProducer nnProducer;
    public transient ConnectionProducer conProducer;
    public transient NNInfo cachedNNInfo;
    private transient NeuralNetwork lastUsedNet;

    public HGenome(int input, int output, Substrate subs, SubstrateToNNInfoProducer producer, ConnectionProducer conProd) {
        super(input, output);
        this.subs = subs;
        this.nnProducer = producer;
        this.conProducer = conProd;

    }
    
    protected HGenome(HGenome gen){
        super(gen);
        this.subs = gen.subs;
        this.nnProducer = gen.nnProducer;
        this.conProducer = gen.conProducer;
        
    }

    public NeuralNetwork generateEvaluatingNetwork() {
        return new NeuralNetwork(this.getNNInfo());
    }

    public NNInfo generateNNInfo() {
//        Log.print("Generate NNInfo");
        this.lastUsedNet = this.getNetwork();
        cachedNNInfo = nnProducer.produce(subs, this.lastUsedNet, conProducer);
//        Log.print("Done generating");
        return cachedNNInfo;
    }

    public NNInfo getNNInfo() {
        if (this.lastUsedNet == null || !this.lastUsedNet.equals(this.getNetwork())) {
            return generateNNInfo();
        }
//        Log.print("Return old NNInfo");
        return cachedNNInfo;
    }

    @Override
    public HGenome clone() {
        return new HGenome(this);
    }

    public static Map<Integer, ActivationFunction> getDefaultActivationMap() {
        HashMap<Integer, ActivationFunction> map = new HashMap<>();
        int i = 0;
        map.put(i++, Func::sigmoid);
        map.put(i++, x -> x);
        map.put(i++, x -> -x);
        map.put(i++, Math::sin);
        map.put(i++, Math::sinh);
        map.put(i++, Math::tanh);

//        map.put(i++, x -> Math.exp(-x * x / 2) / Math.sqrt(2 * Math.PI));//gaussian
        map.put(i++, x -> Math.pow(Math.exp(-((x * x) / 2)), 1 / (Math.sqrt(2 * Math.PI))));
        map.put(i++, x -> Math.abs(x));
        map.put(i++, x -> x % 1);
        return map;
    }
}
