/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT;

import java.io.Serializable;
import java.util.*;
import lt.lb.neurevol.evolution.NEAT.interfaces.Fitness;
import lt.lb.neurevol.evolution.Control.NEATConfig;

/**
 *
 * @author laim0nas100
 */
public class Species<T extends Agent> implements Serializable {

    public transient int id;
    public Fitness bestFitness;
    public transient double avgRank = 0.0;
    public int staleness = 0;
    public ArrayList<T> agents = new ArrayList<>();

    public NEATConfig conf;

    public Agent getLeader() {

        Collections.sort(agents, conf.getSorter().getComparator());
        return agents.get(0);
    }

    public List<T> cullSpecies(double selection, boolean leave1) {
        Collections.sort(agents, conf.getSorter().getComparator());
        int survivors = 1;
        if (!leave1) {
            survivors = (int) Math.ceil(selection * agents.size());
        }

        ArrayDeque<T> survived = new ArrayDeque<>(survivors);
        LinkedList<T> dead = new LinkedList<>();
        dead.addAll(agents);
        agents.clear();

        for (int i = 0; i < survivors; i++) {
            survived.add(dead.removeFirst());
        }
        agents.addAll(survived);
        return dead;
    }

    public double calculateAverageRank() {
        double total = 0.0;
        for (final Agent genome : agents) {
            total += genome.globalRank;
        }
        avgRank = total / agents.size();
        return avgRank;
    }

    public int size() {
        return agents.size();
    }
}
