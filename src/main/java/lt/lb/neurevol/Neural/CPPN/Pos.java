/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Neural.CPPN;

import java.util.Arrays;

/**
 *
 * @author Lemmin
 */
public class Pos {

    public static class MinMax {

        public Number min, max;

        public MinMax(Number min, Number max) {
            this.min = min;
            this.max = max;
        }
    }
    private Double[] vector;

    public Pos(Number... coordinates) {
        vector = new Double[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            vector[i] = coordinates[i].doubleValue();
        }
    }

    public Double[] normalized(MinMax[] minmax) {
        Double[] res = new Double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            Double min = minmax[i].min.doubleValue();
            Double max = minmax[i].max.doubleValue();
            res[i] = (vector[i] - min) / (max - min);
        }
        return res;
    }

    public Double[] normalized(MinMax[] minmax, Number rangeStart, Number rangeEnd) {
        Double[] res = this.normalized(minmax);
        for (int i = 0; i < res.length; i++) {
            res[i] = res[i] * (rangeEnd.doubleValue() - rangeStart.doubleValue()) + rangeStart.doubleValue();
        }
        return res;
    }

    public Double[] get() {
        return vector;
    }

    public Integer dim() {
        return vector.length;
    }

    /*
     * 1,2
     * 2,1
     */
    @Override
    public String toString() {
        return Arrays.asList(vector).toString();
    }

    public Double euclidianDistance(Pos to) {
        Double sum = 0d;

        int len = Math.min(to.vector.length, this.vector.length);
        for (int i = 0; i < len; i++) {
            sum += Math.sqrt(Math.pow(this.vector[i], 2) - Math.pow(to.vector[i], 2));
        }
        Pos higherDim = null;
        if (len < to.vector.length) {
            higherDim = to;
        } else if (len < this.vector.length) {
            higherDim = this;
        }
        if (higherDim != null) {
            for (int i = len; i < higherDim.vector.length; i++) {
                sum += Math.abs(higherDim.vector[i]);
            }
        }
        return sum;
    }

}
