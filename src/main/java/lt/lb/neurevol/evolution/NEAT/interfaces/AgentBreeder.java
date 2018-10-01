/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.interfaces;

import java.util.List;
import lt.lb.neurevol.evolution.NEAT.Agent;

/**
 *
 * @author laim0nas100
 */
public interface AgentBreeder<T extends Agent> {

    public T breedChild(List<T> genomes);

}
