package frontend.gui;

import backend.internalgraph.LocationFixedSparseGraph;
import backend.internalgraph.VertexLabeller;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.AbstractEdgeShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.TransformerUtils;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Map;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class GraphVisualiser {

    private LocationFixedSparseGraph<String,String> graph;
    private VisualizationViewer<String,String> visualizationServer;
    private Layout<String,String> graphLayout;
    private PluggableGraphMouse graphMouse;
    private AppGUI appGUI;
    private boolean firstVisualisation;

    public GraphVisualiser(AppGUI appGUI) {
        this.appGUI = appGUI;
        firstVisualisation = false;
    }

    private void createInitialView(LocationFixedSparseGraph<String, String> graph) {
//        runJB.putClientProperty("graph",graph);
        Map<String,Point2D> vertexLocationsMap = graph.getVertexLocations();
        Transformer<String, Point2D> vertexLocations = TransformerUtils.mapTransformer(vertexLocationsMap);

        if(graphLayout==null) {
            graphLayout = new StaticLayout<String, String>(graph,vertexLocations);
        }
        else {
            graphLayout.setGraph(graph);
            graphLayout.setInitializer(vertexLocations);
        }
        if(visualizationServer==null) {
            visualizationServer = new VisualizationViewer<String, String>(graphLayout);
        }
        else {
            visualizationServer.setGraphLayout(graphLayout);
        }

        visualizationServer.getRenderContext().setVertexLabelTransformer(new VertexLabeller<String>(graph));
//        visualizationServer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
//        visualizationServer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        visualizationServer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<String, String>());
        AbstractEdgeShapeTransformer<String,String> edgeShapeTransformer =
                (AbstractEdgeShapeTransformer<String,String>) visualizationServer.getRenderContext().getEdgeShapeTransformer();
        edgeShapeTransformer.setControlOffsetIncrement(35);

        if(graphMouse==null) {
            graphMouse = new PluggableGraphMouse();
            graphMouse.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1_MASK));
            graphMouse.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f));
            visualizationServer.setGraphMouse(graphMouse);
        }
        appGUI.add(visualizationServer);
        appGUI.revalidate();
        appGUI.repaint();
    }
}
