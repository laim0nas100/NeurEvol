/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.interfaces;

import lt.lb.neurevol.evolution.NEAT.Agent;

/**
 *
 * @author laim0nas100
 */
public interface AgentSimilarityEvaluator<T extends Agent> {

    public double similarity(T g1, T g2);

}
