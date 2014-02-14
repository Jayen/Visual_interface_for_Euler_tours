package backend.internalgraph;

import java.awt.geom.Point2D;

/**
 * Vertex represents a vertex of the graph
 * @author Jayen kumar Jaentilal k1189304
 */

public class Vertex {

    private String vertexName;
    private Point2D location;

    public Vertex(String vertexName, Point2D location) {
        this.vertexName = vertexName;
        this.location = location;
    }

    public String getVertexName() {
        return vertexName;
    }

    public Point2D getLocation() {
        return location;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null) {
            return false;
        }
        if(obj.getClass()!=this.getClass()) {
            return false;
        }

        Vertex otherVertex = (Vertex) obj;
        if(otherVertex.getVertexName().equals(this.getVertexName())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getVertexName();
    }
}