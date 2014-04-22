package frontend.gui;

import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import graphView.backend.ViewGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

/**
 * This class manages the actual graph drawing
 * Jayen kumar Jaentilal k1189304
 */
public class GraphVisualiserPanel extends JPanel {

    private Graph graph;
    private AppGUI appGUI;
    public ViewGrid viewGrid;
    public BufferedImage imageBuffer;
    double scaleFactor;
    public int x = 1;
    public int y = 1;

    public GraphVisualiserPanel(AppGUI appGUI) {
        this.appGUI = appGUI;
        scaleFactor = 1.0;
    }

    /**
     * Draw a given graph on the panel
     * @param graph -Graph
     */
    public void drawNewGraph(Graph graph) {
        this.graph = graph;
        viewGrid = new ViewGrid();
        Thread loadNewGraphThread = new Thread() {
            @Override
            public void run() {
                viewGrid.loadNewGraph(GraphVisualiserPanel.this.graph);
                imageBuffer = new BufferedImage(viewGrid.rowLength()*x,viewGrid.colLength()*y,BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = imageBuffer.createGraphics();

                //draw the grid
                for(int row=0; row<viewGrid.rowLength(); row++) {
                    for(int col=0; col<viewGrid.colLength(); col++) {
                        short value = viewGrid.getOccupancyGridValue(row, col);
                        if(value==viewGrid.getOccupancyType("unoccupied").get(0)) {
                            g2d.setColor(Color.WHITE);
                        }
                        else if(value==viewGrid.getOccupancyType("paddingOccupied").get(0)) {
                            g2d.setColor(Color.LIGHT_GRAY);
                        }
                        else if(value==viewGrid.getOccupancyType("node").get(0)) {
                            g2d.setColor(Color.BLUE);
                        }
                        else {
                            g2d.setColor(Color.BLACK);
                        }
                        g2d.fillRect(col * x, row * y, x, y);
                    }
                }

                //draw the nodes
                Iterator<Node> nodeIterator = GraphVisualiserPanel.this.graph.getNodes().iterator();
                g2d.setColor(Color.BLUE);
                Node node;
                while(nodeIterator.hasNext()) {
                    node = nodeIterator.next();
                    FontMetrics fm = g2d.getFontMetrics();
                    Rectangle2D rect = fm.getStringBounds(node.getNodeName(), g2d);

                    g2d.setColor(Color.BLUE);
                    g2d.fillRect((int)node.getX(),
                                 (int)node.getY() - fm.getAscent()+5,
                                 (int) rect.getWidth(),
                                 (int) rect.getHeight());

                    g2d.setColor(Color.WHITE);
                    g2d.drawString(node.getNodeName(),(int)node.getX(),(int)node.getY()+5);
                }
                GraphVisualiserPanel.this.revalidate();
                GraphVisualiserPanel.this.repaint();
                appGUI.clearStatus();
            }
        };
        loadNewGraphThread.start();
    }

    /**
     * Clear the visualisation
     * that has been drawn on the graph
     * i.e reset the graph drawing
     */
    public void clearVisualisedPath() {
        Graphics2D g2d = imageBuffer.createGraphics();
        for(int row=0; row<viewGrid.rowLength(); row++) {
            for(int col=0; col<viewGrid.colLength(); col++) {
                short value = viewGrid.getOccupancyGridValue(row, col);
                if(value==viewGrid.getOccupancyType("unoccupied").get(0)) {
                    g2d.setColor(Color.WHITE);
                }
                else if(value==viewGrid.getOccupancyType("paddingOccupied").get(0)) {
                    g2d.setColor(Color.LIGHT_GRAY);
                }
                else if(value==viewGrid.getOccupancyType("node").get(0)) {
                    g2d.setColor(Color.BLUE);
                }
                else {
                    g2d.setColor(Color.BLACK);
                }
                g2d.fillRect(col * x, row * y, x, y);
            }
        }

        Iterator<Node> nodeIterator = graph.getNodes().iterator();
        g2d.setColor(Color.BLUE);
        Node node;
        while(nodeIterator.hasNext()) {
            node = nodeIterator.next();
            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D rect = fm.getStringBounds(node.getNodeName(), g2d);

            g2d.setColor(Color.BLUE);
            g2d.fillRect((int)node.getX(),
                    (int)node.getY() - fm.getAscent()+5,
                    (int) rect.getWidth(),
                    (int) rect.getHeight());

            g2d.setColor(Color.WHITE);
            g2d.drawString(node.getNodeName(),(int)node.getX(),(int)node.getY()+5);
        }
        GraphVisualiserPanel.this.revalidate();
        GraphVisualiserPanel.this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        if(imageBuffer!=null) {
            int imageWidth = imageBuffer.getWidth();
            int imageHeight = imageBuffer.getHeight();
            double x = (w - scaleFactor * imageWidth)/2;
            double y = (h - scaleFactor * imageHeight)/2;
            AffineTransform at = AffineTransform.getTranslateInstance(x,y);
            at.scale(scaleFactor, scaleFactor);
            g2d.drawRenderedImage(imageBuffer, at);
        }
    }

    public Dimension getPreferredSize() {
        if(imageBuffer!=null) {
            int w = (int)(scaleFactor * imageBuffer.getWidth());
            int h = (int)(scaleFactor * imageBuffer.getHeight());
            return new Dimension(w, h);
        }
        return new Dimension(250,250);
    }

    public Graph getCurrentGraph() {
        return graph;
    }

    public void incrementScaleFactor() {
        scaleFactor++;
        GraphVisualiserPanel.this.revalidate();
        GraphVisualiserPanel.this.repaint();
    }

    public void decrementScaleFactor() {
        scaleFactor--;
        GraphVisualiserPanel.this.revalidate();
        GraphVisualiserPanel.this.repaint();
    }

    public double getScaleFactor() {
        return scaleFactor;
    }
}
