/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.HyperNEAT;

import java.util.ArrayList;
import java.util.HashMap;
import lt.lb.commons.Log;
import lt.lb.neurevol.Neural.CPPN.Pos;

/**
 *
 * @author Lemmin
 */
public class HyperNEATSpace {

    public ArrayList<ArrayList<Pos>> layers;
    public HashMap<Pos, Integer> idMap;

    public int[] dimensions;

    public HyperNEATSpace(int... dimensions) {
        this.dimensions = dimensions;
        this.initialPositions();
    }

    public int getLayerSize() {
        return layers.get(0).size();
    }

    public int getDimensionSum() {
        int dimSum = 1;
        for (int i = 0; i < dimensions.length; i++) {
            dimSum *= dimensions[i];
        }
        return dimSum;
    }

    public final void initialPositions() {
        layers = new ArrayList<>();
        idMap = new HashMap<>();
        for (int i = 0; i < getLayerCount(); i++) {
            layers.add(new ArrayList<>());
        }
        for (int i = 0; i < getDimensionSum(); i++) {

            Integer[] var = new Integer[dimensions.length];

            int t = i;
            for (int j = 0; j < dimensions.length; j++) {
                var[j] = t % dimensions[j];
                t = t / dimensions[j];
            }

            Pos p;
            if (this.getLayerCount() != 1) {
                p = new Pos(var);
            } else {
                Integer[] var2 = new Integer[dimensions.length - 1];
                for (int j = 0; j < var2.length; j++) {
                    var2[j] = var[j];
                }
                p = new Pos(var2);

            }
            int last = var[var.length - 1];
            layers.get(last).add(p);
        }
        int i = 0;
        for (Pos P : layers.get(0)) {
            idMap.put(P, i++);
        }
        for (Pos P : layers.get(getLayerCount() - 1)) {
            idMap.put(P, i++);
        }

        for (int j = 1; j < layers.size() - 1; j++) {
            for (Pos p : layers.get(j)) {
                idMap.put(p, i++);
            }
        }
        Log.println("HYPER NEAT SPACE INIT");
    }

    public Integer getLayerCount() {
        return dimensions[dimensions.length - 1];
    }
}
