package backend.algorithms;

import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import frontend.gui.AlgorithmVisualiser;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that sets up the
 * foundation for the euler tour algorithms
 * Jayen kumar Jaentilal k1189304
 */
public abstract class EulerTourAlgorithm implements Runnable {

    protected Graph graph;
    protected List<Node> nodePathList;
    protected ConnectivityChecker connectivityChecker;

    @Override
    public void run() {
        this.getEulerTour();
        AlgorithmVisualiser.setAlgorithmFinished(true);
    }

    /**
     * Get the euler tour for the current graph
     * @return List<Node> the list of nodes
     * that form a euler tour
     */
    public abstract List getEulerTour();


    public void setNodePathList(List<Node> nodePathList) {
        this.nodePathList = nodePathList;
    }
}
