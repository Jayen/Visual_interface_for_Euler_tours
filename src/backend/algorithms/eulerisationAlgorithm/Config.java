package backend.algorithms.eulerisationAlgorithm;

import backend.algorithms.Heuristics;
import backend.internalgraph.Edge;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;

import java.util.*;

/**
 * User: jayen
 * Date: 31/03/14, Time: 13:08
 */
public class Config implements Configuration {

    private Graph graph;
    private double cost;
    private ArrayList<Edge> edgesConfig;
    private Random random;
    private HashMap<String,HashSet<Node>> subGraphs;
    private int randomIndex1;
    private int randomIndex2;
    private Edge randomEdge1;
    private Edge randomEdge2;


    public Config(ArrayList<Edge> edgesConfig,Graph graph,HashMap<String,Node[]> subGraphs) {
        this.edgesConfig = edgesConfig;
        this.graph = graph;
        random = new Random();
        subGraphs = new HashMap<String, Node[]>();
        Iterator keysIterator = subGraphs.keySet().iterator();
        String key;
        while(keysIterator.hasNext()) {
            key = (String) keysIterator.next();
            this.subGraphs.put(key, new HashSet<Node>(Arrays.asList(subGraphs.get(key))));
        }
        computeCost();
    }

    public Config(Config config) {
        this.graph = config.getGraph();
        this.edgesConfig = new ArrayList<Edge>();
        for(int i=0; i<config.getNumberOfEdgesAdded(); i++) {
            this.edgesConfig.add(config.getEdges().get(i));
        }
    }

    @Override
    public double generateNeighbouringConfig() {
        randomIndex1 = random.nextInt(edgesConfig.size());
        randomIndex2 = random.nextInt(edgesConfig.size());
        return 0;
    }

    @Override
    public void undoLastGeneration() {

    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public Graph getGraph() {
        return graph;
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

    public ArrayList<Edge> getEdges() {
        return edgesConfig;
    }
}
