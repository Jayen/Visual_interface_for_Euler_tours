package frontend.gui;

import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import graphView.model.ViewGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Iterator;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class GraphVisualiser extends JPanel {

    private Graph graph;
    private AppGUI appGUI;
    private ViewGrid viewGrid;
    private BufferedImage imageBuffer;
    double scaleFactor;
    private int x = 1;
    private int y = 1;

    public GraphVisualiser(AppGUI appGUI) {
        this.appGUI = appGUI;
        scaleFactor = 1.0;
    }

    public void drawNewGraph(Graph graph) {
        this.graph = graph;
        viewGrid = new ViewGrid();
        viewGrid.loadNewGraph(graph);
//        viewGrid.printGrid();
        imageBuffer = new BufferedImage(viewGrid.rowLength()*x,viewGrid.colLength()*y,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imageBuffer.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.drawLine(200,400,400,450);
        for(int row=0; row<viewGrid.rowLength(); row++) {
            for(int col=0; col<viewGrid.colLength(); col++) {
                byte value = viewGrid.getOccupancyGridValue(row,col);
                if(value==viewGrid.getOccupancyType("unoccupied")) {
                    g2d.setColor(Color.WHITE);
                }
                else if(value==viewGrid.getOccupancyType("paddingOccupied")) {
                    g2d.setColor(Color.cyan);
                }
                else if(value==viewGrid.getOccupancyType("node")) {
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
            g2d.fillRect((int)node.getLocation().getX()-2,(int)node.getLocation().getY()-2,5,5);
        }
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

    public Dimension getPreferredSize()
    {
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
        revalidate();
        repaint();
    }

    public void decrementScaleFactor() {
        scaleFactor--;
        revalidate();
        repaint();
    }

    public double getScaleFactor() {
        return scaleFactor;
    }
}
