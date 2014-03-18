package backend.algorithms;

import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import frontend.gui.AppGUI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class NearestNeighbourAlgorithm extends EulerisationAlgorithm {

    public NearestNeighbourAlgorithm(Graph graph) {
        super.graph = graph;
        super.subGraphs = new HashSet<Node[]>();
        super.nodes = new HashMap<Node, Node>();
        super.putAllNodes(nodes);
        if(graph.getNumberOfEdges()!=0) {
            super.findSubGraphs();//there may be subGraphs if there are some edges
        }
        this.nearestNeighbourEuleriseGraph();
    }

    public void nearestNeighbourEuleriseGraph() {
        if(subGraphs.size()==0) {
            tspNearestNeighbour();
            AppGUI.graphVisualiserPanel.drawNewGraph(graph);
        }
        else {
            nearestNeighbourSubGraphs();
            AppGUI.graphVisualiserPanel.drawNewGraph(graph);
        }
    }

    private void tspNearestNeighbour() {
        System.out.println("running tsp nearest neighbour");
        HashMap<Node,Node> unvisitedNodes = new HashMap<Node, Node>(graph.getNumberOfNodes());
        super.putAllNodes(unvisitedNodes);
        Node startNode = graph.getNodes().iterator().next();
        Node prevNode = startNode;
        Node currentNode;
        currentNode = nextNearestUnvistedNode(startNode,prevNode,unvisitedNodes);
        super.graph.addEdge(prevNode,currentNode);
        unvisitedNodes.remove(currentNode);
        prevNode = currentNode;
        while(unvisitedNodes.size()!=0) {
            currentNode = nextNearestUnvistedNode(startNode,prevNode,unvisitedNodes);
            super.graph.addEdge(prevNode,currentNode);
            unvisitedNodes.remove(currentNode);
            prevNode = currentNode;
        }
    }

    private Node nextNearestUnvistedNode(Node startNode, Node prevNode, HashMap<Node, Node> unvisitedNodes) {
        if(unvisitedNodes.size()==1) {//don't go back to the start node until the very end
            return startNode;
        }
        else {
            double minDistance = Double.MAX_VALUE;
            double currentDistance;
            Node minNode = null;
            for (Node nextNode : unvisitedNodes.keySet()) {
                if(nextNode!=startNode) {
                    currentDistance = computeDistance(prevNode, nextNode);
                    if (currentDistance < minDistance) {
                        minDistance = currentDistance;
                        minNode = nextNode;
                    }
                }
            }
            return minNode;
        }
    }

    private void nearestNeighbourSubGraphs() {

    }
}
