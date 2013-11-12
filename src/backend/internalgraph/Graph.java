package backend.internalgraph;

import java.util.ArrayList;

/**
 * This class represents the graph
 * Internally holds a set of Nodes in the graph
 * @author Jayen kumar Jaentilal k1189304
 */

public class Graph {

    private ArrayList<Node> nodes;

    public Graph() {
        nodes = new ArrayList<Node>();
    }

    /**
     * Add a node to the graph
     * @param node
     */
    public void addNode(Node node) {
        nodes.add(node);
    }

    /**
     * Get a certain node in the graph
     * index starts from 0
     * @param index
     * @return Node -node at index
     */
    public Node getNode(int index) {
        return nodes.get(index);
    }

    /**
     * Get the node with the given name
     * @param nodeName
     * @return
     */
    public Node getNode(String nodeName) {
        for(int i=0; i<this.getNumberOfNodes(); i++) {
            if(this.getNode(i).equals(nodeName)) {
                return nodes.get(i);
            }
        }
        return null;
    }

    /**
     * Get the number of nodes in the graph
     * @return int -number of nodes
     */
    public int getNumberOfNodes() {
        return nodes.size();
    }

    /**
     * Connect 2 nodes in the graph
     * @param node1
     * @param node2
     */
    public void connectNodes(Node node1, Node node2) {
        node1.connectNode(node2);
        node2.connectNode(node1);
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        Node node;
        ArrayList<Node> connectedNode;

        for(int i=0; i<this.getNumberOfNodes(); i++) {
            node = this.getNode(i);
            stringBuffer.append(node.toString()+ " connectedNodes: ");
            connectedNode = node.getConnectedNodes();

            for(int j=0; j<connectedNode.size(); i++) {
                stringBuffer.append(connectedNode.get(j).toString()+" ");
            }
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }
}
