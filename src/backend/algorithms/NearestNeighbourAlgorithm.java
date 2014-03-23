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
        super.subGraphs = new HashMap<String, Node[]>();
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
        if(subGraphs.size()>1) {
            HashMap<Node, Node> oddNodes = new HashMap<Node, Node>();
            super.putAllOddNodes(oddNodes);
            if(oddNodes.size()==0) {
                connectSubGraphsEvenDegree();//we have multiple sub-graphs all with even degree
            }
        }
    }

    private void connectSubGraphsEvenDegree() {
        HashSet<String> connectedSubGraphKeys = new HashSet<String>();
        connectedSubGraphKeys.add("subGraph0");
        Node[] connectedSubGraph;
        Node[] unconnectedSubGraph;

        Iterator subGraphsKeyIterator = subGraphs.keySet().iterator();
        String subGraphKey;
        Node sourceNode = null;
        double minDist = Double.MAX_VALUE;
        Node minNode = null;
        Node nextNode;
        double dist;

        //for loop makes sure we ad at least n-1 edges between n sub-graphs
        for(int edgesAdded=0; edgesAdded<subGraphs.size(); edgesAdded++) {

            while(subGraphsKeyIterator.hasNext()) {//go through every subgraph
                subGraphKey = (String) subGraphsKeyIterator.next();

                if(!connectedSubGraphKeys.contains(subGraphKey)) {
                    unconnectedSubGraph = subGraphs.get(subGraphKey);

                    for (String connectedSubGraphKey : connectedSubGraphKeys) {
                        connectedSubGraph = subGraphs.get(connectedSubGraphKey);
                        for (Node node : connectedSubGraph) {
                            //find the nearest node in the unconnected sub graph
                            nextNode = nextNearestNodeInSubGraph(node, unconnectedSubGraph);
                            dist = super.computeDistance(node, nextNode);
                            if (dist < minDist) {
                                minDist = dist;
                                sourceNode = node;
                                minNode = nextNode;
                            }
                        }
                    }
                    connectedSubGraphKeys.add(subGraphKey);//add the unconnected subGraph to the connected set
                    //add 2 edges between the 2 closest neighbours
                    //we add 2 since we cannot have odd edges for Euler tours
                    graph.addEdge(sourceNode,minNode);
                    graph.addEdge(sourceNode,minNode);
                }
            }
        }
    }

    private Node nextNearestNodeInSubGraph(Node sourceNode, Node[] nodesToSearch) {
        double minDistance = Double.MAX_VALUE;
        double currentDistance;
        Node minNode = null;
        for (Node nextNode : nodesToSearch) {
            if(nextNode!=sourceNode) {
                currentDistance = computeDistance(sourceNode, nextNode);
                if (currentDistance < minDistance) {
                    minDistance = currentDistance;
                    minNode = nextNode;
                }
            }
        }
        return minNode;
    }
}
