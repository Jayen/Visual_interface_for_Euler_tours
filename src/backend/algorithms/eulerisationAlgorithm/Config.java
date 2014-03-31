package backend.algorithms.eulerisationAlgorithm;

import backend.algorithms.Heuristics;
import backend.internalgraph.Edge;
import backend.internalgraph.Graph;

import java.util.ArrayList;
import java.util.Random;

/**
 * User: jayen
 * Date: 31/03/14, Time: 13:08
 */
public class Config implements Configuration {

    private Graph graph;
    private double cost;
    private ArrayList<Edge> edgesConfig;
    private Random random;


    public Config(ArrayList<Edge> edgesConfig,Graph graph) {
        this.edgesConfig = edgesConfig;
        this.graph = graph;
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
