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
    private LinkedList<Edge> edges;

    public Graph() {
        nodes = new HashMap<Node, List<Node>>();
        edges = new LinkedList<Edge>();
    }

    /**
     * Copy constructor for graph
     * @param graph graph to copy
     */
    public Graph(Graph graph) {
        nodes = new HashMap<Node, List<Node>>();
        edges = new LinkedList<Edge>();
        this.copyNodesMap(graph.getNodesMap());
        this.copyEdges(graph.getEdges());
    }

    public void addNode(Node nodeToAdd) {
        if(nodeToAdd ==null) {
            throw new IllegalArgumentException("node cannot be null");
        }
        if(!nodes.containsValue(nodeToAdd)) {
            nodes.put(nodeToAdd,new LinkedList<Node>());
        }
    }

    public void removeNode(Node nodeToRemove) {
        LinkedList<Node> incidentNodes = (LinkedList<Node>) nodes.get(nodeToRemove);
        Iterator nodesIterator = incidentNodes.iterator();
        Node oppositeNode;
        while(nodesIterator.hasNext()) {
            oppositeNode = (Node) nodesIterator.next();
            nodes.get(oppositeNode).remove(nodeToRemove);
        }
        nodes.remove(nodeToRemove);
        for(int i=0; i<edges.size(); i++) {
            if(edges.get(i).contains(nodeToRemove)) {
                edges.remove(i);
            }
        }
    }

    public void removeEdge(Node node1, Node node2) {
        nodes.get(node1).remove(node2);
        nodes.get(node2).remove(node1);
        edges.remove(new Edge(node1,node2));
    }

    public void addEdge(Node node1, Node node2) {
        //iterate to find the original nodes in the key set rather than using new objects of node
        Iterator<Node> keyNodesIterator = nodes.keySet().iterator();
        Node currentNode;
        while(keyNodesIterator.hasNext()) {
            currentNode = keyNodesIterator.next();
            if(currentNode.equals(node1)) {
                node1 = currentNode;
            }
            if(currentNode.equals(node2)) {
                node2 = currentNode;
            }
        }
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
        edges.add(new Edge(node1,node2));
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
                                                       /**
 * User: jayen
 * Date: 31/03/14, Time: 13:08
 */
    public LinkedList<Edge> getEdges() {
        return edges;
    }

    public int getNumberOfEdges() {
        return edges.size();
    }

    public HashMap<Node,List<Node>> getNodesMap() {
        return nodes;
    }

    public void copyNodesMap(HashMap<Node,List<Node>> nodesMap) {
        for(Node node : nodesMap.keySet()) {
            LinkedList<Node> nodeList = new LinkedList<Node>();
            for(Node connectedNodes : nodesMap.get(node)) {
                nodeList.add(new Node(connectedNodes));
            }
            this.nodes.put(new Node(node),nodeList);
        }
    }

    public void copyEdges(LinkedList<Edge> edges) {
        for(Edge edge : edges) {
            this.edges.add(new Edge(edge));
        }
    }

    public void printGraph() {
        Iterator nodesIterator = nodes.keySet().iterator();
        Node sourceNode;
        List<Node> connectedNodes;
        while(nodesIterator.hasNext()) {
            sourceNode = (Node) nodesIterator.next();
            System.out.print("sourceNode is: "+sourceNode+" reachable nodes: ");
            connectedNodes = nodes.get(sourceNode);
            for(Node node : connectedNodes) {
                System.out.print(" "+node);
            }
            System.out.println();
        }
    }
}
