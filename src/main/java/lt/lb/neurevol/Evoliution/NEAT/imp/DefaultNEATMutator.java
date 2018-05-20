/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.imp;

import java.util.*;
import lt.lb.commons.ArrayBasedCounter;
import lt.lb.neurevol.Evoliution.NEAT.Gene;
import lt.lb.neurevol.Evoliution.NEAT.Genome;
import lt.lb.neurevol.Evoliution.NEAT.interfaces.GenomeMutator;
import lt.lb.neurevol.Misc.F;
import lt.lb.neurevol.Neural.*;

/**
 *
 * @author Lemmin
 */
public class DefaultNEATMutator implements GenomeMutator {

    public ArrayBasedCounter innovation;

    public double MUT_WEIGHT = 1;
    public double MUT_WEIGHT_STEP = 0.2;
    public double MUT_WEIGHT_RESET = 0.1;

    public double MUT_ENABLE_TOGGLE = 0.1;

    public double MUT_BIAS = 0.8;
    public double MUT_BIAS_STEP = 0.2;
    public double MUT_BIAS_RESET = 0.1;

    public double MUT_ENALBE = 0.6;
    public double MUT_DISABLE = 0.4;

    public double MUT_LINK = 2;
    public double MUT_NODE = 0.6;
    public double weightCap = 20;

    public DefaultNEATMutator() {
        this.innovation = new ArrayBasedCounter(1);
    }

    private void mutateNode(Genome genome) {
        genome.getNetwork();
        Gene gene = F.pickRandom(genome.genes);
        gene.en = false;
        int neuronID = genome.bias.size();
        genome.bias.add(new NeuronInfo());

        Gene inputGene = new Gene(gene.in, neuronID);
        inputGene.inn = innovation.inc();
        inputGene.w = gene.w;

        Gene outputGene = new Gene(neuronID, gene.out);
        outputGene.inn = innovation.inc();
        outputGene.w = 1f;

        genome.genes.add(inputGene);
        genome.genes.add(outputGene);

    }

    private void mutateLink(Genome genome) {

        NeuralNetwork network = genome.getNetwork();
        ArrayList<Integer> full = new ArrayList<>();
        ArrayList<Integer> candidatesInput = new ArrayList<>();
        HashSet<Integer> outputNodes = new HashSet<>();
        for (Neuron n : network.getOutputs()) {
            outputNodes.add(n.ID);
        }
        for (Neuron n : network.neurons.values()) {
            full.add(n.ID);
            if (!outputNodes.contains(n.ID)) {
                candidatesInput.add(n.ID);
            }
        }
        Neuron input = network.neurons.get(candidatesInput.get(F.RND.nextInt(candidatesInput.size())));

        HashSet<Integer> parentSet = network.getParentSet(input.ID);
        parentSet.add(input.ID);
        for (Gene g : genome.genes) {
            if (g.in == input.ID) {
                parentSet.add(g.out);
            }
        }
        full.removeAll(parentSet);

        if (!full.isEmpty()) {
            Neuron output = network.neurons.get(full.get(F.RND.nextInt(full.size())));
            Gene gene = new Gene(input.ID, output.ID);
            gene.inn = innovation.inc();
            genome.genes.add(gene);
        }
    }

    private void mutateEnableDisable(Genome genome, final boolean enable) {
        genome.getNetwork();
        List<Gene> candidates = new ArrayList<>();
        for (Gene gene : genome.genes) {
            if (gene.en != enable) {
                candidates.add(gene);
            }
        }
        if (candidates.isEmpty()) {
            return;
        }

        final Gene gene = candidates.get(F.RND.nextInt(candidates.size()));
        gene.en = !gene.en;
    }

    @Override
    public void mutate(Genome genome) {

        double prob = this.MUT_LINK;
        while (F.RND.nextDouble() < prob || genome.genes.isEmpty()) {
//            Log.print("Mutate Link go");
            mutateLink(genome);
            genome.needUpdate = true;
//                genome.generateNetwork();
            prob -= 1;
//            Log.print("Mutate Link end");

        }
        if (MUT_NODE > F.RND.nextDouble()) {
//            Log.print("Mutate Node go");
            mutateNode(genome);
            genome.needUpdate = true;
//            Log.print("Mutate Mode end");
        }
        if (MUT_WEIGHT > F.RND.nextDouble()) { //mutate weights
            genome.getNetwork();
            for (Gene g : genome.genes) {
                if (MUT_WEIGHT_RESET < F.RND.nextDouble()) {
                    g.w = (2.0 * F.RND.nextDouble() - 1.0);
                } else {
                    g.w += 2.0 * F.RND.nextDouble() * MUT_WEIGHT_STEP - MUT_WEIGHT_STEP;
                }
                g.w = Math.max(Math.min(g.w, this.weightCap), -this.weightCap);
            }
            genome.needUpdate = true;
        }
        if (MUT_ENALBE > F.RND.nextDouble()) {
//            Log.print("Mutate Enable go");
            this.mutateEnableDisable(genome, true);
//            Log.print("Mutate Enable end");
            genome.needUpdate = true;
        }
        if (MUT_DISABLE > F.RND.nextDouble()) {
//            Log.print("Mutate Disable go");
            this.mutateEnableDisable(genome, false);
//            Log.print("Mutate Disable end");
            genome.needUpdate = true;
        }

        if (MUT_BIAS > F.RND.nextDouble()) {
//            Log.print("Mutate Bias go");
            genome.getNetwork();
            int index = F.RND.nextInt(genome.bias.size());
            NeuronInfo val = genome.bias.get(index);
            if (MUT_BIAS_RESET < F.RND.nextDouble()) {
                val.bias = 2 * F.RND.nextDouble() - 1;
            } else {
                val.bias += 2 * F.RND.nextDouble() - 1;
            }
            val.bias %= this.weightCap;
//            Log.print("Mutate Bias End");
            genome.needUpdate = true;
        }
//        genome.needUpdate = true;
//        genome.generateNetwork();

    }

    @Override
    public void setInnovation(ArrayBasedCounter counter) {
        this.innovation = counter;
    }

    @Override
    public ArrayBasedCounter getInnovation() {
        return this.innovation;
    }
}
