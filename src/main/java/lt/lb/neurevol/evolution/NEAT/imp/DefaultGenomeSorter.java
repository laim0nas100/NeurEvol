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

public class DefaultGenomeSorter implements AgentSorter {

    public Comparator<Genome> fitnessAscending = (Genome o1, Genome o2) -> {

        return o1.fitness.compareTo(o2.fitness);
    };

    public Comparator<Genome> fitnessDescending = fitnessAscending.reversed();

    @Override
    public void rankGlobaly(Collection<Agent> genomes) {
        ArrayList<Agent> global = new ArrayList<>();

        global.addAll(genomes);
        Collections.sort(global, getComparator().reversed());

        int rank = 1;
        for (Agent g : global) {
            g.globalRank = rank++;
        }

    }

}
