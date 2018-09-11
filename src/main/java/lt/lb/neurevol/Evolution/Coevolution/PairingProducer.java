/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evolution.Coevolution;

import java.util.Collection;
import lt.lb.commons.containers.Pair;

/**
 *
 * @author Laimonas-Beniusis-PC
 */
public interface PairingProducer {

    public Collection<Pair<PairingInfo>> producePairs(Integer... sizes);

}
