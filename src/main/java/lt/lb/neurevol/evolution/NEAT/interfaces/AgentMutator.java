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
 * @author laim0nas100
 */
public interface AgentMutator<T extends Agent> {

    public void mutate(T agent);

    public default void setInnovation(ArrayBasedCounter counter){
        
    }

    public default ArrayBasedCounter getInnovation(){
        throw new UnsupportedOperationException("ArrayBasedCounter is not needed");
    }

}
