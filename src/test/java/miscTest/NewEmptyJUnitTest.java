/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscTest;

import java.util.*;
import lt.lb.commons.Log;
import lt.lb.commons.containers.tuples.Pair;
import lt.lb.neurevol.evolution.Coevolution.CompleteRelative;
import lt.lb.neurevol.evolution.Coevolution.PairingInfo;
import lt.lb.commons.misc.*;
import org.junit.*;

/**
 *
 * @author laim0nas100
 */
public class NewEmptyJUnitTest {

    public NewEmptyJUnitTest() {
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
//    @Test
    public void testPairing() throws InterruptedException {
        CompleteRelative rel = new CompleteRelative();
        Collection<Pair<PairingInfo>> pairs = rel.producePairs(10, 20);
        Log.print(pairs.size());
        for (Pair<PairingInfo> p : pairs) {
            Log.print(p);
        }
//        Log.close();
    }

    @Test
    public void posTest() throws Exception {
        Interval in = new Interval(0d, 1d);
        Interval[] inn = new Interval[]{new Interval(0d, 800d), new Interval(0d, 600d)};
        Pos pos = new Pos(400, 20);
        Log.print(Arrays.asList(pos.normalized(inn, -1, 1)));

        Thread.sleep(500);
    }

}
