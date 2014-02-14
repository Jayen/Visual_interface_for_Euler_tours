package backend.internalgraph;

/**
 * This class represents a
 * undirected edge between 2 vertices
 * @author Jayen kumar Jaentilal k1189304
 */
public class Edge {

    private Vertex v1, v2;

    public Edge(Vertex v1, Vertex v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public Vertex getFirstVertex() {
        return v1;
    }

    public void setFirstVertex(Vertex v1) {
        this.v1 = v1;
    }

    public Vertex getSecondVertex() {
        return v2;
    }

    public void setSecondVertex(Vertex v2) {
        this.v2 = v2;
    }

    public Vertex getOpposite(Vertex sourceVertex) {
        if(sourceVertex.equals(v1)) {
            return v2;
        }
        if(sourceVertex.equals(v2)){
            return v1;
        }
        return null;
    }
}
