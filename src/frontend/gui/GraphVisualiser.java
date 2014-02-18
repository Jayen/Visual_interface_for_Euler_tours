package frontend.gui;

import backend.internalgraph.Graph;
import backend.internalgraph.LocationFixedSparseGraph;
import backend.internalgraph.VertexLabeller;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.decorators.AbstractEdgeShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.TransformerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Map;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class GraphVisualiser {

    private Graph graph;
    private VisualizationViewer<String,String> visualizationServer;
    private Layout<String,String> graphLayout;
    private PluggableGraphMouse graphMouse;
    private AppGUI appGUI;
    private boolean firstVisualisation;
    private Map<String,Point2D> vertexLocationsMap;
    private Transformer<String,Point2D> vertexLocations;

    public GraphVisualiser(AppGUI appGUI) {
        this.appGUI = appGUI;
        firstVisualisation = true;
    }

    private void initialise(LocationFixedSparseGraph<String, String> graph) {
//        runJB.putClientProperty("graph",graph);
        this.graph = graph;
        vertexLocationsMap = graph.getVertexLocations();
        vertexLocations = TransformerUtils.mapTransformer(vertexLocationsMap);

        if(graphLayout==null) {
            graphLayout = new StaticLayout<String, String>(graph, vertexLocations);
        }
//        else {
//            graphLayout.setGraph(graph);
//            graphLayout.setInitializer(vertexLocations);
//        }
        if(visualizationServer==null) {
            visualizationServer = new VisualizationViewer<String, String>(graphLayout);
        }
//        else {
//            visualizationServer.setGraphLayout(graphLayout);
//        }

        visualizationServer.getRenderContext().setVertexLabelTransformer(new VertexLabeller<String>(graph));
//        visualizationServer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
//        visualizationServer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        visualizationServer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<String, String>());
        AbstractEdgeShapeTransformer<String,String> edgeShapeTransformer =
                (AbstractEdgeShapeTransformer<String,String>) visualizationServer.getRenderContext().getEdgeShapeTransformer();
        edgeShapeTransformer.setControlOffsetIncrement(60);

        if(graphMouse==null) {
            graphMouse = new PluggableGraphMouse();
            graphMouse.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1_MASK));
            graphMouse.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f));
            graphMouse.add(new PickingGraphMousePlugin<String,String>());
            visualizationServer.setGraphMouse(graphMouse);
        }

//        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
//        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
//        visualizationServer.setGraphMouse(gm);

        visualizationServer.setBorder(BorderFactory.createLoweredBevelBorder());
        appGUI.add(visualizationServer,BorderLayout.CENTER);
        appGUI.revalidate();
        appGUI.repaint();
    }

    public void drawNewGraph(Graph graph) {
        if(firstVisualisation) {
            initialise(graph);
            firstVisualisation = false;
        }
        else {
            this.graph = graph;
            vertexLocationsMap =  graph.getVertexLocations();
            vertexLocations = TransformerUtils.mapTransformer(vertexLocationsMap);
            graphLayout.setGraph(graph);
            graphLayout.setInitializer(vertexLocations);
            visualizationServer.getRenderContext().setVertexLabelTransformer(new VertexLabeller<String>(graph));
            visualizationServer.setGraphLayout(graphLayout);
            appGUI.revalidate();
            appGUI.repaint();
        }
    }

    public LocationFixedSparseGraph getCurrentGraph() {
        return graph;
    }
}
