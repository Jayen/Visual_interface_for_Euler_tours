package backend.algorithms.eulerisationAlgorithm;

import backend.internalgraph.Graph;

/**
 * User: jayen
 * Date: 31/03/14, Time: 13:03
 */
public interface Configruation {

    public double generateNeighbouringConfig();

    public void undoLastGeneration();

    public double getCost();

    public Graph getGraph();
}
