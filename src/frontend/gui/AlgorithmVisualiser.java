package frontend.gui;

import backend.algorithms.EulerTourAlgorithm;
import backend.algorithms.FleurysAlgorithm;
import backend.algorithms.HierholzersAlgorithm;
import backend.internalgraph.Node;
import graphView.model.GridCell;
import graphView.model.PathRouter;

import java.awt.*;
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
            ArrayList<Short> visualisedShortValues = new ArrayList<Short>();
            @Override
            public void run() {
                while(!this.isInterrupted()) {
                    if(currentNodeI < nextValidIndex.get()) {
                        this.visualiseEdge(nodePathList.get(currentNodeI - 1), nodePathList.get(currentNodeI));
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

            public void visualiseEdge(Node node1, Node node2) {
                ArrayList<Short> edgeValues = graphVisualiserPanel.viewGrid.getEdgeValue(node1, node2);
                short edgeValue = -1;
                if(edgeValues.size()==1) {
                    edgeValue = edgeValues.get(0);
                }
                else {
                    for(short value:edgeValues) {
                        if(!visualisedShortValues.contains(value)) {
                            edgeValue = value;
                        }
                    }
                }
                visualisedShortValues.add(edgeValue);
                PathRouter edgePathFinder = new PathRouter((int)node1.getY(),(int)node1.getX(),
                        (int)node2.getY(),(int)node2.getX(),
                        new short[]{edgeValue},graphVisualiserPanel.viewGrid);

                ArrayList<GridCell> path = (ArrayList<GridCell>) edgePathFinder.getPath();

                Graphics2D g2d = graphVisualiserPanel.imageBuffer.createGraphics();
                g2d.setColor(Color.GREEN);
                for(int i=path.size()-1; i>=0; i--) {
                    g2d.fillRect(path.get(i).getCol() * graphVisualiserPanel.x, path.get(i).getRow() * graphVisualiserPanel.y,
                                 graphVisualiserPanel.x,graphVisualiserPanel.y);
                    if(i==path.size()/2) {
                        graphVisualiserPanel.revalidate();
                        graphVisualiserPanel.repaint();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                graphVisualiserPanel.revalidate();
                graphVisualiserPanel.repaint();
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
