/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.interfaces;

import java.util.List;
import lt.lb.neurevol.Evoliution.NEAT.Genome;

/**
 *
 * @author Laimonas-Beniusis-PC
 */
public interface GenomeBreeder {

    public Genome breedChild(List<Genome> genomes);

}
