/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evolution.NEAT.interfaces;

import java.util.Collection;
import java.util.Comparator;
import lt.lb.neurevol.Evolution.NEAT.Genome;

/**
 *
 * @author Laimonas-Beniusis-PC
 */
public interface GenomeSorter {

    public void rankGlobaly(Collection<Genome> genomes);

    /*
        Better genome should appear before worse genome
     */
    public default Comparator<Genome> getComparator() {
        return (Genome o1, Genome o2) -> {
            return o2.fitness.compareTo(o1.fitness);
        };
    }

}
