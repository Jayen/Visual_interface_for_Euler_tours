package frontend.gui;

import backend.internalgraph.Graph;
import backend.internalgraph.LocationFixedSparseGraph;
import backend.internalgraph.Node;
import backend.internalgraph.VertexLabeller;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.decorators.AbstractEdgeShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import graphView.model.ViewGrid;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.TransformerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class GraphVisualiser extends JPanel{

    private Graph graph;
    private AppGUI appGUI;
    private ViewGrid viewGrid;
    private BufferedImage imageBuffer;
    private int x = 1;
    private int y = 1;

    public GraphVisualiser(AppGUI appGUI) {
        this.appGUI = appGUI;
    }

    public void drawNewGraph(Graph graph) {
        this.graph = graph;
        viewGrid = new ViewGrid();
        viewGrid.loadNewGraph(graph);
//        viewGrid.printGrid();
        imageBuffer = new BufferedImage(viewGrid.rowLength()*x,viewGrid.colLength()*y,BufferedImage.TYPE_INT_RGB);
        Graphics g = imageBuffer.createGraphics();
        for(int row=0; row<viewGrid.rowLength(); row++) {
            for(int col=0; col<viewGrid.colLength(); col++) {
                byte value = viewGrid.getOccupancyGridValue(row,col);
                if(value==viewGrid.getOccupancyType("unoccupied")) {
                    g.setColor(Color.WHITE);
                }
                else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(col * x, row * y, x, y);
            }
        }
        Iterator<Node> nodeIterator = graph.getNodes().iterator();
        g.setColor(Color.RED);
        Node node;
        while(nodeIterator.hasNext()) {
            node = nodeIterator.next();
            g.fillRect((int)node.getLocation().getX(),(int)node.getLocation().getY(),20,20);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imageBuffer,0,0,null);
    }

    public Graph getCurrentGraph() {
        return graph;
    }
}
