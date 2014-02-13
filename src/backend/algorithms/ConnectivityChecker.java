package backend.algorithms;

import backend.internalgraph.LocationFixedSparseGraph;

import java.util.*;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class ConnectivityChecker<V,E> {

    private LocationFixedSparseGraph graph;
    private Map<V,V> marked;

    public ConnectivityChecker(LocationFixedSparseGraph graph) {
        this.graph = graph;
        marked = new HashMap<V, V>(graph.getVertexCount());
    }

    /**
     * Check if all the nodes in the graph
     * are reachable i.e connected from the sourceVertex
     * @param sourceVertex -any vertex in the graph
     *                      will work if the graph is connected
     * @return true if graph is connected else false
     */
    public boolean depthFirstSearch(V sourceVertex) {
        marked.put(sourceVertex, sourceVertex);
        Collection connectedVertices = graph.getNeighbors(sourceVertex);
        Iterator iterator = connectedVertices.iterator();
        V vertex;
        while(iterator.hasNext()) {
            vertex = (V) iterator.next();
            if(marked.get(vertex)==null) {
                depthFirstSearch(vertex);
            }
        }
        if(marked.size()==graph.getVertexCount()) {
            return true;
        }
        return false;
    }
}
