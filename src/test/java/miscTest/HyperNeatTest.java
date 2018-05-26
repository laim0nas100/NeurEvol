/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscTest;

import lt.lb.commons.Tracer;
import lt.lb.neurevol.Evoliution.NEAT.Genome;
import lt.lb.neurevol.Evoliution.NEAT.HyperNEAT.SubstrateLayer.SLayerType;
import lt.lb.neurevol.Evoliution.NEAT.HyperNEAT.*;
import lt.lb.neurevol.Evoliution.NEAT.HyperNEAT.imp.HyperSpaceToSubstrateLayerTransformerImpl;
import lt.lb.neurevol.Evoliution.NEAT.HyperNEAT.imp.SubstrateToNNInfoProducerImpl;
import lt.lb.neurevol.Evoliution.NEAT.imp.DefaultNEATMutator;
import lt.lb.neurevol.Neural.*;
import org.junit.*;

/**
 *
 * @author Lemmin
 */
public class HyperNeatTest {

    public HyperNeatTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void testSubstrate() throws Exception {

        Tracer t = Tracer.get("");

        HyperNEATSpace sp1 = new HyperNEATSpace(2, 1, 1);

        HyperNEATSpace hidden = new HyperNEATSpace(2, 2, 1);

        HyperNEATSpace outputSpace = new HyperNEATSpace(1, 3, 1);
//        t.dump(sp1);
//        t.dump(hidden);
//        t.dump(outputSpace);

        HyperSpaceToSubstrateLayerTransformerImpl tr = new HyperSpaceToSubstrateLayerTransformerImpl();

        SubstrateNeuronLayer nl1 = tr.produce(sp1);
        nl1.type = SLayerType.INPUT;
        SubstrateNeuronLayer nl2 = tr.produce(hidden);
        nl2.type = SLayerType.HIDDEN;
        SubstrateNeuronLayer nl3 = tr.produce(outputSpace);
        nl3.type = SLayerType.OUTPUT;
        nl1.connectTo(nl2);
        nl2.connectTo(nl3);

        Genome g = new Genome(4, 3);
        DefaultNEATMutator mutator = new DefaultNEATMutator();
        mutator.mutate(g);
        mutator.mutate(g);
        mutator.mutate(g);
        mutator.mutate(g);
        NeuralNetwork net = g.generateNetwork();

        Substrate subs = new Substrate(nl1, nl2, nl3);
        subs.makeGlobalIDs();

        SubstrateToNNInfoProducerImpl prod = new SubstrateToNNInfoProducerImpl();
        ConnectionProducer conProd = (NeuronInfo in, NeuronInfo to, Double[] weights) -> {
            HyperNeuron hIn = (HyperNeuron) in;
            HyperNeuron hTo = (HyperNeuron) to;

            double w = 0;
            int use = 0;
            final String n1 = nl1.ID;
            final String n2 = nl2.ID;
            final String n3 = nl3.ID;
            String inID = hIn.substrateLayerID;

            if (inID.equals(n1)) {
                use = 0;
            } else if (inID.endsWith(n2)) {
                use = 1;
            } else {
                use = 2;
            }
            w = weights[use];

//            if (Math.abs(w) > 0.2) {
            return new Synapse(hIn.id, hTo.id, w);
//            }
//            return null;

        };
        NNInfo produce = prod.produce(subs, net, conProd);

        t.dump(produce.links);
//        t.dump(subs.produceLinks());
        Thread.sleep(5000);

    }
}
