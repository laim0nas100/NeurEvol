/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.imp;

import LibraryLB.ArrayBasedCounter;
import lt.lb.neurevol.Evoliution.NEAT.Genome;
import lt.lb.neurevol.Evoliution.NEAT.interfaces.GenomeMutator;
import lt.lb.neurevol.Misc.F;
import lt.lb.neurevol.Neural.NeuronInfo;

/**
 *
 * @author Lemmin
 */
public class DefaultHyperNEATMutator implements GenomeMutator {

    public double MUTATE_ACTIVE_FUNCTION = 0.3;
    private GenomeMutator neatMutator = new DefaultNEATMutator();

    @Override
    public void mutate(Genome genome) {

        if (F.RND.nextDouble() < MUTATE_ACTIVE_FUNCTION) {
            int index = F.RND.nextInt(genome.bias.size());
            NeuronInfo get = genome.bias.get(index);
            get.afType = F.RND.nextInt(Genome.activationMap.size());
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
