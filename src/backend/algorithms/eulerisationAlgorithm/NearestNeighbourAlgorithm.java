package backend.algorithms.eulerisationAlgorithm;

import backend.algorithms.Heuristics;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import frontend.gui.AppGUI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * This class implements the nearest neighbour
 * algorithm to eulerise a graph
 * Jayen kumar Jaentilal k1189304
 */
public class NearestNeighbourAlgorithm extends EulerisationAlgorithm {

    private Node sourceNode;
    private Node minNode;
    private double currentMinDist;
    private double cost;

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

    /**
     * This method implements the
     * nearest neighbour algorithm for
     * the TSP problem when there are no
     * edges in the graph
     */
    private void tspNearestNeighbour() {
        HashMap<Node, Node> unvisitedNodes = new HashMap<Node, Node>(graph.getNumberOfNodes());
        super.putAllNodes(unvisitedNodes);
        Node startNode = graph.getNodes().iterator().next();
        Node prevNode = startNode;
        Node currentNode;
        currentNode = nextNearestUnvisitedNode(startNode, prevNode, unvisitedNodes);
        super.graph.addEdge(prevNode, currentNode);
        cost = Heuristics.computeEuclideanDistance(prevNode,currentNode);
        unvisitedNodes.remove(currentNode);
        prevNode = currentNode;
        while (unvisitedNodes.size() != 0) {
            currentNode = nextNearestUnvisitedNode(startNode, prevNode, unvisitedNodes);
            super.graph.addEdge(prevNode, currentNode);
            cost = cost + Heuristics.computeEuclideanDistance(prevNode,currentNode);
            unvisitedNodes.remove(currentNode);
            prevNode = currentNode;
        }
        AppGUI.updateCostPanel(this.getCost(),super.graph.getNumberOfNodes());
    }

    /**
     * Get the next nearest node that
     * has not been visited
     * @param startNode the starting node for the algorithm
     * @param prevNode  the last node the algorithm visited
     * @param unvisitedNodes the set of nodes that are yet to be visited
     * @return nearest node to the prevNode or the startNode if unvisitedNode is empty.
     */
    private Node nextNearestUnvisitedNode(Node startNode, Node prevNode, HashMap<Node, Node> unvisitedNodes) {
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

    /**
     * This method implements the nearest neighbour algorithm
     * for instances where there are multiple sub graphs in the main graph
     * which means they need to be connected up before eulerisation
     */
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

    /**
     * This method eulerises a connected graph with odd nodes
     * @param oddNodes nodes which have odd degree
     */
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

    /**
     * This methods gives the next best node
     * to connect from a sourceNode and the set of nodes to connect (nodesToSearch)
     * @param sourceNode -the node to connect from
     * @param nodesToSearch -the set of nodes that the sourceNode could connect to
     * @return node -best node to connect as defined by this method
     */
    private Node nextBestNodeToConnect(Node sourceNode, Node[] nodesToSearch) {
        /*
         Best node to connect as defined by this method
         smallest distance and degree odd node is the best
         next best is any odd degree node
         and lastly the shortest distance node with even degree
        */
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

    /**
     * This method connects up
     * all the subgraph such that at then end
     * the graph is connected and there are
     * no disconnected components
     */
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
        while(edgesAdded<subGraphs.size()-1) {

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
                }
            }
            edgesAdded++;
        }
    }

    /**
     * This method adds the edge depending
     * on the current connection type and the
     * connection type that the node and nextNode have
     * @param node node to connect from
     * @param nextNode node to connect to
     * @param currentConnectionType the current connection type that exists
     * @return
     */
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

    /**
     * update the distance and the nodes the connection is made to
     * @param node node to connect from
     * @param nextNode node to connect to
     * @param greedyUpdate force connection update -use when giving a certain connection type priority
     *                     like odd to odd connection over odd to even connection
     */
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

    public double getCost() {
        return cost;
    }
}
