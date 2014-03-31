package backend.algorithms;

import backend.internalgraph.Node;

/**
 * User: jayen
 * Date: 28/03/14, Time: 13:33
 */
public class Heuristics {

    public static double computeEuclideanDistance(Node currentNode, Node nextNode) {
        //euclidean distance (pythagoras)
        return Math.sqrt((currentNode.getX()-nextNode.getX())*(currentNode.getX()-nextNode.getX())
                +(currentNode.getY()-nextNode.getY())*(currentNode.getY()-nextNode.getY()));
    }
}
