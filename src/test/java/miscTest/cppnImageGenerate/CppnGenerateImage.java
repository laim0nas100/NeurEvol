/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscTest.cppnImageGenerate;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.imageio.ImageIO;
import lt.lb.commons.ArrayOp;
import lt.lb.commons.Log;
import lt.lb.commons.F;
import lt.lb.commons.misc.Interval;
import lt.lb.commons.misc.Pos;
import lt.lb.neurevol.evolution.Control.Func;
import lt.lb.neurevol.evolution.NEAT.HyperNEAT.HGenome;
import lt.lb.neurevol.neural.NNInfo;
import lt.lb.neurevol.neural.NeuralNetwork;
import lt.lb.neurevol.neural.NeuronInfo;
import lt.lb.neurevol.neural.Synapse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author laim0nas100
 */
public class CppnGenerateImage {

    public CppnGenerateImage() {
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
    public static class LPixel {

        public Pos pos;
        public Double intensity;
    }

    public BufferedImage parseImage(List<LPixel> pixels, int h, int w) {
        final BufferedImage image = new BufferedImage(h, w, BufferedImage.TYPE_3BYTE_BGR);

        pixels.forEach(pixel -> {

            int rgb = (int) Math.round(pixel.intensity * 255);
            int nRgb = new Color(rgb, rgb, rgb).getRGB();
            int w1 = pixel.pos.get()[0].intValue();
            int h1 = pixel.pos.get()[1].intValue();

            image.setRGB(w1, h1, nRgb);
        });
        return image;
    }

    public List<LPixel> generateImage(NeuralNetwork net, int height, int weight) {

        List<LPixel> list = new ArrayList<>();
        for (int i = 0; i < weight; i++) {
            for (int j = 0; j < height; j++) {

                double x = (double) i / weight;
                double y = (double) j / height;
                double d = Math.sqrt(x * x + y * y);

                Double[] evaluate = net.evaluate(ArrayOp.asArray(x, y, d));

                LPixel pixel = new LPixel();
                pixel.pos = new Pos(i, j);
                pixel.intensity = Interval.ZERO_ONE.clamp(evaluate[0]);
                list.add(pixel);
            }
        }

        return list;
    }

    @Test
    public void makeImage() throws IOException, InterruptedException, TimeoutException {
        String url = "image.png";

        NNInfo info = new NNInfo();
        info.inputs = 3;
        info.outputs = 1;
        info.defaultActivation = Func::sigmoid;
        info.activationMap = HGenome.getDefaultActivationMap();

        ArrayList<NeuronInfo> infos = new ArrayList<>();

        // 2 input 1 output 1 hidden
        for (int i = 0; i < info.inputs; i++) {
            F.unsafeRun(() -> {
                NeuronInfo ni = new NeuronInfo();
                ni.afType = 0;
                ni.bias = 0.001d;
                infos.add(ni);
            });
        }

        for (int i = 0; i < info.outputs; i++) {
            F.unsafeRun(() -> {
                NeuronInfo ni = new NeuronInfo();
                ni.afType = 2;
                ni.bias = 0.001d;
                infos.add(ni);
            });
        }
        F.unsafeRun(() -> {
            NeuronInfo ni = new NeuronInfo();
            ni.afType = 2;
            ni.bias = 0.0d;
            infos.add(ni);
        });
        F.unsafeRun(() -> {
            NeuronInfo ni = new NeuronInfo();
            ni.afType = 4;
            ni.bias = 0.0d;
            infos.add(ni);
        });

        // 0 1, 2, 3
        // 0,1,2 - input
        // 3 - output
        // 4,5 - hidden
        //links 
        ArrayList<Synapse> synapses = new ArrayList<>();
        synapses.add(new Synapse(0, 4, 1.666));
        synapses.add(new Synapse(1, 4, -0.777));
        synapses.add(new Synapse(2, 4, -0.888));
        synapses.add(new Synapse(4, 5, 1.111));
        synapses.add(new Synapse(5, 3, 1));
        info.biases = infos;
        info.links = synapses;

        NeuralNetwork net = new NeuralNetwork(info);

        int w = 1000;
        int h = 1000;
        List<LPixel> pixelList = this.generateImage(net, w, h);
        BufferedImage image = this.parseImage(pixelList, w, h);
        ImageIO.write(image, "png", new File(url));
        

        Log.await(1, TimeUnit.DAYS);
    }
}
