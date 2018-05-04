package lt.lb.neurevol.Evoliution.Control;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.concurrent.Executor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Laimonas-Beniusis-PC
 */
public abstract class GenomePool {

    public abstract Executor getExecutor();

    public abstract ControllerPool getControllerPool();

    public void evolve() {
        Collection<Runnable> runnables = new ArrayDeque<>();
        for (GameController controller : this.getControllerPool().getControllers()) {
            runnables.add(controller.makeRunnable());
        }
        Executor executor = this.getExecutor();
        for (Runnable run : runnables) {
            executor.execute(run);
        }
    }

}
