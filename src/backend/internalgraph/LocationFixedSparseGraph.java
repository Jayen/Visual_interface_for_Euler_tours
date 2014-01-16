package backend.internalgraph;

import edu.uci.ics.jung.graph.SparseMultigraph;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 *@author Jayen kumar Jaentilal k1189304
 */
public class LocationFixedSparseGraph<V,E> extends SparseMultigraph {

    private Map<V,Point2D> vertexLocations;

    public LocationFixedSparseGraph() {
        super();
        vertexLocations = new HashMap<V, Point2D>();
    }


    public void addVertexLocation(V vertex,Point2D location) {
            //noinspection unchecked
        if(this.containsVertex(vertex)) {
            vertexLocations.put(vertex,location);
        }
    }

    public Map<V,Point2D> getVertexLocations() {
        return vertexLocations;
    }
}
