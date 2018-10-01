/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscTest;

import java.util.*;
import lt.lb.commons.Log;
import lt.lb.commons.containers.Pair;
import lt.lb.neurevol.evolution.Coevolution.CompleteRelative;
import lt.lb.neurevol.evolution.Coevolution.PairingInfo;
import lt.lb.commons.misc.*;
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

    public void finalStat() throws InterruptedException {
        int size = 10000;

        Double[] arr = new Double[size];
        this.initArray(arr, () -> 0d);

        long time = System.currentTimeMillis();

        LinkedList l;
        int times = 10;
        for (int i = 0; i < times; i++) {
            Double[] statShuffle = this.statShuffle(size);
            for (int j = 0; j < size; j++) {
                arr[j] += statShuffle[j];
            }
            Log.print("done " + i);
        }

        time = System.currentTimeMillis() - time;

        for (int i = 0; i < size; i++) {
            arr[i] /= times;
        }

        Log.print(Arrays.asList(arr));
        Log.print(time);

        Thread.sleep(1000);

    }

    private interface make<T> {

        public T makeMe();
    }

    public <T> void initArray(T[] arr, make<T> m) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = m.makeMe();
        }
    }

    public Double[] statShuffle(int size) throws InterruptedException {
        Integer[] arr = new Integer[size];
        initArray(arr, () -> 0);
        for (int i = 0; i < 0; i++) {
            arr[i] = 0;
        }
        for (int tr = 0; tr < 100; tr++) {
            List<Integer> testShuffle = testShuffle(size);

            for (int i = 0; i < size; i++) {
                arr[i] += testShuffle.get(i);
            }
        }

        double avg = 0;
        for (int i = 0; i < 10; i++) {
            avg += arr[i];
        }
        avg /= size;

        Double[] avgAr = new Double[size];
        initArray(avgAr, () -> 0d);

        for (int i = 0; i < size; i++) {
            avgAr[i] = arr[i] / avg;
        }

//        Log.print(avg);
//        Log.print(Arrays.asList(arr));
//        Log.print(Arrays.asList(avgAr));
        return avgAr;
    }

    public List<Integer> testShuffle(int size) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }

//        Log.print(list);
        F.RND.seededShuffle(list, F.RND.RND);
//        Log.print(list);
        return list;

    }

    @Test
    public void posTest() throws Exception {
        Interval in = new Interval(0, 1);
        Interval[] inn = new Interval[]{new Interval(0, 800), new Interval(0, 600)};
        Pos pos = new Pos(400, 20);
        Log.print(Arrays.asList(pos.normalized(inn, -1, 1)));

        Thread.sleep(500);
    }

}
