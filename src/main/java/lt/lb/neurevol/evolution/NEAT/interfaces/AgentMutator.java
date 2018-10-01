/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.interfaces;

import lt.lb.commons.ArrayBasedCounter;
import lt.lb.neurevol.evolution.NEAT.Agent;

/**
 *
 * @author Lemmin
 */
public interface AgentMutator {

    public void mutate(Agent genome);

    public void setInnovation(ArrayBasedCounter counter);

    public ArrayBasedCounter getInnovation();

}
