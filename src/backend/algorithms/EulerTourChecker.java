package backend.algorithms;

import backend.internalgraph.Graph;
import backend.internalgraph.Node;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * Jayen kumar Jaentilal k1189304
 */
public class EulerTourChecker {

    public static boolean hasEulerTour(Graph graph) {
        ConnectivityChecker connectivityChecker = new ConnectivityChecker(graph);
        if(connectivityChecker.depthFirstSearch(graph.getNodes().iterator().next()) && hasNoOddDegreeVetices(graph)) {
            return true;
        }
        return false;
    }

    /**
     * Check if the graph has any
     * vertices with odd degrees
     * @param graph -Graph
     * @return boolean -false if there is a vertex with odd degree else true
     */
    private static boolean hasNoOddDegreeVetices(Graph graph) {
        Collection nodes = graph.getNodes();
        Iterator nodeIterator = nodes.iterator();
        Node node;
        while(nodeIterator.hasNext()) {
            node = (Node) nodeIterator.next();
            if(graph.degree(node)%2!=0) {
                System.out.println("found odd degree "+node);
                return false;
            }
        }
        return true;
    }
}
