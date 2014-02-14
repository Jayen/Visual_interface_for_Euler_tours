package backend.internalgraph;

import java.util.*;

/**
* This class represents the graph internally
 * represented as a adjacency map
 * each vertex in the graph must have a unique name
 * @author Jayen kumar Jaentilal k1189304
*/

public class Graph {

    private Map<Vertex,Set<Edge>> vertices;

    public Graph() {
        vertices = new HashMap<Vertex, Set<Edge>>();
    }

    public void addVertex(Vertex vertex) {
        if(vertex==null) {
            throw new IllegalArgumentException("vertex cannot be null");
        }
        if(!vertices.containsValue(vertex)) {
            vertices.put(vertex,new HashSet<Edge>());
        }
    }

    public void removeVertex(Vertex vertex) {
        Set<Edge> incidentEdges = vertices.get(vertex);
        Iterator edgesIterator = incidentEdges.iterator();
        Vertex oppositeVertex;
        Edge edge;
        while(edgesIterator.hasNext()) {
            edge = (Edge)edgesIterator.next();
            oppositeVertex = edge.getOpposite(vertex);
            vertices.get(oppositeVertex).remove(edge);
        }
        vertices.remove(vertex);
    }

    private void removeEdges(Edge edge) {
        Vertex vertex = edge.getFirstVertex();
        vertices.get(vertex).remove(edge);
        vertex = edge.getSecondVertex();
        vertices.get(vertex).remove(edge);
    }

    public void addEdge(Edge edge) {
        HashSet<Edge> edges = (HashSet<Edge>) vertices.get(edge.getFirstVertex());
        if(edges==null) {
            edges = new HashSet<Edge>();
            edges.add(edge);
        }
        else {
            edges.add(edge);
        }
        edges = (HashSet<Edge>) vertices.get(edge.getSecondVertex());
        if(edges==null) {
            edges = new HashSet<Edge>();
            edges.add(edge);
        }
        else {
            edges.add(edge);
        }
    }

   public Collection<Edge> getIncidentEdges(Vertex vertex) {
       return vertices.get(vertex);
   }



}
