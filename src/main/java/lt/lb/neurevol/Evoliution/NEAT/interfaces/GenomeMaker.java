/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.interfaces;

import java.util.Collection;
import lt.lb.neurevol.Evoliution.NEAT.Genome;

/**
 *
 * @author Lemmin
 */
public interface GenomeMaker {

    public Collection<Genome> initializeGeneration();
}
