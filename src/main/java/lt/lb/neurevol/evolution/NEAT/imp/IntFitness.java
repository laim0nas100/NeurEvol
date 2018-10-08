/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.imp;

import lt.lb.commons.F;
import lt.lb.commons.containers.Value;
import lt.lb.neurevol.evolution.NEAT.interfaces.Fitness;

/**
 *
 * @author Lemmin
 */
public class IntFitness extends Value<Integer> implements Fitness {

    public IntFitness(int in) {
        super(in);
    }

    @Override
    public int compareTo(Fitness o) {
        if (o instanceof IntFitness) {
            IntFitness other = F.cast(o);
            return this.get() - other.get();
        }else{
            return 0;
        }
    }

}
