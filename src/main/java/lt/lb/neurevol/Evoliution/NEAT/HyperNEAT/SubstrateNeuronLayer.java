/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.NEAT.HyperNEAT;

import java.util.*;
import lt.lb.neurevol.Misc.Pair;

/**
 *
 * @author Lemmin
 */
public class SubstrateNeuronLayer extends SubstrateLayer {

    public List<HyperNeuron> neurons = new ArrayList<>();

    public void resolveConnections(List<Pair<HyperNeuron>> list, Set<String> visited) {
        if (visited.contains(this.ID)) {
            return;
        } else {
            visited.add(this.ID);
        }

        for (SubstrateLayer l : this.connectedFrom.values()) {
            if (l instanceof SubstrateNeuronLayer) {
                SubstrateNeuronLayer layer = (SubstrateNeuronLayer) l;
                for (HyperNeuron mine : this.neurons) {
                    for (HyperNeuron from : layer.neurons) {
                        Pair<HyperNeuron> pair = new Pair<>(from, mine);
                        list.add(pair);
                    }

                }
                layer.resolveConnections(list, visited);

            }

        }

    }

}
