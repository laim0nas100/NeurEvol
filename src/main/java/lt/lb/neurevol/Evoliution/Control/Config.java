/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.Control;

import java.util.Map;
import java.util.concurrent.Executor;
import lt.lb.neurevol.Evoliution.NEAT.Pool;
import lt.lb.neurevol.Evoliution.NEAT.Species;
import lt.lb.neurevol.Evoliution.NEAT.interfaces.*;

/**
 *
 * @author Laimonas-Beniusis-PC
 */
public interface Config {

    public Map<String, Double> getMap();

    public Pool getPool();

    public GenomeMaker getGenomeMaker();

    public GenomeBreeder getGenomeBreeder();

    public GenomeMutator getGenomeMutator();

    public GenomeSorter getGenomeSorter();

    public GenomeSimilarityEvaluator getGenomeSimilarityEvaluater();

    public Species newSpecies();

    public Executor getSequentialExecutor();

    public Executor getExecutor();

}
