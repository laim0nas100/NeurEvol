/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.interfaces;

/**
 *
 * @author Lemmin
 */
public interface Fitness extends Comparable<Fitness> {

    public static int compareNumberFitnessBiggerFirst(Number n1, Number n2) {
        double d1 = n1.doubleValue();
        double d2 = n2.doubleValue();
        if (d1 > d2) {
            return -1;
        }
        if (d1 < d2) {
            return 1;
        }
        return 0;

    }

}
