package backend.algorithms.eulerisationAlgorithm;

import backend.internalgraph.Graph;

import java.util.ArrayList;

/**
 * Interface that defines what Configruation is
 * and what methods it should support
 * @author Jayen kumar Jaentilal k1189304
 */
public interface Configuration {

    /**
     * Method to generate a neighbouring
     * configuration from the current config
     * @return the cost of the neighbouring configuration
     */
    public double generateNeighbouringConfig();

    /**
     * Undo the last configuration
     * that was generated
     */
    public void undoLastGeneration();

    /**
     * Get the cost of the configuration
     * @return double -the cost of the configuration
     */
    public double getCost();

    /**
     * Get the graph that represents
     * this configuration
     * @return graph representing this configuration
     */
    public Graph getGraphWithNewEdges();

    public ArrayList getConfig();
}
