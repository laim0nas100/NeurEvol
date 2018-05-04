package lt.lb.neurevol.Evoliution.Control;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Laimonas-Beniusis-PC
 */
public interface GameController {

    public Game getGame();

    public Genome getGenome();

    public void advanceGame();

    public GameView extractView();

    public void makeMove(GameMove move);

    public boolean isEvaluable();

    public default Runnable makeRunnable() {
        return () -> {
            while (!this.isEvaluable()) {
                GameView extractView = this.extractView();
                GameMove move = this.getGenome().evaluateView(extractView);
                this.makeMove(move);

            }
        };
    }

}
