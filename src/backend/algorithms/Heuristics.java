package backend.algorithms;

import backend.internalgraph.Node;

/**
 * This class has the heuristics
 * for the algorithms
 * Jayen kumar Jaentilal k1189304
 */
public class Heuristics {

    public static double computeEuclideanDistance(Node currentNode, Node nextNode) {
        //euclidean distance (pythagoras)
        return Math.sqrt((currentNode.getX()-nextNode.getX())*(currentNode.getX()-nextNode.getX())
                +(currentNode.getY()-nextNode.getY())*(currentNode.getY()-nextNode.getY()));
    }

    public static double computeManhattanDistance(Node currentNode, Node nextNode) {
        double xDiff = Math.abs(currentNode.getX() - nextNode.getX());
        double yDiff = Math.abs(currentNode.getY() - nextNode.getY());
        return xDiff+yDiff;
    }
}
