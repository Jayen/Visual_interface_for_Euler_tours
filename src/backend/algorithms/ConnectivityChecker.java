package backend.algorithms;

import backend.internalgraph.LocationFixedSparseGraph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class ConnectivityChecker<V> {

    private LocationFixedSparseGraph graph;

    public ConnectivityChecker(LocationFixedSparseGraph graph) {
        this.graph = graph;
    }

    /**
     * Check if all the nodes in the graph
     * are reachable i.e connected from the sourceVertex
     * @param sourceVertex -any vertex in the graph
     *                      will work if the graph is connected
     * @return true if graph is connected else false
     */
    public boolean depthFirstSearch(V sourceVertex) {
        Map<V,V> marked = new HashMap<V, V>(graph.getVertexCount());//TODO what if the vertex names are the same?
        marked.put(sourceVertex,sourceVertex);
        Collection connectedVertex = graph.getNeighbors(sourceVertex);
        Iterator iterator = connectedVertex.iterator();
        V vertex;
        while(iterator.hasNext()) {
            vertex = (V) iterator.next();
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
