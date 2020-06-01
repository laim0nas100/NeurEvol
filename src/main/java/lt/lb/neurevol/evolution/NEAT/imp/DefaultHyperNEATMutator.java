package lt.lb.neurevol.evolution.NEAT.imp;

import java.util.Map;
import lt.lb.commons.ArrayBasedCounter;
import lt.lb.commons.containers.tuples.Tuple;
import lt.lb.commons.misc.rng.RandomDistribution;
import lt.lb.neurevol.evolution.Control.Func;
import lt.lb.neurevol.evolution.NEAT.Genome;
import lt.lb.neurevol.neural.NeuronInfo;
import lt.lb.neurevol.evolution.NEAT.interfaces.AgentMutator;
import lt.lb.neurevol.evolution.NEAT.interfaces.Fitness;
import lt.lb.neurevol.neural.ActivationFunction;

/**
 *
 * @author laim0nas100
 */
public class DefaultHyperNEATMutator implements AgentMutator<Genome> {

    
    
    public RandomDistribution rnd;
    public double MUTATE_ACTIVE_FUNCTION = 0.3;
    public AgentMutator neatMutator;
    public Tuple<Map<Integer, ActivationFunction>, ActivationFunction> functions = new Tuple<>(Genome.activationMap, Func::sigmoid);


    public DefaultHyperNEATMutator(RandomDistribution r){
        rnd = r;
        neatMutator = new DefaultNEATMutator(r);
    }
    
    @Override
    public void mutate(Genome genome) {

        if (rnd.nextDouble() < MUTATE_ACTIVE_FUNCTION) {
            int index = rnd.nextInt(0,genome.bias.size());
            NeuronInfo get = genome.bias.get(index);
            get.afType = rnd.pickRandom(functions.getG1().keySet());
            genome.needUpdate = true;
        }
        neatMutator.mutate(genome);

    }

    @Override
    public void setInnovation(ArrayBasedCounter counter) {
        this.neatMutator.setInnovation(counter);
    }

    @Override
    public ArrayBasedCounter getInnovation() {
        return this.neatMutator.getInnovation();
    }

}
