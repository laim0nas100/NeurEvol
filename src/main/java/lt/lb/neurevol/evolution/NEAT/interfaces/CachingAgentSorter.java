package lt.lb.neurevol.evolution.NEAT.interfaces;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import lt.lb.neurevol.evolution.NEAT.Agent;

/**
 *
 * @author laim0nas100
 * @param <T>
 */
public abstract class CachingAgentSorter<T extends Agent, E extends Fitness> implements AgentSorter<T> {

    public int generation;
    public HashMap<Integer, GenerationalInfo<E>> generations = new HashMap<>();

    public static class GenerationalInfo<E> {

        public final int generation;
        public ConcurrentHashMap<String, E> fitness = new ConcurrentHashMap<>();
        public ConcurrentHashMap<String, Integer> rank = new ConcurrentHashMap<>();

        public GenerationalInfo(int gen) {
            this.generation = gen;
        }
    }

    @Override
    public E evaluateFitness(T agent) {
        return getCurrent().fitness.computeIfAbsent(agent.id, id -> computeFitenss(agent));
    }

    public void startGeneration(int gen) {
        this.generation = gen;
        generations.put(gen, new GenerationalInfo<>(gen));
    }

    public abstract E computeFitenss(T agent);

    @Override
    public void setGlobalRank(int generation, T agent, int rank) {
        lazyGet(generation).rank.put(agent.id, rank);
    }

    @Override
    public int globalRank(T agent) {
        return getCurrent().rank.get(agent.id);
    }

    protected GenerationalInfo<E> getCurrent() {
        return lazyGet(generation);
    }

    protected GenerationalInfo<E> lazyGet(int gen) {
        return generations.computeIfAbsent(gen, id -> new GenerationalInfo<>(gen));
    }

}
