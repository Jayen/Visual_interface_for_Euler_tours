package frontend.gui;

import backend.algorithms.EulerTourAlgorithm;
import backend.algorithms.FleurysAlgorithm;
import backend.algorithms.HierholzersAlgorithm;
import backend.internalgraph.Node;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: Jayen
 * Date: 01/03/14, Time: 12:30
 */
public class AlgorithmVisualiser {

    private static List<Node> nodePathList;
    private static AtomicInteger nextValidIndex;
    private static Thread algorithmThread;
    private static Thread visulisationThread;
    private static boolean algorithmFinished = false;

    public AlgorithmVisualiser(final GraphVisualiserPanel graphVisualiserPanel, EulerTourAlgorithm eulerTourAlgorithm) {
        nextValidIndex = new AtomicInteger(0);
        if(eulerTourAlgorithm instanceof FleurysAlgorithm) {
            nodePathList = new ArrayList<Node>();
        }
        else if(eulerTourAlgorithm instanceof HierholzersAlgorithm) {
            nodePathList = new LinkedList<Node>();
        }
        eulerTourAlgorithm.setNodePathList(nodePathList);
        algorithmThread = new Thread(eulerTourAlgorithm);
        algorithmThread.start();
        visulisationThread = new Thread() {
            int currentNodeI = 1;
            @Override
            public void run() {
                while(!this.isInterrupted()) {
                    if(currentNodeI < nextValidIndex.get()) {
                        graphVisualiserPanel.visualiseEdge(nodePathList.get(currentNodeI - 1), nodePathList.get(currentNodeI));
                        currentNodeI++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                        if(algorithmFinished && currentNodeI==nodePathList.size()) {
                            break;
                        }
                    }
                }
            }
        };
        visulisationThread.start();
    }

    public void endVisulisationThread() {
        visulisationThread.interrupt();
    }

    public static void incrementNextValidIndex() {
        nextValidIndex.addAndGet(1);
    }

    public static void setAlgorithmFinished(boolean algorithmFinished) {
       AlgorithmVisualiser.algorithmFinished = algorithmFinished;
    }
}
