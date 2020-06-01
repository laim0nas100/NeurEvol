package lt.lb.neurevol.evolution.NEAT.interfaces;

import java.util.Collection;
import java.util.List;
import lt.lb.neurevol.evolution.NEAT.Agent;

/**
 *
 * @author laim0nas100
 */
public interface AgentBreeder<T extends Agent> {

    public List<T> breedChild(List<T> agents);
    
    public Collection<T> initializeGeneration();

}
