/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lt.lb.neurevol.Evoliution.NEAT.interfaces.Pool;

/**
 *
 * @author Laimonas-Beniusis-PC
 */
public class MultiPool implements Pool {

    public ArrayList<Pool> pools = new ArrayList<>();

    @Override
    public Collection<Genome> getPopulation() {
        ArrayList<Genome> list = new ArrayList<>();
        for (Pool p : pools) {
            list.addAll(p.getPopulation());
        }
        return list;
    }

    @Override
    public List<List<Genome>> getSubpopulations() {
        ArrayList<List<Genome>> list = new ArrayList<>();
        for (Pool p : pools) {
            ArrayList<Genome> genomes = new ArrayList<>();
            genomes.addAll(p.getPopulation());
            list.add(genomes);
        }
        return list;
    }

    @Override
    public void newGeneration() {

        for (Pool p : pools) {

        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
