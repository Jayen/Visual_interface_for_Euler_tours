package backend.internalgraph;

import java.util.ArrayList;

/**
 * Node represents a vertex of the graph
 * @author Jayen kumar Jaentilal k1189304
 */

public class Node {

    private String nodeName;
    private ArrayList<Node> connectedNodes = new ArrayList<Node>();

    public Node(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Connect a node to this node
     * @param node
     */
    public void connectNode(Node node) {
        this.connectedNodes.add(node);
    }

    public String getNodeName() {
        return nodeName;
    }

    /**
     * Get all the nodes connected to this node
     * @return ArrayList<Node> -list of connected nodes
     */
    public ArrayList<Node> getConnectedNodes() {
        return  connectedNodes;
    }

    @Override
    public String toString() {
        return this.getNodeName();
    }
}