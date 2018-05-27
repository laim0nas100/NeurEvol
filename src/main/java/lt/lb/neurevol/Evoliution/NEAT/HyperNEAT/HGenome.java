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

    public transient boolean updateNNInfo = false;

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
        cachedNNInfo = nnProducer.produce(subs, this.getNetwork(), conProducer);
        updateNNInfo = false;
        return cachedNNInfo;
    }

    @Override
    public NeuralNetwork getNetwork() {
        this.updateNNInfo = needUpdate;
        return super.getNetwork();
    }

    public NNInfo getNNInfo() {
        if (cachedNNInfo == null || updateNNInfo) {
            return generateNNInfo();
        }
        return cachedNNInfo;
    }

    @Override
    public Object clone() {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}
