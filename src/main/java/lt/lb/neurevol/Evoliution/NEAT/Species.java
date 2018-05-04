/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Lemmin
 */
public class Species implements Serializable {

    public transient int id;
    public double bestFitness = 0.0;
    public transient double avgRank = 0.0;
    public int staleness = 0;
    public transient ArrayList<Genome> backup = new ArrayList<>();
    public ArrayList<Genome> genomes = new ArrayList<>();

    public Genome getLeader() {
        Collections.sort(genomes, Genome.fitnessDescending);
        return genomes.get(0);
    }

    public List<Genome> cullSpecies(double selection, boolean leave1) {
        Collections.sort(genomes, Genome.fitnessDescending);
        int survivors = 1;
        if (!leave1) {
            survivors = (int) Math.ceil(selection * genomes.size());
        }

        ArrayDeque<Genome> survived = new ArrayDeque<>(survivors);
        LinkedList<Genome> dead = new LinkedList<>();
        dead.addAll(genomes);
        genomes.clear();

        for (int i = 0; i < survivors; i++) {
            survived.add(dead.removeFirst());
        }
        genomes.addAll(survived);
        return dead;
    }

    public double calculateAverageRank() {
        double total = 0.0;
        for (final Genome genome : genomes) {
            total += genome.globalRank;
        }
        avgRank = total / genomes.size();
        return avgRank;
    }

    public int size() {
        return genomes.size();
    }
}
