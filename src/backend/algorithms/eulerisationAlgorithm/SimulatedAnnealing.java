package backend.algorithms.eulerisationAlgorithm;

import backend.internalgraph.Edge;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import frontend.gui.AppGUI;

import java.util.*;

/**
 * This class implement the simulated
 * annealing algorithm for eulerisation
 * Jayen kumar Jaentilal k1189304
 */
public class SimulatedAnnealing extends EulerisationAlgorithm {

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
        numberOfTransitions = 400;
        temperature = 1500.0;
        isTSP = graph.getNumberOfEdges()==0;
        currentConfig = initialiseConfig();
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
                nextConfigCost = currentConfig.generateNeighbouringConfig();
                if(nextConfigCost<bestConfig.getCost()) {
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
            temperature=temperature-0.35;
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

    /**
     * Generate the initial configuration
     * for the simulated annealing algorithm
     * @return
     */
    private Configuration initialiseConfig() {
        if(isTSP) {
            return randomTSPConfig();
        }
        else {
            ArrayList<Edge> edgesConfig = new ArrayList<Edge>();
            connectTheSubGraphs(edgesConfig);
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

    /**
     * This method randomly following
     * a set of preferences connects
     * all the sub-graphs
     * @param edgesConfig the edges used to connect
     *                    the sub-graphs will be added
     *                    to the edgesConfig
     */
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

    /**
     * Randomly eulerise a graph
     * @param edgesConfig the edges used to eulerise
     *                    will be added to the edgesConfig
     */
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

    /**
     * Get a node that is part of the connected sub-graphs
     * @param connectedSubGraphKeys the keys of the connected sub-graphs
     * @return Node -a node in the connected sub-graph, if there are nodes
     *               which have odd degree then they are returned else any
     *               node with even degree is returned
     */
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

    /**
     * Get a node that is part of the sub-graph given
     * @param subGraphKeyToConnect the keys of the sub-graph to be connected
     * @return Node -a node in the sub-graph, if there are nodes
     *               which have odd degree then they are returned else any
     *               node with even degree is returned
     */
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

    /**
     * This method generates the config
     * for when the problem is the TSP.
     * The method generates a random solution
     * for the problem
     * @return TSPConfig -Config representing the random solution
     */
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

    /**
     * Get and remove a random node from
     * the set of unvisited nodes.
     * @param startNode the source node for the TSP
     * @param unvisitedNodes set of nodes that still need to be visited
     * @param randomGenerator the Random instance to be used for
     *                        generating a random choice
     * @return
     */
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
}