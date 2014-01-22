package backend.internalgraph;

import org.apache.commons.collections15.Transformer;

import java.awt.geom.Point2D;

/**
 *@author Jayen kumar Jaentilal k1189304
 */
public class VertexLabeller<V> implements Transformer<V,String>{

    private LocationFixedSparseGraph<V,String> graph;

    public VertexLabeller(LocationFixedSparseGraph<V,String> graph) {
        this.graph = graph;
    }

    @Override
    public String transform(V v) {
        Point2D point = graph.getVertexLocations().get(v);
        return v.toString()+" ("+point.getX()+","+point.getY()+")";
    }
}
