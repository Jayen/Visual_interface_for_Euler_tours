package frontend.gui;

import backend.algorithms.eulerTourAlgorithms.EulerTourAlgorithm;
import backend.algorithms.eulerTourAlgorithms.FleurysAlgorithm;
import backend.algorithms.eulerTourAlgorithms.HierholzersAlgorithm;
import backend.internalgraph.Node;
import graphView.backend.GridCell;
import graphView.backend.PathRouter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class sets up the visualisation of
 * algorithms. The visualisation is done on a
 * separate thread to not block the main thread
 * @author Jayen kumar Jaentilal k1189304
 */
public class AlgorithmVisualiser {

    private static List<Node> nodePathList;
    private static Thread AlgorithmVisualisationThread;
    private boolean  paused = false;
    private static GraphVisualiserPanel graphVisualiserPanel;
    private static ArrayList<Short> visualisedShortValues = new ArrayList<Short>();
    private int currentNodeI = 1;

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
                visualisedShortValues = new ArrayList<Short>();
                nodePathList = eulerTourAlgorithm.getEulerTour(graphVisualiserPanel.getCurrentGraph());
                if(nodePathList!=null) {
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
        short edgeValue = getEdgeValueToTraverse(node1,node2,true);
        visualisedShortValues.add(edgeValue);
        animateEdge(node1,node2,edgeValue,true);
    }

    /**
     * Animate the edge between 2 nodes
     * @param node1 -the starting node
     * @param node2 -the next node
     * @param edgeValue -edgeValue to animate
     * @param goingForward -indication for animating forward or backwards
     */
    private static void animateEdge(Node node1,Node node2,short edgeValue,boolean goingForward) {
        PathRouter edgePathFinder = new PathRouter((int)node1.getY(),(int)node1.getX(),
                (int)node2.getY(),(int)node2.getX(),
                new short[]{edgeValue},graphVisualiserPanel.viewGrid);

        ArrayList<GridCell> path = (ArrayList<GridCell>) edgePathFinder.getPath();

        Graphics2D g2d = graphVisualiserPanel.imageBuffer.createGraphics();
        if(goingForward) {
            g2d.setColor(Color.YELLOW);
        }
        else {
            g2d.setColor(Color.BLACK);
        }
        try {
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
        }
        catch(NullPointerException e) {
            JOptionPane.showMessageDialog(null, "<html>The tour is "+nodePathList.toString()+"<br>" +
                    "however due to edge crossing program is unable to visualise.</html>", "Unable to visualise", JOptionPane.INFORMATION_MESSAGE);
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
    private static short getEdgeValueToTraverse(Node node1,Node node2,boolean goingForward) {
        ArrayList<Short> edgeValues = graphVisualiserPanel.viewGrid.getEdgeValues(node1, node2);
        short edgeValue = -1;
        if(edgeValues.size()==1) {
            edgeValue = edgeValues.get(0);
        }
        else {
            //only traverse edges values not traversed (needed since there may be multiple edges between 2 nodes)
            if(goingForward) {
                for(short value:edgeValues) {
                    if(!visualisedShortValues.contains(value)) {
                        edgeValue = value;
                    }
                }
            }
            else {
                for(short value:edgeValues) {
                    if(visualisedShortValues.contains(value)) {
                        edgeValue = value;
                    }
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
            short edgeValue = getEdgeValueToTraverse(node1,node2,false);
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
            short edgeValue = getEdgeValueToTraverse(node1,node2,true);
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
