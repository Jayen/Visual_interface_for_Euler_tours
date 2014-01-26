package backend.algorithms;

import backend.internalgraph.LocationFixedSparseGraph;

import java.util.*;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class ConnectivityChecker<V> {

    private LocationFixedSparseGraph graph;
    private Map<V,V> marked;//TODO what if the vertex names are the same?

    public ConnectivityChecker(LocationFixedSparseGraph graph) {
        this.graph = graph;
        marked = new HashMap<V,V>(graph.getVertexCount());
    }

    /**
     * Check if all the nodes in the graph
     * are reachable i.e connected from the sourceVertex
     * @param sourceVertex -any vertex in the graph
     *                      will work if the graph is connected
     * @return true if graph is connected else false
     */
    public boolean depthFirstSearch(V sourceVertex) {
        //TODO DFS for checking connectivity
        marked.put(sourceVertex,sourceVertex);
        Collection connectedVertex = graph.getNeighbors(sourceVertex);
        Iterator interator = connectedVertex.iterator();
        V vertex;
        while(interator.hasNext()) {
            vertex = (V) interator.next();
            if(!marked.containsValue(vertex)) {
                depthFirstSearch(vertex);
            }
        }

        if(marked.size()==graph.getVertexCount()) {
            return true;
        }
        return false;
    }
}
