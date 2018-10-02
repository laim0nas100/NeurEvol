/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.imp;

import lt.lb.commons.ArrayBasedCounter;
import lt.lb.commons.misc.RandomDistribution;
import lt.lb.neurevol.evolution.NEAT.Genome;
import lt.lb.neurevol.neural.NeuronInfo;
import lt.lb.neurevol.evolution.NEAT.interfaces.AgentMutator;

/**
 *
 * @author laim0nas100
 */
public class DefaultHyperNEATMutator implements AgentMutator<Genome> {

    
    
    public RandomDistribution rnd;
    public double MUTATE_ACTIVE_FUNCTION = 0.3;
    public AgentMutator neatMutator;


    public DefaultHyperNEATMutator(RandomDistribution r){
        rnd = r;
        neatMutator = new DefaultNEATMutator(r);
    }
    
    @Override
    public void mutate(Genome genome) {

        if (rnd.nextDouble() < MUTATE_ACTIVE_FUNCTION) {
            int index = rnd.nextInt(0,genome.bias.size());
            NeuronInfo get = genome.bias.get(index);
            get.afType = rnd.nextInt(0,Genome.activationMap.size());
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
