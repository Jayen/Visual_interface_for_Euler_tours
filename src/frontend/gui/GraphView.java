package frontend.gui;

import backend.internalgraph.LocationFixedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

import java.awt.*;
import java.util.HashMap;

/**
 *@author Jayen kumar Jaentilal k1189304
 */
public class GraphView {

    private LocationFixedSparseGraph<String,String> graph;

    public GraphView() {
        graph = new LocationFixedSparseGraph<String, String>();

        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");

        graph.addEdge("Edge AB","A","B");
        graph.addEdge("Edge AC","A","C");
        graph.addEdge("Edge AD","A","D");
        graph.addEdge("Edge CD","C","D");

        graph.addVertexLocation("A",new Point(100,100));
        graph.addVertexLocation("A",new Point(200,100));
        graph.addVertexLocation("A",new Point(100,200));
        graph.addVertexLocation("A",new Point(200,200));
    }

    public LocationFixedSparseGraph<String, String> getGraph() {
        return graph;
    }
}
