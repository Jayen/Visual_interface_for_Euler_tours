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

    private double cost;
    private ArrayList<Edge> edgesConfig;
    private Random random;
    private HashMap<String,Node[]> subGraphs;
    private int randomIndex1;
    private int randomIndex2;
    private Edge randomEdge1;
    private Edge randomEdge2;
    private Edge newEdge1;
    private Edge newEdge2;


    public Config(ArrayList<Edge> edgesConfig,HashMap<String,Node[]> subGraphs) {
        this.edgesConfig = edgesConfig;
        random = new Random();
        this.subGraphs = new HashMap<String, Node[]>();
        Iterator keysIterator = subGraphs.keySet().iterator();
        String key;
        Node[] subGraph;
        Node[] subGraphArray;
        while(keysIterator.hasNext()) {
            key = (String) keysIterator.next();
            subGraphArray = subGraphs.get(key);
            subGraph = new Node[subGraphArray.length];
            for(int i=0; i<subGraphArray.length; i++) {
                subGraph[i]=subGraphArray[i];
            }
            this.subGraphs.put(key, subGraph);
        }
        computeCost();
    }

    public Config(Config config) {
        this.edgesConfig = new ArrayList<Edge>();
        for (int i = 0; i < config.getNumberOfEdgesAdded(); i++) {
            this.edgesConfig.add((Edge) config.getConfig().get(i));
        }
        computeCost();
    }

    @Override
    public double generateNeighbouringConfig() {
        //cannot generate config if there is only 1 edge
        if(edgesConfig.size()!=1) {
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
                changeCommonNode(node);
            }
            else {
                twoOptEdgeSwap();
            }
            computeCost();
        }
        return cost;
    }

    private void changeCommonNode(Node commonNode) {
        /*
         find which sub-graph the commonNode is in
         we only want to change the common node with a node in the same sub-graph
        */
        Iterator subGraphIterator = subGraphs.keySet().iterator();
        Node[] subGraph = null;
        while(subGraphIterator.hasNext()) {
            subGraph = subGraphs.get(subGraphIterator.next());
            for(int i=0; i<subGraph.length; i++) {
                if(subGraph[i].equals(commonNode)) {
                    break;
                }
            }
        }
        if(subGraph.length==1) {
            return;//if only 1 node in the sub-graph then cannot make any changes
        }
        Node node;
        //choose a random valid node to change to
        while(true) {
            node = subGraph[random.nextInt(subGraph.length)];
            if(!node.equals(commonNode)) {
                newEdge1 = new Edge(randomEdge1.getOpposite(commonNode),node);
                newEdge2 = new Edge(randomEdge2.getOpposite(commonNode),node);
                edgesConfig.set(randomIndex1,newEdge1);
                edgesConfig.set(randomIndex2,newEdge2);
                break;
            }
        }
    }

    private void twoOptEdgeSwap() {
        String edge1Node1SubGraph = getContainingSubGraphKey(randomEdge1.getFirstNode());
        String edge1Node2SubGraph = getContainingSubGraphKey(randomEdge1.getSecondNode());

        String edge2Node1SubGraph = getContainingSubGraphKey(randomEdge2.getFirstNode());
        String edge2Node2SubGraph = getContainingSubGraphKey(randomEdge2.getSecondNode());

        //if the edge is in the same sub graph just do
        // a  random swap as connectivity is not affected
        if(edge1Node1SubGraph.equals(edge1Node2SubGraph) ||
                edge2Node1SubGraph.equals(edge2Node2SubGraph)) {
            if(random.nextInt(100)>50) {
                newEdge1 = new Edge(randomEdge1.getFirstNode(),randomEdge2.getFirstNode());
                newEdge2 = new Edge(randomEdge1.getSecondNode(),randomEdge2.getSecondNode());
                edgesConfig.set(randomIndex1,newEdge1);
                edgesConfig.set(randomIndex2,newEdge2);
            }
            else {
                newEdge1 = new Edge(randomEdge1.getFirstNode(),randomEdge2.getSecondNode());
                newEdge2 = new Edge(randomEdge1.getSecondNode(),randomEdge2.getFirstNode());
                edgesConfig.set(randomIndex1,newEdge1);
                edgesConfig.set(randomIndex2,newEdge2);
            }
        }
        else {
            int crossingCount = getEdgesCrossingCount(edge1Node1SubGraph,edge2Node1SubGraph,
                    edge1Node2SubGraph,edge2Node2SubGraph);

            if(crossingCount==2) {
                newEdge1 = new Edge(randomEdge2.getFirstNode(),randomEdge1.getSecondNode());
                newEdge2 = new Edge(randomEdge1.getFirstNode(),randomEdge2.getSecondNode());
                edgesConfig.set(randomIndex1,newEdge1);
                edgesConfig.set(randomIndex2,newEdge2);
                return;
            }

            crossingCount = getEdgesCrossingCount(edge1Node1SubGraph,edge2Node2SubGraph,
                    edge1Node2SubGraph,edge2Node1SubGraph);

            if(crossingCount==2) {
                newEdge1 = new Edge(randomEdge1.getFirstNode(),randomEdge2.getFirstNode());
                newEdge2 = new Edge(randomEdge1.getSecondNode(),randomEdge2.getSecondNode());
                edgesConfig.set(randomIndex1,newEdge1);
                edgesConfig.set(randomIndex2,newEdge2);
                return;
            }
        }
    }

    private int getEdgesCrossingCount(String set1Key1, String set1Key2, String set2Key1, String set2Key2) {
        String edgeKey1;
        String edgeKey2;
        int edgeCrossCount = 0;
        for(Edge edge : edgesConfig) {
            edgeKey1 = getContainingSubGraphKey(edge.getFirstNode());
            edgeKey2 = getContainingSubGraphKey(edge.getSecondNode());
            if(edgeKey1.equals(set1Key1) || edgeKey1.equals(set1Key2)) {
                if(edgeKey2.equals(set2Key1) || edgeKey2.equals(set2Key2)) {
                    edgeCrossCount++;
                }
            }
            else if(edgeKey1.equals(set2Key1) || edgeKey1.equals(set2Key2)) {
                if(edgeKey2.equals(set1Key1) || edgeKey2.equals(set1Key2)) {
                    edgeCrossCount++;
                }
            }
        }
        return edgeCrossCount;
    }

    private String getContainingSubGraphKey(Node nodeToSearch) {
        Iterator subGraphKeysIterator = subGraphs.keySet().iterator();
        String key;
        Node[] subGraph;
        while(subGraphKeysIterator.hasNext()) {
            key = (String) subGraphKeysIterator.next();
            subGraph = subGraphs.get(key);
            for(int i=0; i<subGraph.length; i++) {
                if(subGraph[i].equals(nodeToSearch)) {
                    return key;
                }
            }
        }
        return null;
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
        edgesConfig.set(randomIndex1, randomEdge1);
        edgesConfig.set(randomIndex2, randomEdge2);
        computeCost();
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public Graph getGraphWithNewEdges() {
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
}
