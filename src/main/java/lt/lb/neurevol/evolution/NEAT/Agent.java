/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT;

import lt.lb.neurevol.evolution.NEAT.interfaces.Fitness;

/**
 *
 * @author laim0nas100
 */
public class Agent implements Cloneable{
    public String id;

    public Fitness fitness;
    public transient int globalRank;
    
    @Override
    public Object clone(){
        
        try {
            Agent agent = (Agent) super.clone();
            return agent;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
        
    }
}
