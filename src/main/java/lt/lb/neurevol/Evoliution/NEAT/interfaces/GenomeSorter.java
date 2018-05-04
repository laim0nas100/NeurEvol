/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.interfaces;

import java.util.Collection;
import java.util.Comparator;
import lt.lb.neurevol.Evoliution.NEAT.Genome;

/**
 *
 * @author Laimonas-Beniusis-PC
 */
public interface GenomeSorter {

    public void rankGlobaly(Collection<Genome> genomes);

    public Comparator<Genome> getComparator();

}
