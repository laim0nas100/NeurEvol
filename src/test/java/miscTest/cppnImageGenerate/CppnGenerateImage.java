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
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import lt.lb.commons.ArrayOp;
import lt.lb.commons.Log;
import lt.lb.commons.misc.F;
import lt.lb.commons.misc.Pos;
import lt.lb.neurevol.evolution.NEAT.Genome;
import lt.lb.neurevol.neural.NNInfo;
import lt.lb.neurevol.neural.NeuralNetwork;
import lt.lb.neurevol.neural.NeuronInfo;
import lt.lb.neurevol.neural.Synapse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author labe2219
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
            int nRgb = new Color(rgb,rgb,rgb).getRGB();
            int w1 = pixel.pos.get()[0].intValue();
            int h1 = pixel.pos.get()[1].intValue();
            
//            Log.print(w1,h1,rgb,nRgb);
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

                Double[] evaluate = net.evaluate(ArrayOp.asArray(x, y));

                LPixel pixel = new LPixel();
                pixel.pos = new Pos(i, j);
                pixel.intensity = evaluate[0];
                list.add(pixel);
            }
        }

        return list;
    }
    @Test
    public void makeImage() throws IOException, InterruptedException {
        String url = "image.png";

        NNInfo info = new NNInfo();
        info.inputs = 2;
        info.outputs = 1;
        info.defaultActivation = F::sigmoid;

        ArrayList<NeuronInfo> infos = new ArrayList<>();

        // 2 input 1 output 1 hidden
        for (int i = 0; i < 3; i++) {
            F.unsafeRun(() -> {
                NeuronInfo ni = new NeuronInfo();
                ni.afType = 2;
                ni.bias = 0.05d;
                infos.add(ni);
            });
        }
        NeuronInfo ni = new NeuronInfo();
        ni.afType = 0;
        ni.bias = 0d;
        infos.add(ni);
        
        // 0 1, 2, 3
        
        //links 
        ArrayList<Synapse> synapses = new ArrayList<>();
        synapses.add(new Synapse(0,3,0.5));
        synapses.add(new Synapse(1,3,0.258));
        synapses.add(new Synapse(3,2,0.1));
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
