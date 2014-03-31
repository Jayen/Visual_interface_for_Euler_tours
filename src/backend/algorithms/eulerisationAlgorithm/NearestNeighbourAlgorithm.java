package backend.algorithms.eulerisationAlgorithm;

import backend.algorithms.Heuristics;
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

    private Node sourceNode;
    private Node minNode;
    private double currentMinDist;

    public NearestNeighbourAlgorithm(Graph graph) {
        super.graph = graph;
        super.subGraphs = new HashMap<String, Node[]>();
        super.nodes = new HashMap<Node, Node>();
        super.putAllNodes(nodes);
        if(graph.getNumberOfEdges()!=0) {
            super.findSubGraphs();//there may be subGraphs if there are some edges
        }
    }

    public void euleriseGraph(boolean visualise) {
        if(graph.getNumberOfEdges()==0) {
            tspNearestNeighbour();
            if(visualise) {
                AppGUI.graphVisualiserPanel.drawNewGraph(graph);
            }
        }
        else {
            nearestNeighbourSubGraphs();
            if(visualise) {
                AppGUI.graphVisualiserPanel.drawNewGraph(graph);
            }
        }
    }

    private void tspNearestNeighbour() {
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
        //don't go back to the start node until the very end
        if(unvisitedNodes.size()==1) {
            return startNode;
        }
        else {
            double minDistance = Double.MAX_VALUE;
            double currentDistance;
            Node minNode = null;
            for (Node nextNode : unvisitedNodes.keySet()) {
                if(nextNode!=startNode) {
                    currentDistance = Heuristics.computeEuclideanDistance(prevNode, nextNode);
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
            //we have multiple sub-graphs all with even degree
            connectTheSubGraphs();
        }
        HashMap<Node, Node> oddNodes = new HashMap<Node, Node>();
        super.putAllOddNodes(oddNodes);
        if(oddNodes.size()>0) {
            euleriseGraphWithOddNodes(oddNodes);
        }
    }

    private void euleriseGraphWithOddNodes(HashMap<Node, Node> oddNodes) {
        Iterator oddNodesIterator = oddNodes.keySet().iterator();
        Node sourceOddNode;
        Node nextNodeToConnect;
        while(oddNodesIterator.hasNext()) {
            sourceOddNode = (Node) oddNodesIterator.next();
            nextNodeToConnect = nextBestNodeToConnect(sourceOddNode,oddNodes.values().toArray(new Node[oddNodes.size()]));
            graph.addEdge(sourceOddNode,nextNodeToConnect);
            oddNodes.remove(sourceOddNode);
            oddNodes.remove(nextNodeToConnect);
            oddNodesIterator = oddNodes.keySet().iterator();
        }
    }

    private Node nextBestNodeToConnect(Node sourceNode, Node[] nodesToSearch) {
        double minDistance = Double.MAX_VALUE;
        double currentDistance;
        Node minNode = null;
        boolean foundOddNode = false;

        for (Node nextNode : nodesToSearch) {
            if(nextNode!=sourceNode) {
                if(foundOddNode) {
                    if(graph.degree(nextNode)%2!=0) {
                        currentDistance = Heuristics.computeEuclideanDistance(sourceNode, nextNode);
                        if(currentDistance < minDistance) {
                            minDistance = currentDistance;
                            minNode = nextNode;
                        }
                    }

                }
                else {
                    if(graph.degree(nextNode)%2!=0) {
                        foundOddNode = true;
                        minDistance = Heuristics.computeEuclideanDistance(sourceNode, nextNode);
                        minNode = nextNode;
                    }
                    else {
                        currentDistance = Heuristics.computeEuclideanDistance(sourceNode, nextNode);
                        if(currentDistance < minDistance) {
                            minDistance = currentDistance;
                            minNode = nextNode;
                        }
                    }
                }
            }
        }
        return minNode;
    }

    private void connectTheSubGraphs() {
        HashSet<String> connectedSubGraphKeys = new HashSet<String>();
        connectedSubGraphKeys.add("subGraph0");
        Node[] connectedSubGraph;
        Node[] unconnectedSubGraph;

        Iterator subGraphsKeyIterator = subGraphs.keySet().iterator();
        String subGraphKey;
        sourceNode = null;
        currentMinDist = Double.MAX_VALUE;
        minNode = null;
        Node nextNode;
        String currentConnectionType;

        //loop makes sure we add at least n-1 edges between n sub-graphs
        int edgesAdded = 0;
        while(edgesAdded<subGraphs.size()) {

            while(subGraphsKeyIterator.hasNext()) {//go through every subgraph
                subGraphKey = (String) subGraphsKeyIterator.next();

                if(!connectedSubGraphKeys.contains(subGraphKey)) {
                    unconnectedSubGraph = subGraphs.get(subGraphKey);

                    currentConnectionType = "evenToEven";
                    for (String connectedSubGraphKey : connectedSubGraphKeys) {

                        connectedSubGraph = subGraphs.get(connectedSubGraphKey);
                        for (Node node : connectedSubGraph) {
                            //prefer odd degree nodes over even degree nodes
                            //if there are no odd degree nodes for the connection then connect closest even degree nodes
                            nextNode = nextBestNodeToConnect(node, unconnectedSubGraph);
                            currentConnectionType = addEdgeBasedOnConnectionType(node,nextNode,currentConnectionType);
                        }
                    }
                    //add the unconnected subGraph to the connected set
                    connectedSubGraphKeys.add(subGraphKey);
                    graph.addEdge(sourceNode, minNode);
                    //add 2 edges between the 2 closest neighbours
                    //we add 2 since we cannot have odd edges for Euler tours
//                    if(currentConnectionType.equals("evenToEven")) {
//                        graph.addEdge(sourceNode, minNode);
//                    }
                }
            }
            edgesAdded++;
        }
    }

    private String addEdgeBasedOnConnectionType(Node node, Node nextNode, String currentConnectionType) {
        if(graph.degree(node)%2!=0) {//node has odd degree
            if(graph.degree(nextNode)%2!=0) {//nextNode has odd degree

                //only change the edges nodes if the connection is with shorter distance
                // since we already have a oddToOdd connection
                if(currentConnectionType.equals("oddToOdd")) {
                    if(graph.degree(node)%2!=0) {
                        if(graph.degree(nextNode)%2!=0) {
                            updateDistAndNodes(node,nextNode,false);
                        }
                    }
                }
                //change the edges if the connection is odd to odd as we prefer
                // odd to odd over odd to even and even to even connection
                else {
                    updateDistAndNodes(node,nextNode,true);
                    currentConnectionType = "oddToOdd";
                }
            }
            else {//nextNode has even degree
                //change the edges if this connection odd to even is less than the current odd to even connection
                if(currentConnectionType.equals("oddToEven")) {
                    updateDistAndNodes(node,nextNode,false);
                }
                //change the edges since we prefer odd to even connection over even to even connection
                if(currentConnectionType.equals("evenToEven")) {
                    updateDistAndNodes(node,nextNode,true);
                    currentConnectionType = "oddToEven";
                }
            }
        }
        else {//node has even degree
            if(graph.degree(nextNode)%2!=0) {//next node has odd degree
                //change the edges if this connection odd to even is less than the current odd to even connection
                if(currentConnectionType.equals("oddToEven")) {
                    updateDistAndNodes(node,nextNode,false);
                }
                //change the edges since we prefer odd to even connection over even to even connection
                if(currentConnectionType.equals("evenToEven")) {
                    updateDistAndNodes(node,nextNode,true);
                    currentConnectionType = "oddToEven";
                }
            }
            else {
                //next node has even degree
                //only change the edge if our current edge connection is even to even
                //since we prefer other types of connections
                if(currentConnectionType.equals("evenToEven")) {
                    updateDistAndNodes(node,nextNode,false);
                }
            }
        }
        return currentConnectionType;
    }

    private void updateDistAndNodes(Node node, Node nextNode,boolean greedyUpdate) {
        double dist = Heuristics.computeEuclideanDistance(node, nextNode);
        if(greedyUpdate) {
            currentMinDist = dist;
            sourceNode = node;
            minNode = nextNode;
        }
        else {
            if (dist < currentMinDist) {
                currentMinDist = dist;
                sourceNode = node;
                minNode = nextNode;
            }
        }
    }
}
