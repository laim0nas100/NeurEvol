/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.interfaces;

import lt.lb.neurevol.Evoliution.NEAT.Genome;

/**
 *
 * @author Laimonas-Beniusis-PC
 */
public interface GenomeSimilarityEvaluator {

    public double similarity(Genome g1, Genome g2);

}
