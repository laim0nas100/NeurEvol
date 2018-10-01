/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.Control;

import java.util.concurrent.Executor;
import lt.lb.neurevol.evolution.NEAT.Species;
import lt.lb.neurevol.evolution.NEAT.interfaces.*;

/**
 *
 * @author laim0nas100
 */
public interface NEATConfig {

    public Pool getPool();

    public AgentMaker getMaker();

    public AgentBreeder getBreeder();

    public AgentMutator getMutator();

    public AgentSorter getSorter();

    public AgentSimilarityEvaluator getSimilarityEvaluator();

    public Species newSpecies();

    public Executor getSequentialExecutor();

    public Executor getExecutor();

}
