/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evolution.NEAT.HyperNEAT;

import java.util.*;
import lt.lb.commons.Misc.*;

/**
 *
 * @author Lemmin
 */
public class SubstrateNeuronLayer extends SubstrateLayer implements DimentionInfo {

    public SubstrateNeuronLayer() {

    }

    public SubstrateNeuronLayer(String id) {
        this.ID = id;
    }

    public Interval[] layerMinMax;
    public List<HyperNeuron> neurons = new ArrayList<>();

    public HyperNeuron getClosestNeuronByPosisition(Pos p) {
        if (this.neurons.isEmpty()) {
            return null;
        }

        Double distance = Double.MAX_VALUE;
        HyperNeuron neuron = null;

        for (HyperNeuron n : this.neurons) {
            if (neuron == null) {
                neuron = n;
                continue;
            }
            Double dis = n.position.manhattanDistance(p);
            if (distance > dis) {
                distance = dis;
                neuron = n;
            }
        }
        return neuron;
    }

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

    @Override
    public Interval[] getDimentions() {
        return this.layerMinMax;
    }

}
