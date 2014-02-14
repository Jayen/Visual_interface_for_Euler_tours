package backend.internalgraph;

/**
 * This class represents a
 * undirected edge between 2 vertices
 * @author Jayen kumar Jaentilal k1189304
 */
public class Edge {

    private Node v1, v2;

    public Edge(Node v1, Node v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public Node getFirstNode() {
        return v1;
    }

    public void setFirstVertex(Node v1) {
        this.v1 = v1;
    }

    public Node getSecondNode() {
        return v2;
    }

    public void setSecondVertex(Node v2) {
        this.v2 = v2;
    }

    public Node getOpposite(Node sourceNode) {
        if(sourceNode.equals(v1)) {
            return v2;
        }
        if(sourceNode.equals(v2)){
            return v1;
        }
        return null;
    }
}
