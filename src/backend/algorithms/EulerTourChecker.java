package backend.algorithms;

import backend.internalgraph.LocationFixedSparseGraph;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * Jayen kumar Jaentilal k1189304
 */
public class EulerTourChecker {

    public static boolean EulerTourCheck(LocationFixedSparseGraph graph) {
        ConnectivityChecker<String,String> connectivityChecker = new ConnectivityChecker(graph);
        if(connectivityChecker.depthFirstSearch((String) graph.getVertices().iterator().next()) && hasNoOddDegreeVetices(graph)) {
            return true;
        }
        return false;
    }

    /**
     * Check if the graph has any
     * vertices with odd degrees
     * @param graph -LocationFixedSparseGraph
     * @return boolean -false if there is a vertex with odd degree else true
     */
    private static boolean hasNoOddDegreeVetices(LocationFixedSparseGraph graph) {
        Collection vertices = graph.getVertices();
        Iterator vertexIterator = vertices.iterator();
        while(vertexIterator.hasNext()) {
            if(graph.getNeighborCount(vertexIterator.next())%2!=0){
                System.out.println("found odd degree");
                return false;
            }
        }
        return true;
    }
}
