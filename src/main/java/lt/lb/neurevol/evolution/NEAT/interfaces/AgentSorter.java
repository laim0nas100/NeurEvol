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
 * @author laim0nas100
 */
public interface AgentSorter<T extends Agent> {

    public void rankGlobaly(Collection<T> agents);

    /*
        Better genome should appear before worse genome
     */
    public default Comparator<T> getComparator() {
        return (T o1, T o2) -> {
            return o2.fitness.compareTo(o1.fitness);
        };
    }

}
