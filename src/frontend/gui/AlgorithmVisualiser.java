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
 * This class sets up the visualisation of
 * algorithms. The visualisation is done on a
 * separate thread to not block the main thread
 * @author Jayen kumar Jaentilal k1189304
 */
public class AlgorithmVisualiser {

    private static List<Node> nodePathList;
    private static Thread AlgorithmVisualisationThread;
    private static boolean  paused = false;
    private static GraphVisualiserPanel graphVisualiserPanel;
    int currentNodeI = 1;
    private static ArrayList<Short> visualisedShortValues = new ArrayList<Short>();

    public AlgorithmVisualiser(final GraphVisualiserPanel graphVisualiserPanel, final EulerTourAlgorithm eulerTourAlgorithm) {
        AlgorithmVisualiser.graphVisualiserPanel = graphVisualiserPanel;
        if(eulerTourAlgorithm instanceof FleurysAlgorithm) {
            nodePathList = new ArrayList<Node>();
        }
        else if(eulerTourAlgorithm instanceof HierholzersAlgorithm) {
            nodePathList = new LinkedList<Node>();
        }

      AlgorithmVisualisationThread = new Thread() {
            @Override
            public void run() {
                nodePathList = eulerTourAlgorithm.getEulerTour();
                while(!this.isInterrupted()) {
                    if(!paused) {
                        if(currentNodeI < nodePathList.size()) {
                            AlgorithmVisualiser.visualiseNextAnimated(nodePathList.get(currentNodeI - 1), nodePathList.get(currentNodeI));
                            currentNodeI++;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            if(currentNodeI==nodePathList.size()) {
                                currentNodeI--;
                                break;
                            }
                        }
                    }
                    else {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        };
        AlgorithmVisualisationThread.start();
    }

    /**
     * Visualise the next edge between
     * 2 nodes with animation
     * @param node1 -the starting node
     * @param node2 -the next node
     */
    public static void visualiseNextAnimated(Node node1, Node node2) {
        short edgeValue = getEdgeValueToTraverse(node1,node2);
        visualisedShortValues.add(edgeValue);
        animateEdge(node1,node2,edgeValue,true);
    }

    /**
     * Animate the edge between 2 nodes
     * @param node1 -the starting node
     * @param node2 -the next node
     * @param edgeValue -edgeValue to animate
     * @param forward -indication for animating forward or backwards
     */
    private static void animateEdge(Node node1,Node node2,short edgeValue,boolean forward) {
        PathRouter edgePathFinder = new PathRouter((int)node1.getY(),(int)node1.getX(),
                (int)node2.getY(),(int)node2.getX(),
                new short[]{edgeValue},graphVisualiserPanel.viewGrid);

        ArrayList<GridCell> path = (ArrayList<GridCell>) edgePathFinder.getPath();

        Graphics2D g2d = graphVisualiserPanel.imageBuffer.createGraphics();
        if(forward) {
            g2d.setColor(Color.GREEN);
        }
        else {
            g2d.setColor(Color.BLACK);
        }
        for(int i=path.size()-1; i>=0; i--) {
            g2d.fillRect(path.get(i).getCol() * graphVisualiserPanel.x, path.get(i).getRow() * graphVisualiserPanel.y,
                    graphVisualiserPanel.x,graphVisualiserPanel.y);
            if(i==path.size()/2) {
                graphVisualiserPanel.revalidate();
                graphVisualiserPanel.repaint();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        graphVisualiserPanel.revalidate();
        graphVisualiserPanel.repaint();
    }

    /**
     * Get the edge value to traverse with animation
     * for the given nodes
     * @param node1 -the starting node
     * @param node2 -the next node
     * @return short -edge value
     */
    private static short getEdgeValueToTraverse(Node node1,Node node2) {
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
        return edgeValue;
    }

    /**
     * This methods clears the current visualisation
     * and ends the visualisation thread.
     */
    public void resetVisualisation() {
        graphVisualiserPanel.clearVisualisedPath();
        this.endVisualisationThread();
    }

    public void undoStep() {
        paused = true;
        if(currentNodeI>0 && currentNodeI<nodePathList.size()) {
            currentNodeI--;
            Node node1 = nodePathList.get(currentNodeI);
            Node node2 = nodePathList.get(currentNodeI + 1);
            short edgeValue = getEdgeValueToTraverse(node1,node2);
            visualisedShortValues.remove(new Short(edgeValue));
            animateEdge(node1,node2,edgeValue,false);
        }
    }

    public void nextStep() {
        paused = true;
        if(currentNodeI>=0 && currentNodeI<nodePathList.size()-1) {
            currentNodeI++;
            Node node1 = nodePathList.get(currentNodeI-1);
            Node node2 = nodePathList.get(currentNodeI);
            short edgeValue = getEdgeValueToTraverse(node1,node2);
            visualisedShortValues.add(edgeValue);
            animateEdge(node1,node2,edgeValue,true);
        }
    }

    public void pausePlay() {
        if(paused) {
            paused = false;
        }
        else {
            paused = true;
        }
    }

    public void endVisualisationThread() {
        AlgorithmVisualisationThread.interrupt();
    }
}
