package backend.algorithms.eulerisationAlgorithm;

import backend.internalgraph.Edge;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import frontend.gui.AppGUI;

import java.util.*;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class SimulatedAnnealing extends EulerisationAlgorithm {

    private int numberOfPhases;
    private double temperature;
    private int numberOfTransistions;
    private Configruation bestConfig;
    private Configruation currentConfig;
    private boolean isTSP;

    public SimulatedAnnealing(Graph graph,boolean localSearch) {
        super.graph = new Graph(graph);
        numberOfPhases = 5000;
        numberOfTransistions = 1000;
        temperature = 150.0;
        currentConfig = intialiseConfig();
        if(isTSP) {
            bestConfig = new TSPConfig((TSPConfig)currentConfig);
        }
        else {
            bestConfig = new Config((Config)currentConfig);
        }
        this.simluatedAnnealing(localSearch);
    }

    public void simluatedAnnealing(boolean localSearch) {
        double prevConfigCost = currentConfig.getCost();
        double nextConfigCost;
        for(int phase = 0; phase<numberOfPhases; phase++) {
            for (int transition = 0; transition<numberOfTransistions; transition++) {
                nextConfigCost = currentConfig.generateNeighbouringConfig();
                if(nextConfigCost<bestConfig.getCost()) {
                    bestConfig = new TSPConfig((TSPConfig)currentConfig);
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
        AppGUI.graphVisualiserPanel.drawNewGraph(bestConfig.getGraph());
    }

    private Configruation intialiseConfig() {
        super.subGraphs = new HashMap<String, Node[]>();
        if(graph.getNumberOfEdges()!=0) {
            super.findSubGraphs();
        }
        if(graph.getNumberOfEdges()==0) {
            isTSP = true;//TSP path is also the euler path
            return randomTSPConfig();
        }
        else {
            ArrayList<Edge> edgesConfig = new ArrayList<Edge>();
            connectTheSubGraphs(edgesConfig);
            euleriseTheGraph(edgesConfig);
            return null;
        }
    }

    private void connectTheSubGraphs(ArrayList<Edge> edgesConfig) {
        HashSet<String> connectedSubGraphKeys = new HashSet<String>();
        connectedSubGraphKeys.add("subGraph0");

        Iterator subGraphsKeyIterator = subGraphs.keySet().iterator();
        String subGraphKey;

        //loop makes sure we add at least n-1 edges between n sub-graphs
        int edgesAdded = 0;
        Node connectedNode;
        Node unconnectedNode;
        while(edgesAdded<subGraphs.size()) {
            while(subGraphsKeyIterator.hasNext()) {
                subGraphKey = (String) subGraphsKeyIterator.next();
                if(connectedSubGraphKeys.contains(subGraphKey)) {
                    //if there is a odd node then get that else get any random even node
                    connectedNode = getAConnectedNode(connectedSubGraphKeys);
                    //get a unconnected node, if there is a odd node then get that else get any random even node
                    unconnectedNode = getAUnconnectedNode(connectedSubGraphKeys);
                    super.graph.addEdge(connectedNode,unconnectedNode);
                    edgesConfig.add(new Edge(connectedNode,unconnectedNode));
                    connectedSubGraphKeys.add(subGraphKey);
                    edgesAdded++;
                }
            }
        }
    }

    private void euleriseTheGraph(ArrayList<Edge> edgesConfig) {
        Node oddDegreeNode1 = null;
        Node oddDegreeNode2 = null;
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
                        oddDegreeNode1 = tempNode2;
                    }
                }
            }
            edgesConfig.add(new Edge(oddDegreeNode1,oddDegreeNode2));
            super.graph.addEdge(oddDegreeNode1,oddDegreeNode2);
            oddDegreeNode1 = null;
            oddDegreeNode2 = null;
        }
    }

    private Node getAConnectedNode(HashSet<String> connectedSubGraphKeys) {
        Iterator keysIterator = connectedSubGraphKeys.iterator();
        Node[] connectedNodeArray;
        while(keysIterator.hasNext()) {
            connectedNodeArray = super.subGraphs.get(keysIterator.next());
            Node node = null;
            for(int i=0; i<connectedNodeArray.length; i++) {
                node = connectedNodeArray[i];
                if(super.graph.degree(node)%2!=0) {
                    return node;
                }
            }
            return node;
        }
        return null;
    }

    private Node getAUnconnectedNode(HashSet<String> connectedSubGraphKeys) {
        Node[] unconnectedNodeArray = null;
        Iterator subGraphsIterator = super.subGraphs.keySet().iterator();
        String keyTemp;
        while(subGraphsIterator.hasNext()) {
            keyTemp = (String) subGraphsIterator.next();
            if(!connectedSubGraphKeys.contains(keyTemp)) {
                unconnectedNodeArray = super.subGraphs.get(keyTemp);
            }
        }

        Node node = null;
        for(int i=0; i<unconnectedNodeArray.length; i++) {
            node = unconnectedNodeArray[i];
            if(super.graph.degree(node)%2!=0) {
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
}
