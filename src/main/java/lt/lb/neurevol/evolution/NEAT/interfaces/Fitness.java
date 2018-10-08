/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.interfaces;

/**
 *
 * @author laim0nas100
 * Fitness itself is like a number. While comparing fitness, worse should appear first.
 * When implementing AgentSorter, better should appear first.
 * 
 */
public interface Fitness extends Comparable<Fitness>{

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
