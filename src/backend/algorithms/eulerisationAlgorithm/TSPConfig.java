package backend.algorithms.eulerisationAlgorithm;

import backend.algorithms.Heuristics;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import frontend.gui.AppGUI;

import java.util.ArrayList;
import java.util.Random;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class TSPConfig implements Configuration {

    private double cost;
    private ArrayList<Node> pathNodes;
    private Random random;
    private int randomIndex1;
    private int randomIndex2;
    private Node randomNode1;
    private Node randomNode2;

    public TSPConfig(ArrayList<Node> eulerPath) {
        this.pathNodes = eulerPath;
        random = new Random();
        computeCost();
    }

    public TSPConfig(TSPConfig config) {
        this.pathNodes = new ArrayList<Node>();
        for(int i=0; i<config.getPathSize(); i++) {
            this.pathNodes.add(config.getConfig().get(i));
        }
        computeCost();
    }

    public double generateNeighbouringConfig() {
        tourImprovementAlgorithm();
        computeCost();
        return cost;

    }

    private void tourImprovementAlgorithm() {
        /*
         Avoid swapping the first and the last node as they are fixed for the tour
         and swapping them will generate a invalid tour
         so we only generate numbers from 1 to path size-2
        */
        randomIndex1 = random.nextInt(pathNodes.size() - 2)+1;
        randomIndex2 = random.nextInt(pathNodes.size() - 2)+1;
        randomNode1 = pathNodes.get(randomIndex1);
        randomNode2 = pathNodes.get(randomIndex2);
        pathNodes.set(randomIndex1, randomNode2);
        pathNodes.set(randomIndex2, randomNode1);
    }

    public void undoLastGeneration() {
        pathNodes.set(randomIndex1, randomNode1);
        pathNodes.set(randomIndex2, randomNode2);
        computeCost();
    }

    public void setEdges(ArrayList<Node> edges) {
        this.pathNodes = edges;
        computeCost();
    }

    public double getCost() {
        return cost;
    }

    public int getPathSize() {
        return pathNodes.size();
    }

    public ArrayList<Node> getConfig() {
        return pathNodes;
    }

    private void computeCost() {
        cost = 0;//reset cost for each calculation
        Node currentNode = pathNodes.get(0);
        Node nextNode = pathNodes.get(1);
        cost = Heuristics.computeEuclideanDistance(currentNode,nextNode);
        currentNode = nextNode;
        for(int i=2; i< pathNodes.size(); i++) {
            nextNode = pathNodes.get(i);
            cost = cost + Heuristics.computeEuclideanDistance(currentNode,nextNode);
            currentNode = nextNode;
        }
    }

    public Graph getGraphWithNewEdges() {
        int counter = 0;
        while(counter<pathNodes.size()-1) {
            AppGUI.graphVisualiserPanel.getCurrentGraph().addEdge(pathNodes.get(counter), pathNodes.get(counter+1));
            counter++;
        }
        return AppGUI.graphVisualiserPanel.getCurrentGraph();
    }
}
