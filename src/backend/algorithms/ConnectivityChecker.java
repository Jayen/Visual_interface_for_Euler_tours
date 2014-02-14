package backend.algorithms;

import backend.internalgraph.Graph;
import backend.internalgraph.LocationFixedSparseGraph;
import backend.internalgraph.Node;

import java.util.*;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class ConnectivityChecker {

    private Graph graph;
    private HashMap<Node,Node> marked;

    public ConnectivityChecker(Graph graph) {
        this.graph = graph;
        marked = new HashMap<Node,Node>(graph.getNodesCount());
    }

    /**
     * Check if all the nodes in the graph
     * are reachable i.e connected from the sourceNode
     *
     * @param sourceNode -any vertex in the graph
     *                      will work if the graph is connected
     * @return true if graph is connected else false
     */
    public boolean depthFirstSearch(Node sourceNode) {
        marked.put(sourceNode,sourceNode);
        Iterator iterator = graph.getConnectedNodes(sourceNode).iterator();
        Node node;
        while(iterator.hasNext()) {
            node = (Node) iterator.next();
            if(marked.get(node)==null) {
                depthFirstSearch(node);
            }
        }
        if(marked.size()==graph.getNodesCount()) {
            return true;
        }
        return false;
    }
}
