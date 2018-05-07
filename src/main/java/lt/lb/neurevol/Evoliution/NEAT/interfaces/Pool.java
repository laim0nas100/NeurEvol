/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lt.lb.neurevol.Evoliution.NEAT.Genome;

/**
 *
 * @author Laimonas-Beniusis-PC
 */
public interface Pool {

    public Collection<Genome> getPopulation();

    public default List<List<Genome>> getSubpopulations() {
        ArrayList<List<Genome>> list = new ArrayList<>();
        ArrayList<Genome> subpopulation = new ArrayList<>();
        subpopulation.addAll(this.getPopulation());
        list.add(subpopulation);
        return list;
    }

    public void newGeneration();

}
