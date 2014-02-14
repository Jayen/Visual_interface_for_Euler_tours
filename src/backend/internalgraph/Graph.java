package backend.internalgraph;

import java.util.*;

/**
* This class represents the graph internally
 * represented as a adjacency map
 * each node in the graph must have a unique name
 * @author Jayen kumar Jaentilal k1189304
*/

public class Graph {

    private Map<Node,Set<Edge>> nodes;
    private int edgeCount;

    public Graph() {
        nodes = new HashMap<Node, Set<Edge>>();
        edgeCount = 0;
    }

    public void addNode(Node node) {
        if(node ==null) {
            throw new IllegalArgumentException("node cannot be null");
        }
        if(!nodes.containsValue(node)) {
            nodes.put(node,new HashSet<Edge>());
        }
    }

    public void removeNode(Node node) {
        Set<Edge> incidentEdges = nodes.get(node);
        Iterator edgesIterator = incidentEdges.iterator();
        Node oppositeNode;
        Edge edge;
        while(edgesIterator.hasNext()) {
            edge = (Edge)edgesIterator.next();
            oppositeNode = edge.getOpposite(node);
            nodes.get(oppositeNode).remove(edge);
        }
        nodes.remove(node);
    }

    public void removeEdge(Edge edge) {
        Node node = edge.getFirstNode();
        nodes.get(node).remove(edge);
        node = edge.getSecondNode();
        nodes.get(node).remove(edge);
        edgeCount--;
    }

    public void addEdge(Edge edge) {
        HashSet<Edge> edges = (HashSet<Edge>) nodes.get(edge.getFirstNode());
        if(edges==null) {
            edges = new HashSet<Edge>();
            edges.add(edge);
        }
        else {
            edges.add(edge);
        }
        edges = (HashSet<Edge>) nodes.get(edge.getSecondNode());
        if(edges==null) {
            edges = new HashSet<Edge>();
            edges.add(edge);
        }
        else {
            edges.add(edge);
        }
        edgeCount++;
    }

   public Collection<Edge> getIncidentEdges(Node node) {
       return nodes.get(node);
   }


    public boolean containsNode(Node node) {
        return nodes.containsKey(node);
    }

    public Set<Node> getNodes() {
        return nodes.keySet();
    }

    public int degree(Node node) {
        return nodes.get(node).size();
    }

    public int getNodesCount() {
        return nodes.size();
    }

    public Collection getConnectedNodes(Node sourceNode) {
        Iterator<Edge> edges = nodes.get(sourceNode).iterator();
        Set<Node> connectedNodes = new HashSet<Node>();
        while(edges.hasNext()) {
            connectedNodes.add(edges.next().getOpposite(sourceNode));
        }
        return connectedNodes;
    }

    public int getEdgeCount() {
        return edgeCount;
    }
}
