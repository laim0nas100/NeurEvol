/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.interfaces;

import java.util.*;
import lt.lb.neurevol.evolution.NEAT.Agent;

/**
 *
 * @author laim0nas100
 */
public interface Pool<T extends Agent> {

    public Collection<T> getPopulation();

    public default List<List<T>> getSubpopulations() {
        ArrayList<List<T>> list = new ArrayList<>();
        ArrayList<T> subpopulation = new ArrayList<>();
        subpopulation.addAll(this.getPopulation());
        list.add(subpopulation);
        return list;
    }

    public void newGeneration();

    public int getGeneration();

    public void setGeneration(int generation);

}
