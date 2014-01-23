package backend.internalgraph;

import java.awt.geom.Point2D;

/**
 * Node represents a vertex of the graph
 * @author Jayen kumar Jaentilal k1189304
 */

public class Node {

    private String nodeName;
    private Point2D location;

    public Node(String nodeName,Point2D location) {
        this.nodeName = nodeName;
        this.location = location;
    }

    public String getNodeName() {
        return nodeName;
    }

    @Override
    public String toString() {
        return this.getNodeName();
    }

    public Point2D getLocation() {
        return location;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }
}