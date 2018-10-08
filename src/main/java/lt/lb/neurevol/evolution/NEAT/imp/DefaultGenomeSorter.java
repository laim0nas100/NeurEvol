/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.imp;

import java.util.*;
import lt.lb.neurevol.evolution.NEAT.Agent;
import lt.lb.neurevol.evolution.NEAT.Genome;
import lt.lb.neurevol.evolution.NEAT.interfaces.AgentSorter;

public class DefaultGenomeSorter implements AgentSorter<Genome> {

    public Comparator<Agent> fitnessAscending = (Agent o1, Agent o2) -> {

        return o1.fitness.compareTo(o2.fitness);
    };

    public Comparator<Agent> fitnessDescending = fitnessAscending.reversed();

}
