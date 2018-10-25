/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.evolution.NEAT.HyperNEAT;

import java.util.*;
import lt.lb.commons.containers.tuples.Pair;

/**
 *
 * @author laim0nas100
 */
public class Substrate {

    public Map<String, SubstrateLayer> layers = new HashMap<>();

    public Substrate(SubstrateLayer... lrs) {
        for (SubstrateLayer l : lrs) {
            layers.put(l.ID, l);
        }
    }

    public List<SubstrateLayer> getTypeLayers(SubstrateLayer.SLayerType type) {
        List<SubstrateLayer> list = new ArrayList<>();
        for (SubstrateLayer layer : layers.values()) {
            if (type == layer.type) {
                list.add(layer);
            }
        }
        return list;
    }

    public void makeGlobalIDs() {
        int id = 0;
        List<SubstrateLayer> idLayers = this.getTypeLayers(SubstrateLayer.SLayerType.INPUT);
        idLayers.addAll(this.getTypeLayers(SubstrateLayer.SLayerType.OUTPUT));
        idLayers.addAll(this.getTypeLayers(SubstrateLayer.SLayerType.HIDDEN));
        for (SubstrateLayer layer : idLayers) {
            if (layer instanceof SubstrateNeuronLayer) {
                SubstrateNeuronLayer l = (SubstrateNeuronLayer) layer;
                if (l.neurons.isEmpty()) {
                    throw new RuntimeException("Layer " + l.ID + " is empty");
                }
                for (HyperNeuron n : l.neurons) {
                    n.id = id++;
                    n.substrateLayer = l;
                }
            } else {
                throw new RuntimeException("Not supported");
            }
        }

    }

    public Map<Integer, HyperNeuron> getNeuronMap() {
        Map<Integer, HyperNeuron> map = new HashMap<>();
        for (SubstrateLayer layer : this.layers.values()) {
            if (layer instanceof SubstrateNeuronLayer) {
                SubstrateNeuronLayer l = (SubstrateNeuronLayer) layer;
                for (HyperNeuron n : l.neurons) {
                    map.put(n.id, n);
                }
            }
        }
        return map;

    }

    public Map<Integer, HyperNeuron> produceInputMap() {
        Map<Integer, HyperNeuron> map = new HashMap<>();
        List<SubstrateLayer> typeLayers = this.getTypeLayers(SubstrateLayer.SLayerType.INPUT);
        for (SubstrateLayer layer : typeLayers) {
            if (layer instanceof SubstrateNeuronLayer) {
                SubstrateNeuronLayer l = (SubstrateNeuronLayer) layer;
                for (HyperNeuron n : l.neurons) {
                    map.put(n.id, n);
                }
            }
        }

        return map;

    }

    public List<Pair<HyperNeuron>> produceLinks() {
        List<Pair<HyperNeuron>> list = new ArrayList<>();

        List<SubstrateLayer> outputLayers = this.getTypeLayers(SubstrateLayer.SLayerType.OUTPUT);

        Set<String> visited = new HashSet<>();

        for (SubstrateLayer layer : outputLayers) {
            if (layer instanceof SubstrateNeuronLayer) {
                ((SubstrateNeuronLayer) layer).resolveConnections(list, visited);
            }
        }

        return list;

    }

}
