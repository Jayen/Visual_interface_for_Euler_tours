package backend.algortihms;

import backend.internalgraph.LocationFixedSparseGraph;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class ConnectivityChecker {

    private LocationFixedSparseGraph graph;

    public ConnectivityChecker(LocationFixedSparseGraph graph) {
        this.graph = graph;
    }

    /**
     * Check if all the nodes in the graph
     * are reachable i.e connected
     * @return true if graph is connected else false
     */
    public boolean isConnected() {
        //TODO DFS for checking connectivity
        return true;
    }
}
