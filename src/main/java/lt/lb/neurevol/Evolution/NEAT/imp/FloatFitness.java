/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evolution.NEAT.imp;

import lt.lb.commons.containers.Value;
import lt.lb.neurevol.Evolution.NEAT.interfaces.Fitness;

public class FloatFitness extends Value<Float> implements Fitness {

    public FloatFitness() {
    }

    public FloatFitness(Float val) {
        super(val);
    }

    @Override
    public int compareTo(Fitness t) {
        if (t instanceof FloatFitness) {
            return Float.compare(this.get(), ((FloatFitness) t).get());
        }
        return 0;
    }

    @Override
    public String toString() {
        return this.value + "";
    }

}
