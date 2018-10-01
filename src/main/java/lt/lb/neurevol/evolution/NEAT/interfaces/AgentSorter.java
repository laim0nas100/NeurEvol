/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.interfaces;

import java.util.Collection;
import java.util.Comparator;
import lt.lb.neurevol.evolution.NEAT.Agent;

/**
 *
 * @author Laimonas-Beniusis-PC
 */
public interface AgentSorter {

    public void rankGlobaly(Collection<Agent> genomes);

    /*
        Better genome should appear before worse genome
     */
    public default Comparator<Agent> getComparator() {
        return (Agent o1, Agent o2) -> {
            return o2.fitness.compareTo(o1.fitness);
        };
    }

}
