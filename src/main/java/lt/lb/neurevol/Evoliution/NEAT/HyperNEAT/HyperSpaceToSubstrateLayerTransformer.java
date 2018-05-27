/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.HyperNEAT;

/**
 *
 * @author Lemmin
 */
public interface HyperSpaceToSubstrateLayerTransformer {

    public SubstrateNeuronLayer produce(HyperSpace space);

}
