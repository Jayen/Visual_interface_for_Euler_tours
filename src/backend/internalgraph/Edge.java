package backend.internalgraph;

/**
 * This class represents a
 * undirected edge between 2 nodes
 * @author Jayen kumar Jaentilal k1189304
 */
public class Edge {

    private Node n1, n2;

    public Edge(Node n1, Node n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    public boolean contains(Node node) {
        if(n1.equals(node)) {
            return true;
        }
        else if(n2.equals(node)) {
            return true;
        }
        return false;
    }

    public Node getFirstNode() {
        return n1;
    }

    public void setFirstVertex(Node v1) {
        this.n1 = v1;
    }

    public Node getSecondNode() {
        return n2;
    }

    public void setSecondVertex(Node v2) {
        this.n2 = v2;
    }

    public Node getOpposite(Node sourceNode) {
        if(sourceNode.equals(n1)) {
            return n2;
        }
        if(sourceNode.equals(n2)){
            return n1;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null) {
            return false;
        }
        if(obj.getClass()!=this.getClass()) {
            return false;
        }
        Edge otherEdge = (Edge) obj;
        if(this.contains(otherEdge.getFirstNode()) && this.contains(otherEdge.getSecondNode())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return n1.toString()+ n2.toString();
    }
}
