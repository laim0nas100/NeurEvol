/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.imp;

import java.util.*;
import lt.lb.neurevol.Evoliution.NEAT.Genome;
import lt.lb.neurevol.Evoliution.NEAT.interfaces.GenomeSorter;

public class DefaultGenomeSorter implements GenomeSorter {

    @Override
    public void rankGlobaly(Collection<Genome> genomes) {
        ArrayList<Genome> global = new ArrayList<>();

        global.addAll(genomes);
        Collections.sort(global, getComparator().reversed());

        int rank = 1;
        for (Genome g : global) {
            g.globalRank = rank++;
        }
    }

    @Override
    public Comparator<Genome> getComparator() {
        return (Genome t, Genome t1) -> Genome.fitnessDescending.compare(t, t1);
    }

}
