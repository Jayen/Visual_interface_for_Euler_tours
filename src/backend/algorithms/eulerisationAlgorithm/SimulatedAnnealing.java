package backend.algorithms.eulerisationAlgorithm;

import backend.algorithms.Heuristics;
import backend.internalgraph.Edge;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import frontend.gui.AppGUI;

import java.util.*;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class SimulatedAnnealing extends EulerisationAlgorithm {

    private Node sourceNode;
    private Node minNode;
    private double currentMinDist;
    private int edgesAdded = 0;
    private double cost = 0.0;

    private int numberOfPhases;
    private double temperature;
    private int numberOfTransitions;
    private Configuration bestConfig;
    private Configuration currentConfig;
    private boolean isTSP;

    public SimulatedAnnealing(Graph graph,boolean localSearch) {
        super.graph = new Graph(graph);
        super.subGraphs = new HashMap<String, Node[]>();
        super.nodes = new HashMap<Node, Node>();
        super.putAllNodes(nodes);
        if(graph.getNumberOfEdges()!=0) {
            super.findSubGraphs();//there may be subGraphs if there are some edges
        }
        numberOfPhases = 6000;
        numberOfTransitions = 500;
        temperature = 1500.0;
        isTSP = graph.getNumberOfEdges()==0;
        currentConfig = initialiseConfig();
        System.out.println(currentConfig.getConfig());
        if(isTSP) {
            bestConfig = new TSPConfig((TSPConfig)currentConfig);
        }
        else {
            bestConfig = new Config((Config)currentConfig);
        }
        this.simulatedAnnealing(localSearch);
    }

    public void simulatedAnnealing(boolean localSearch) {
        double prevConfigCost = currentConfig.getCost();
        double nextConfigCost;
        for(int phase = 0; phase<numberOfPhases; phase++) {
            for (int transition = 0; transition< numberOfTransitions; transition++) {
                System.out.println("phase "+phase+" transition "+transition);
                nextConfigCost = currentConfig.generateNeighbouringConfig();
                if(nextConfigCost<bestConfig.getCost()) {
                    System.out.println("improving");
                    if(isTSP) {
                        bestConfig = new TSPConfig((TSPConfig)currentConfig);
                    }
                    else {
                        bestConfig = new Config((Config)currentConfig);
                    }
                    prevConfigCost = nextConfigCost;
                }
                else {
                    if(localSearch) {
                        currentConfig.undoLastGeneration();
                    }
                    else {
                        if((Math.exp((prevConfigCost-nextConfigCost)/temperature) > Math.random())) {
                            //chance that we do want to accept a lower solution
                            prevConfigCost = nextConfigCost;
                        }
                        else {
                            currentConfig.undoLastGeneration();
                        }
                    }
                }
            }
            temperature=temperature-0.15;
        }
        AppGUI.graphVisualiserPanel.drawNewGraph(bestConfig.getGraphWithNewEdges());
        if(isTSP) {
            AppGUI.updateCostPanel(bestConfig.getCost(),bestConfig.getConfig().size()-1);
        }
        else {
            System.out.println(bestConfig.getConfig());
            AppGUI.updateCostPanel(bestConfig.getCost(), bestConfig.getConfig().size());
        }
    }

    private Configuration initialiseConfig() {
        if(isTSP) {
            return randomTSPConfig();
        }
        else {
            ArrayList<Edge> edgesConfig = new ArrayList<Edge>();
            connectTheSubGraphs(edgesConfig);
//            HashMap<Node, Node> oddNodes = new HashMap<Node, Node>();
//            super.putAllOddNodes(oddNodes);
//            if(oddNodes.size()>0) {
//                euleriseGraphWithOddNodes(oddNodes,edgesConfig);
//            }
            euleriseTheGraph(edgesConfig);
            removeTheEdgesAddedToTheGraph(edgesConfig);
            return new Config(edgesConfig,subGraphs);
        }
    }

    private void removeTheEdgesAddedToTheGraph(ArrayList<Edge> edgesConfig) {
        for(Edge edge : edgesConfig) {
            super.graph.removeEdge(edge.getFirstNode(),edge.getSecondNode());
        }
    }

    private void connectTheSubGraphs(ArrayList<Edge> edgesConfig) {
        HashSet<String> connectedSubGraphKeys = new HashSet<String>();
        connectedSubGraphKeys.add("subGraph0");

        Iterator subGraphsKeyIterator = subGraphs.keySet().iterator();
        String subGraphKeyToConnect;

        //loop makes sure we add at least n-1 edges between n sub-graphs
        int edgesAdded = 0;
        Node connectedNode;
        Node unconnectedNode;
        while(edgesAdded<subGraphs.size()-1) {
            while(subGraphsKeyIterator.hasNext()) {
                subGraphKeyToConnect = (String) subGraphsKeyIterator.next();
                if(!connectedSubGraphKeys.contains(subGraphKeyToConnect)) {
                    //if there is a odd node then get that else get any random even node
                    connectedNode = getAConnectedNode(connectedSubGraphKeys);
                    //get a unconnected node, if there is a odd node then get that else get any random even node
                    unconnectedNode = getAUnconnectedNode(subGraphKeyToConnect);
                    super.graph.addEdge(connectedNode,unconnectedNode);
                    edgesConfig.add(new Edge(connectedNode,unconnectedNode));
                    connectedSubGraphKeys.add(subGraphKeyToConnect);
                    edgesAdded++;
                }
            }
        }
    }

    private void euleriseTheGraph(ArrayList<Edge> edgesConfig) {
        Node oddDegreeNode1;
        Node oddDegreeNode2;
        Iterator nodesIterator = super.graph.getNodes().iterator();
        Node tempNode;
        Node tempNode2;
        while(nodesIterator.hasNext()) {
            tempNode = (Node) nodesIterator.next();
            if(super.graph.degree(tempNode)%2!=0) {
                oddDegreeNode1 = tempNode;
                while(nodesIterator.hasNext()) {
                    tempNode2 = (Node) nodesIterator.next();
                    if(super.graph.degree(tempNode2)%2!=0) {
                        oddDegreeNode2 = tempNode2;
                        edgesConfig.add(new Edge(oddDegreeNode1,oddDegreeNode2));
                        super.graph.addEdge(oddDegreeNode1,oddDegreeNode2);
                        break;
                    }
                }
            }
        }
    }

    private Node getAConnectedNode(HashSet<String> connectedSubGraphKeys) {
        Iterator keysIterator = connectedSubGraphKeys.iterator();
        Node node = null;
        Node[] connectedNodeArray;
        while(keysIterator.hasNext()) {
            connectedNodeArray = super.subGraphs.get(keysIterator.next());
            for(int i=0; i<connectedNodeArray.length; i++) {
                node = connectedNodeArray[i];
                if(super.graph.degree(node)%2!=0) {
                    return node;
                }
            }
        }
        return node;
    }

    private Node getAUnconnectedNode(String subGraphKeyToConnect) {
        List<Node> nodesList = Arrays.asList(super.subGraphs.get(subGraphKeyToConnect));

    Node node = null;
    Iterator listIterator = nodesList.listIterator();
    while(listIterator.hasNext()) {
        node = (Node) listIterator.next();
        if(super.graph.degree(node)%2!=0 || super.graph.degree(node)==0) {
            return node;
        }
    }
    return node;
}

    private TSPConfig randomTSPConfig() {
        ArrayList<Node> tspPath = new ArrayList<Node>();
        ArrayList<Node> unvisitedNodes = this.getAllNodes();
        Random randomGenerator = new Random();
        Node startNode = unvisitedNodes.get(randomGenerator.nextInt(unvisitedNodes.size()));
        Node currentNode = removeNextRandomNode(startNode, unvisitedNodes, randomGenerator);
        tspPath.add(startNode);
        tspPath.add(currentNode);
        while(unvisitedNodes.size()!=1) {
            currentNode = removeNextRandomNode(startNode, unvisitedNodes, randomGenerator);
            tspPath.add(currentNode);
        }
        tspPath.add(startNode);
        return new TSPConfig(tspPath);
    }

    private Node removeNextRandomNode(Node startNode, ArrayList<Node> unvisitedNodes, Random randomGenerator) {
        //don't go back to the start node until the very end
        if(unvisitedNodes.size()==0) {
            return startNode;
        }
        else {
            int randomKey = randomGenerator.nextInt(unvisitedNodes.size());
            Node node = unvisitedNodes.get(randomKey);
            while(node.equals(startNode)) {
                randomKey = randomGenerator.nextInt(unvisitedNodes.size());
                node = unvisitedNodes.get(randomKey);
            }
            unvisitedNodes.remove(randomKey);
            return node;
        }
    }

    private ArrayList<Node> getAllNodes() {
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (Node node : super.graph.getNodes()) {
            nodes.add(node);
        }
        return nodes;
    }




//    /**
//     * This method connects up
//     * all the sub-graphs such that at then end
//     * the graph is connected and there are
//     * no disconnected components
//     */
//    private void connectTheSubGraphs(ArrayList<Edge> edgesConfig) {
//        HashSet<String> connectedSubGraphKeys = new HashSet<String>();
//        connectedSubGraphKeys.add("subGraph0");
//        Node[] connectedSubGraph;
//        Node[] unconnectedSubGraph;
//
//        Iterator subGraphsKeyIterator = subGraphs.keySet().iterator();
//        String subGraphKey;
//        sourceNode = null;
//        currentMinDist = Double.MAX_VALUE;
//        minNode = null;
//        Node nextNode;
//        String currentConnectionType;
//
//        //loop makes sure we add at least n-1 edges between n sub-graphs
//        int edgesAdded = 0;
//        while(edgesAdded<subGraphs.size()-1) {
//
//            while(subGraphsKeyIterator.hasNext()) {//go through every sub-graph
//                subGraphKey = (String) subGraphsKeyIterator.next();
//
//                if(!connectedSubGraphKeys.contains(subGraphKey)) {
//                    unconnectedSubGraph = subGraphs.get(subGraphKey);
//
//                    currentConnectionType = "evenToEven";
//                    for (String connectedSubGraphKey : connectedSubGraphKeys) {
//
//                        connectedSubGraph = subGraphs.get(connectedSubGraphKey);
//                        for (Node node : connectedSubGraph) {
//                            //prefer odd degree nodes over even degree nodes
//                            //if there are no odd degree nodes for the connection then connect closest even degree nodes
//                            nextNode = nextBestNodeToConnect(node, unconnectedSubGraph);
//                            currentConnectionType = addEdgeBasedOnConnectionType(node,nextNode,currentConnectionType);
//                        }
//                    }
//                    //add the unconnected subGraph to the connected set
//                    connectedSubGraphKeys.add(subGraphKey);
//                    super.graph.addEdge(sourceNode, minNode);
//                    edgesConfig.add(new Edge(sourceNode,minNode));
//                    cost = cost + Heuristics.computeEuclideanDistance(sourceNode, minNode);
//                    this.edgesAdded++;
//                }
//            }
//            edgesAdded++;
//        }
//    }

//    /**
//     * This methods gives the next best node
//     * to connect from a sourceNode and the set of nodes to connect (nodesToSearch)
//     * @param sourceNode the node to connect from
//     * @param nodesToSearch the set of nodes that the sourceNode could connect to
//     * @return node best node to connect
//     */
//    private Node nextBestNodeToConnect(Node sourceNode, Node[] nodesToSearch) {
//        /*
//         Best node to connect as defined by this method
//         smallest distance and degree odd node is the best
//         next best is any odd degree node
//         and lastly the shortest distance node with even degree
//        */
//        double minDistance = Double.MAX_VALUE;
//        double currentDistance;
//        Node bestNode = null;
//        boolean foundOddNode = false;
//
//        for (Node nextNode : nodesToSearch) {
//            if(nextNode!=sourceNode) {
//                if(foundOddNode) {
//                    if(graph.degree(nextNode)%2!=0) {
//                        currentDistance = Heuristics.computeEuclideanDistance(sourceNode, nextNode);
//                        if(currentDistance < minDistance) {
//                            minDistance = currentDistance;
//                            bestNode = nextNode;
//                        }
//                    }
//                }
//                else {
//                    if(graph.degree(nextNode)%2!=0) {
//                        foundOddNode = true;
//                        minDistance = Heuristics.computeEuclideanDistance(sourceNode, nextNode);
//                        bestNode = nextNode;
//                    }
//                    else {
//                        currentDistance = Heuristics.computeEuclideanDistance(sourceNode, nextNode);
//                        if(currentDistance < minDistance) {
//                            minDistance = currentDistance;
//                            bestNode = nextNode;
//                        }
//                    }
//                }
//            }
//        }
//        return bestNode;
//    }
//
//    /**
//     * This method adds the edge depending
//     * on the current connection type and the
//     * connection type that the node and nextNode have
//     * @param node node to connect from
//     * @param nextNode node to connect to
//     * @param currentConnectionType the current connection type that exists
//     * @return
//     */
//    private String addEdgeBasedOnConnectionType(Node node, Node nextNode, String currentConnectionType) {
//        if(graph.degree(node)%2!=0) {//node has odd degree
//            if(graph.degree(nextNode)%2!=0) {//nextNode has odd degree
//
//                //only change the edges nodes if the connection is with shorter distance
//                // since we already have a oddToOdd connection
//                if(currentConnectionType.equals("oddToOdd")) {
//                    if(graph.degree(node)%2!=0) {
//                        if(graph.degree(nextNode)%2!=0) {
//                            updateDistAndNodes(node,nextNode,false);
//                        }
//                    }
//                }
//                //change the edges if the connection is odd to odd as we prefer
//                // odd to odd over odd to even and even to even connection
//                else {
//                    updateDistAndNodes(node,nextNode,true);
//                    currentConnectionType = "oddToOdd";
//                }
//            }
//            else {//nextNode has even degree
//                //change the edges if this connection odd to even is less than the current odd to even connection
//                if(currentConnectionType.equals("oddToEven")) {
//                    updateDistAndNodes(node,nextNode,false);
//                }
//                //change the edges since we prefer odd to even connection over even to even connection
//                if(currentConnectionType.equals("evenToEven")) {
//                    updateDistAndNodes(node,nextNode,true);
//                    currentConnectionType = "oddToEven";
//                }
//            }
//        }
//        else {//node has even degree
//            if(graph.degree(nextNode)%2!=0) {//next node has odd degree
//                //change the edges if this connection odd to even is less than the current odd to even connection
//                if(currentConnectionType.equals("oddToEven")) {
//                    updateDistAndNodes(node,nextNode,false);
//                }
//                //change the edges since we prefer odd to even connection over even to even connection
//                if(currentConnectionType.equals("evenToEven")) {
//                    updateDistAndNodes(node,nextNode,true);
//                    currentConnectionType = "oddToEven";
//                }
//            }
//            else {
//                //next node has even degree
//                //only change the edge if our current edge connection is even to even
//                //since we prefer other types of connections
//                if(currentConnectionType.equals("evenToEven")) {
//                    updateDistAndNodes(node,nextNode,false);
//                }
//            }
//        }
//        return currentConnectionType;
//    }
//
//    /**
//     * update the distance and the nodes the connection is made to
//     * @param node node to connect from
//     * @param nextNode node to connect to
//     * @param greedyUpdate force connection update -use when giving a certain connection type priority
//     *                     like odd to odd connection over odd to even connection
//     */
//    private void updateDistAndNodes(Node node, Node nextNode,boolean greedyUpdate) {
//        double dist = Heuristics.computeEuclideanDistance(node, nextNode);
//        if(greedyUpdate) {
//            currentMinDist = dist;
//            sourceNode = node;
//            minNode = nextNode;
//        }
//        else {
//            if (dist < currentMinDist) {
//                currentMinDist = dist;
//                sourceNode = node;
//                minNode = nextNode;
//            }
//        }
//    }
//
//    /**
//     * This method eulerises a connected graph with odd nodes
//     * @param oddNodes nodes which have odd degree
//     */
//    private void euleriseGraphWithOddNodes(HashMap<Node, Node> oddNodes,ArrayList<Edge> edgesConfig) {
//        Iterator oddNodesIterator = oddNodes.keySet().iterator();
//        Node sourceOddNode;
//        Node nextNodeToConnect;
//        while(oddNodesIterator.hasNext()) {
//            sourceOddNode = (Node) oddNodesIterator.next();
//            nextNodeToConnect = nextBestNodeToConnect(sourceOddNode,oddNodes.values().toArray(new Node[oddNodes.size()]));
//            super.graph.addEdge(sourceOddNode,nextNodeToConnect);
//            edgesConfig.add(new Edge(sourceOddNode,nextNodeToConnect));
//            cost = cost + Heuristics.computeEuclideanDistance(sourceOddNode,nextNodeToConnect);
//            edgesAdded++;
//            oddNodes.remove(sourceOddNode);
//            oddNodes.remove(nextNodeToConnect);
//            oddNodesIterator = oddNodes.keySet().iterator();
//        }
//    }
}
