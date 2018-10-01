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
 * @author Laimonas-Beniusis-PC
 */
public interface Pool {

    public Collection<Agent> getPopulation();

    public default List<List<Agent>> getSubpopulations() {
        ArrayList<List<Agent>> list = new ArrayList<>();
        ArrayList<Agent> subpopulation = new ArrayList<>();
        subpopulation.addAll(this.getPopulation());
        list.add(subpopulation);
        return list;
    }

    public void newGeneration();

    public int getGeneration();

    public void setGeneration(int generation);

}
