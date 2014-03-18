package backend.algorithms;

import backend.internalgraph.Graph;
import backend.internalgraph.Node;

import java.util.List;

/**
 * Abstract class that sets up the
 * foundation for the euler tour algorithms
 * Jayen kumar Jaentilal k1189304
 */
public abstract class EulerTourAlgorithm {

    protected Graph graph;
    protected List<Node> nodePathList;
    protected ConnectivityChecker connectivityChecker;

    /**
     * Get the euler tour for the current graph
     * @return List<Node> the list of nodes
     * that form a euler tour
     */
    public abstract List getEulerTour(Graph graphToCheck);

}
