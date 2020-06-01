package lt.lb.neurevol.evolution.Control;

import java.util.concurrent.Executor;
import lt.lb.neurevol.evolution.NEAT.Agent;
import lt.lb.neurevol.evolution.NEAT.Species;
import lt.lb.neurevol.evolution.NEAT.interfaces.*;

/**
 *
 * @author laim0nas100
 */
public interface NEATConfig<T extends Agent> {

    public Pool<T> getPool();

    public AgentBreeder<T> getBreeder();

    public AgentMutator<T> getMutator();

    public AgentSorter<T> getSorter();

    public AgentSimilarityEvaluator<T> getSimilarityEvaluator();

    public Species<T> newSpecies();

    public Executor getExecutor();

}
