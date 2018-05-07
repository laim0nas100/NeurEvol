/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.Coevolution;

import java.util.Collection;
import lt.lb.neurevol.Evoliution.NEAT.Genome;
import lt.lb.neurevol.Evoliution.NEAT.interfaces.Pool;
import lt.lb.neurevol.Misc.Pair;

/**
 *
 * @author Laimonas-Beniusis-PC
 */
public interface PairingProducer {

    public Collection<Pair<Genome>> producePairs(Pool pool);

}
