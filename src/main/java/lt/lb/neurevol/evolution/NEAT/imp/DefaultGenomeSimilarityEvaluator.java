/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.imp;

import lt.lb.commons.F;
import lt.lb.neurevol.evolution.NEAT.Agent;
import lt.lb.neurevol.evolution.NEAT.CrossoverList;
import lt.lb.neurevol.evolution.NEAT.Genome;
import lt.lb.neurevol.evolution.NEAT.interfaces.AgentSimilarityEvaluator;

/**
 *
 * @author laim0nas100
 */
public class DefaultGenomeSimilarityEvaluator implements AgentSimilarityEvaluator<Genome> {

    public double DELTA_DISJOINT = 1;
    public double DELTA_EXCESS = 1;
    public double DELTA_WEIGHTS = 0.4;
    
    
    @Override
    public double similarity(Genome net1, Genome net2) {
//        double e = Double.MIN_VALUE;
        CrossoverList cross = new CrossoverList(net1, net2);
        double N = Math.max(net1.genes.size(), net2.genes.size());
        double D = ((double) cross.excessIndexStart - cross.matchingGene) * this.DELTA_DISJOINT / N;
        double E = ((double) cross.geneList.size() - cross.excessIndexStart) * this.DELTA_EXCESS / N;
        double W = (cross.matchingWeightSum / (cross.matchingGene)) * this.DELTA_WEIGHTS;
        double DB = cross.disjointBias;
        double B = cross.biasSum / (cross.biasList.size() - cross.disjointBias);

        return D + E + W + DB + B;
    }

}
