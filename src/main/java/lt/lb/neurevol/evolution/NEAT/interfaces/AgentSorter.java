/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import lt.lb.neurevol.evolution.NEAT.Agent;

/**
 *
 * @author laim0nas100
 * @param <T> any subclass of Agent
 */
public interface AgentSorter<T extends Agent> {

    /**
     * Better agent should appear before worse agent
     * @return Comparator which compares agents' fitness
     */
    public default Comparator<T> getComparator() {
        return (T o1, T o2) -> {
            return o1.fitness.compareTo(o2.fitness);
        };
    }
    
    public default Comparator<Fitness> fitnessAscending(){
        return (Fitness o1, Fitness o2) -> {
            return o1.compareTo(o2);
        };
    }

    public default Comparator<Fitness> fitnessDescending(){
        return fitnessAscending().reversed();
    }
    
    /**
     * Sorts all agents by fitness and orders it by global influence.
     * Default implementation increments by one.
     * @param agents 
     */
    public default void rankGlobaly(Collection<T> agents) {
        ArrayList<T> global = new ArrayList<>();

        global.addAll(agents);
        Collections.sort(global, getComparator());

        int rank = 1;
        for (T agent : global) {
            agent.influenceGlobally = rank++;
        }

    }

}
