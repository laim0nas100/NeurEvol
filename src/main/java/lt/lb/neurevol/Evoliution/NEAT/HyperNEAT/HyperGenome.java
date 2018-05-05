/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.HyperNEAT;

import lt.lb.commons.Log;
import java.util.ArrayList;
import lt.lb.neurevol.Evoliution.NEAT.Genome;
import lt.lb.neurevol.Neural.CPPN.Pos;
import lt.lb.neurevol.Neural.CPPN.Pos.MinMax;
import lt.lb.neurevol.Neural.*;

/**
 *
 * @author lemmin
 */
public class HyperGenome extends Genome implements Cloneable {

    //main network generates the evaluating network
    public double maxLinkLength;
    private static double maxVal = 1d;
    private static double minVal = -1d;
    private transient NeuralNetwork generated;
    private transient boolean updateGenerated;
    public transient HyperNEATSpace space;

    public HyperGenome(int... dimensions) {
        super((dimensions.length - 1) * 2, 1);
        //{3,3,9} 3*3 and 9 layers
//        Log.print("Inputs:"+this.input+" "+"Outputs"+this.output);

    }

    @Override
    public Object clone() {
        Object g = super.clone();
        HyperGenome genome = (HyperGenome) g;
        genome.space = this.space;
        return genome;
    }

    @Override
    public NeuralNetwork generateNetwork() {
        updateGenerated = true;
        return super.generateNetwork();
    }

    public NeuralNetwork getEvaluatingNetwork() {
        if (updateGenerated || this.generated == null) {
            generateInner();
        }
        return this.generated;
    }

    @Override
    public Double[] evaluate(Double[] input) {

        Double[] evaluate = getEvaluatingNetwork().evaluate(input);
//        Log.print("Hyper evaluate "+Arrays.toString(evaluate));
        return evaluate;
    }

    private NeuralNetwork generateInner() {
//        Log.print("GENERATE");

        this.updateGenerated = false;
        ArrayList<Synapse> links = new ArrayList<>(space.getDimensionSum());
        ArrayList<Pos> from;
        ArrayList<Pos> to;
        MinMax mm = new MinMax(minVal, maxVal);
        for (int i = 0; i < space.layers.size() - 1; i++) {
            from = space.layers.get(i);
            to = space.layers.get(i + 1);
            MinMax[] minmax = new MinMax[from.size()];

            for (int j = 0; j < minmax.length; j++) {
                minmax[j] = mm;
            }
            for (int iFrom = 0; iFrom < from.size(); iFrom++) {
                Pos posFrom = from.get(iFrom);
                for (int iTo = 0; iTo < to.size(); iTo++) {
                    Pos posTo = to.get(iTo);
                    Synapse syn = new Synapse();

                    syn.in = space.idMap.get(posFrom);
                    syn.out = space.idMap.get(posTo);
                    int inputDim = posTo.dim() + posFrom.dim();
                    Double[] inputs = new Double[inputDim];
                    for (int t = 0; t < posFrom.dim(); t++) {
                        inputs[t] = posFrom.normalized(minmax)[t];
                    }

                    for (int t = posTo.dim(); t < inputDim; t++) {

                        inputs[t] = posTo.normalized(minmax)[t - posFrom.dim()];
                    }
                    Double[] evaluate = this.getNetwork().evaluate(inputs);
                    syn.w = evaluate[0];
                    double threshold = 0.2;
                    if (Math.abs(syn.w) > threshold) {
                        links.add(syn);
                    }

                }
            }
        }
        ArrayList<NeuronInfo> biasList = new ArrayList<>(space.getDimensionSum());
        for (int i = 0; i < space.idMap.size(); i++) {
            biasList.add(new NeuronInfo());
        }

        int layerSize = space.getLayerSize();
        Log.println("generated input/output size:" + layerSize);

        this.generated = new NeuralNetwork(layerSize, layerSize, links, biasList);
        return this.network;

    }

}
