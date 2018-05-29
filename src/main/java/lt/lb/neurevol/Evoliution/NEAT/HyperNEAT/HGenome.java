/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.HyperNEAT;

import lt.lb.commons.Log;
import lt.lb.neurevol.Evoliution.NEAT.Genome;
import lt.lb.neurevol.Neural.NNInfo;
import lt.lb.neurevol.Neural.NeuralNetwork;

/**
 *
 * @author Lemmin
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

    public NeuralNetwork generateEvaluatingNetwork() {
        return new NeuralNetwork(this.getNNInfo());
    }

    public NNInfo generateNNInfo() {
        Log.print("Generate NNInfo");
        this.lastUsedNet = this.getNetwork();
        cachedNNInfo = nnProducer.produce(subs, this.lastUsedNet, conProducer);
        Log.print("Done generating");
        return cachedNNInfo;
    }

    public NNInfo getNNInfo() {
        if (this.lastUsedNet == null || !this.lastUsedNet.equals(this.getNetwork())) {
            return generateNNInfo();
        }
        Log.print("Return old NNInfo");
        return cachedNNInfo;
    }

    @Override
    public Object clone() {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}
