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
public class Config implements Configuration {

//    private Graph graph;
    private double cost;
    private ArrayList<Edge> edgesConfig;
    private Random random;
    private HashMap<String,HashMap<Integer,Node>> subGraphs;
    private int randomIndex1;
    private int randomIndex2;
    private Edge randomEdge1;
    private Edge randomEdge2;
    private Edge newEdge1;
    private Edge newEdge2;


    public Config(ArrayList<Edge> edgesConfig,Graph graph,HashMap<String,Node[]> subGraphs) {
        this.edgesConfig = edgesConfig;
//        this.graph = graph;
        random = new Random();
        this.subGraphs = new HashMap<String, HashMap<Integer, Node>>();
        Iterator keysIterator = subGraphs.keySet().iterator();
        String key;
        HashMap<Integer,Node> subGraph;
        Node[] subGraphArray;
        while(keysIterator.hasNext()) {
            key = (String) keysIterator.next();
            subGraph = new HashMap<Integer, Node>();
            subGraphArray = subGraphs.get(key);
            for(int i=0; i<subGraphArray.length; i++) {
                subGraph.put(i,subGraphArray[i]);
            }
            this.subGraphs.put(key, subGraph);
        }
        computeCost();
    }

    public Config(Config config) {
//        this.graph = config.getGraph();
        this.edgesConfig = new ArrayList<Edge>();
        for (int i = 0; i < config.getNumberOfEdgesAdded(); i++) {
            this.edgesConfig.add((Edge) config.getConfig().get(i));
        }
        computeCost();
    }

    @Override
    public double generateNeighbouringConfig() {
        randomIndex1 = random.nextInt(edgesConfig.size());
        randomIndex2 = random.nextInt(edgesConfig.size());
        while(randomIndex1==randomIndex2) {
            randomIndex2 = random.nextInt(edgesConfig.size());
        }
        randomEdge1 = edgesConfig.get(randomIndex1);
        randomEdge2 = edgesConfig.get(randomIndex2);


        Node node = getCommonNodeBetweenEdges(randomEdge1,randomEdge2);
        //since there is a common node the best move is to change the common node
        //as changing the other nodes will not make a difference to the config
        if(node!=null) {
            changeCommonNode(randomIndex1,randomIndex2,randomEdge1,randomEdge2,node);
        }
        else {
            twoOptEdgeSwap(randomIndex1,randomIndex2,randomEdge1,randomEdge2);
        }
        computeCost();
        return cost;
    }

    private void changeCommonNode(int edge1Index,int edge2Index,Edge edge1, Edge edge2, Node commonNode) {
        /*
         find which sub-graph the commonNode is in
         we only want to change the common node with a node in the same sub-graph
         that also has even degree
        */
        Iterator subGraphIterator = subGraphs.keySet().iterator();
        HashMap<Integer,Node> subGraph = null;
        while(subGraphIterator.hasNext()) {
            subGraph = subGraphs.get(subGraphIterator.next());
            if(subGraph.containsValue(commonNode)) {
                break;
            }
        }
        if(subGraph.size()==1) {
            return;//if only 1 node in the sub-graph then cannot make any changes
        }
        Node node;
        //choose a random valid node to change to
        while(true) {
            node = subGraph.get(random.nextInt(subGraph.size()));
            if(!node.equals(commonNode) ) {
//                this.graph.removeEdge(edge1.getFirstNode(),edge1.getSecondNode());
//                this.graph.removeEdge(edge2.getFirstNode(),edge2.getSecondNode());
                newEdge1 = new Edge(edge1.getOpposite(commonNode),node);
                newEdge2 = new Edge(edge2.getOpposite(commonNode),node);
                edgesConfig.set(edge1Index,newEdge1);
                edgesConfig.set(edge2Index,newEdge2);
//                this.graph.addEdge(newEdge1.getFirstNode(),newEdge1.getSecondNode());
//                this.graph.addEdge(newEdge2.getFirstNode(),newEdge2.getSecondNode());
                break;
            }
        }
    }

    private void twoOptEdgeSwap(int edge1Index,int edge2Index,Edge edge1, Edge edge2) {
//        this.graph.removeEdge(edge1.getFirstNode(),edge1.getSecondNode());
//        this.graph.removeEdge(edge2.getFirstNode(),edge2.getSecondNode());
        newEdge1 = new Edge(edge2.getFirstNode(),edge1.getSecondNode());
        newEdge2 = new Edge(edge1.getFirstNode(),edge2.getSecondNode());
        edgesConfig.set(edge1Index,newEdge1);
        edgesConfig.set(edge2Index,newEdge2);
//        this.graph.addEdge(newEdge1.getFirstNode(),newEdge1.getSecondNode());
//        this.graph.addEdge(newEdge2.getFirstNode(),newEdge2.getSecondNode());
    }

    private Node getCommonNodeBetweenEdges(Edge edge1, Edge edge2) {
        if(edge1.contains(edge2.getFirstNode())) {
            return edge2.getFirstNode();
        }
        else if(edge1.contains(edge2.getSecondNode())) {
            return edge2.getSecondNode();
        }
        return null;
    }

    @Override
    public void undoLastGeneration() {
//        this.graph.removeEdge(newEdge1.getFirstNode(),newEdge1.getSecondNode());
//        this.graph.removeEdge(newEdge2.getSecondNode(),newEdge2.getSecondNode());
        edgesConfig.set(randomIndex1,randomEdge1);
        edgesConfig.set(randomIndex2,randomEdge2);
//        this.graph.addEdge(randomEdge1.getFirstNode(),randomEdge1.getSecondNode());
//        this.graph.addEdge(randomEdge2.getFirstNode(),randomEdge2.getSecondNode());
        computeCost();
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public Graph getGraphWithNewEdges() {
        System.out.println(edgesConfig.size()+"edges added");
        for(Edge edge : edgesConfig) {
            AppGUI.graphVisualiserPanel.getCurrentGraph().addEdge(edge.getFirstNode(), edge.getSecondNode());
        }
        return AppGUI.graphVisualiserPanel.getCurrentGraph();
    }

    @Override
    public ArrayList getConfig() {
        return edgesConfig;
    }

    private void computeCost() {
        cost = 0;//reset cost for each calculation
        for(Edge edge:edgesConfig) {
            cost = cost + Heuristics.computeEuclideanDistance(edge.getFirstNode(),edge.getSecondNode());
        }
    }

    public int getNumberOfEdgesAdded() {
        return edgesConfig.size();
    }

//    public Graph getGraph() {
//        return graph;
//    }
}
