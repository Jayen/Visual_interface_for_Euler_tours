package backend.algorithms.eulerisationAlgorithm;

import backend.internalgraph.Graph;

/**
 * User: jayen
 * Date: 31/03/14, Time: 13:08
 */
public class Config implements Configruation {

    public Config(Config currentConfig) {

    }

    @Override
    public double generateNeighbouringConfig() {
        return 0;
    }

    @Override
    public void undoLastGeneration() {

    }

    @Override
    public double getCost() {
        return 0;
    }

    @Override
    public Graph getGraph() {
        return null;
    }
}
