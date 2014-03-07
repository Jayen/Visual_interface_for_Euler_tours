package backend.internalgraph;

import java.awt.geom.Point2D;

/**
 * This class represents a node in the graph
 * A Node has a name and a location
 * @author Jayen kumar Jaentilal k1189304
 */

public class Node {

    private String nodeName;
    private Point2D location;

    public Node(String nodeName, Point2D location) {
        this.nodeName = nodeName;
        this.location = location;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String name) {
        this.nodeName = name;
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

        Node otherNode = (Node) obj;
        if(otherNode.getNodeName().equals(this.getNodeName())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getNodeName();
    }

    @Override
    public int hashCode() {
        return nodeName != null ? nodeName.hashCode() : 0;
    }

    public double getX() {
        return location.getX();
    }

    public double getY() {
        return location.getY();
    }
}