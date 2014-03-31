package backend.algorithms;

import backend.internalgraph.Graph;
import backend.internalgraph.Node;

import java.util.HashMap;
import java.util.Iterator;

/**
 * This class allows you to check if
 * a graph is connected using DFS
 * Jayen kumar Jaentilal k1189304
 */
public class ConnectivityChecker {

    private Graph graph;
    private HashMap<Node,Node> marked;

    public ConnectivityChecker(Graph graph) {
        this.graph = graph;
        marked = new HashMap<Node,Node>(this.graph.getNumberOfNodes());
    }

    /**
     * Check if all the nodes in the graph
     * are reachable i.e connected from the sourceNode
     *
     * @param sourceNode -any Node in the graph
     *                    will work if the graph is connected
     * @return true if graph is connected else false
     */
    public boolean isGraphConnectedDFS(Node sourceNode) {
        marked.put(sourceNode,sourceNode);
        Iterator iterator = graph.getConnectedNodes(sourceNode).iterator();
        Node node;
        while(iterator.hasNext()) {
            node = (Node) iterator.next();
            if(marked.get(node)==null) {
                isGraphConnectedDFS(node);
            }
        }
        if(marked.size()==graph.getNumberOfNodes()) {
            return true;
        }
        return false;
    }
}
