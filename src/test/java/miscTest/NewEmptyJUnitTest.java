/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscTest;

import java.util.Collection;
import lt.lb.commons.Log;
import lt.lb.neurevol.Evoliution.Coevolution.CompleteRelative;
import lt.lb.neurevol.Evoliution.Coevolution.PairingInfo;
import lt.lb.neurevol.Misc.Pair;
import org.junit.*;

/**
 *
 * @author Laimonas-Beniusis-PC
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
    @Test
    public void testPairing() throws InterruptedException {
        CompleteRelative rel = new CompleteRelative();
        Collection<Pair<PairingInfo>> pairs = rel.producePairs(10, 20);
        Log.print(pairs.size());
        for (Pair<PairingInfo> p : pairs) {
            Log.print(p);
        }
        Log.close();
    }
}
