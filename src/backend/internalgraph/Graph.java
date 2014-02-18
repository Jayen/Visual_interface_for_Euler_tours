package backend.internalgraph;

import java.util.*;

/**
* This class represents the graph internally
 * represented as a adjacency map
 * each node in the graph must have a unique name
 * @author Jayen kumar Jaentilal k1189304
*/

public class Graph {

    private HashMap<Node, List<Node>> nodes;
    private int numberOfEdges;

    public Graph() {
        nodes = new HashMap<Node, List<Node>>();
        numberOfEdges = 0;
    }

    public void addNode(Node node) {
        if(node ==null) {
            throw new IllegalArgumentException("node cannot be null");
        }
        if(!nodes.containsValue(node)) {
            nodes.put(node,new LinkedList<Node>());
        }
    }

    public void removeNode(Node node) {
        LinkedList<Node> incidentNodes = (LinkedList<Node>) nodes.get(node);
        Iterator nodesIterator = incidentNodes.iterator();
        Node oppositeNode;
        while(nodesIterator.hasNext()) {
            oppositeNode = (Node) nodesIterator.next();
            nodes.get(oppositeNode).remove(node);
        }
        nodes.remove(node);
    }

    public void removeEdge(Node node1, Node node2) {
        nodes.get(node1).remove(node2);
        nodes.get(node2).remove(node1);
        numberOfEdges--;
    }

    public void addEdge(Node node1, Node node2) {
        LinkedList<Node> connectedNodes = (LinkedList<Node>) nodes.get(node1);
        if(connectedNodes==null) {
            connectedNodes = new LinkedList<Node>();
            connectedNodes.add(node2);
        }
        else {
            connectedNodes.add(node2);
        }
        connectedNodes = (LinkedList<Node>) nodes.get(node2);
        if(connectedNodes==null) {
            connectedNodes = new LinkedList<Node>();
            connectedNodes.add(node1);
        }
        else {
            connectedNodes.add(node1);
        }
        numberOfEdges++;
    }

   public Collection<Node> getIncidentNodes(Node node) {
       return nodes.get(node);
   }


    public boolean containsNode(Node node) {
        return nodes.containsKey(node);
    }

    public Set<Node> getNodes() {
        return nodes.keySet();
    }

    public int degree(Node node) {
        if(nodes.get(node)==null) {
            return 0;
        }
        return nodes.get(node).size();
    }

    public int getNumberOfNodes() {
        return nodes.size();
    }

    public Collection getConnectedNodes(Node sourceNode) {
        Iterator<Node> connectedNodesIterator = nodes.get(sourceNode).iterator();
        Set<Node> connectedNodes = new HashSet<Node>();//set removes duplicates i.e if there are multiple edges to the same node
        while(connectedNodesIterator.hasNext()) {
            connectedNodes.add(connectedNodesIterator.next());
        }
        return connectedNodes;
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }
}
