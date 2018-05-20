/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT;

import java.util.*;
import lt.lb.neurevol.Evoliution.NEAT.interfaces.Pool;

/**
 *
 * @author Laimonas-Beniusis-PC
 */
public class MultiPool implements Pool {

    public int generation = 0;
    public ArrayList<Pool> pools = new ArrayList<>();

    public MultiPool(Pool... pools) {
        for (Pool p : pools) {
            this.pools.add(p);
        }
    }

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
            p.newGeneration();
        }
        this.generation++;
    }

    @Override
    public int getGeneration() {
        return this.generation;
    }

    @Override
    public void setGeneration(int generation) {
        this.generation = generation;
    }

}
