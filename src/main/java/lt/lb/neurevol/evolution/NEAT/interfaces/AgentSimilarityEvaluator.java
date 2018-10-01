/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.interfaces;

import lt.lb.neurevol.evolution.NEAT.Agent;

/**
 *
 * @author Laimonas-Beniusis-PC
 */
public interface AgentSimilarityEvaluator {

    public double similarity(Agent g1, Agent g2);

}
