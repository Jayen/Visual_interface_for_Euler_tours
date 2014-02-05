package backend.algorithms;

import backend.fileparser.GraphParser;
import backend.fileparser.IncorrectFileFormatException;
import backend.internalgraph.LocationFixedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import frontend.gui.OpenFileActionListener;

import java.io.IOException;
import java.util.*;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class FleurysAlgorithm<V,E> implements EulerTourAlgorithm {

    private LocationFixedSparseGraph graph;
    private LocationFixedSparseGraph reducedGraph;
    private Map<V, Integer> order;//the order the depth first search will visit the vertices
    private HashMap<V, Integer> deepestEdgeLink;//the deepest link for each vertex (back edge)
    private int counter;
    private ArrayList pathList;
    private ConnectivityChecker connectivityChecker;

    @Override
    public List getEulerTour() {
        graph = null;
        try {
            graph = GraphParser.createGraphFromFile(OpenFileActionListener.currentFile);
        } catch (IncorrectFileFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pathList = new ArrayList(graph.getEdgeCount());
        connectivityChecker = new ConnectivityChecker(graph);
        V currentVertex = (V) graph.getVertices().iterator().next();
        System.out.println(currentVertex);
        Iterator incidentEdges = graph.getIncidentEdges(currentVertex).iterator();
        E edge = null;
        int edgesCount = graph.getEdgeCount();
        for(int i=0; i<edgesCount; i++) {
            while(incidentEdges.hasNext()) {
                edge = (E) incidentEdges.next();
                if(!pathList.contains(edge)) {//make sure we haven't already travelled the edge
                    if (!this.isBridge(edge)) {
                        pathList.add(edge);
                        break;
                    }
                    else if(!incidentEdges.hasNext()) {
                        pathList.add(edge);
                        break;
                    }
                }
            }
            currentVertex = (V) graph.getOpposite(currentVertex,edge);
            System.out.println(currentVertex);
            graph.removeEdge(edge);
//            System.out.println(edge);
            incidentEdges = graph.getIncidentEdges(currentVertex).iterator();
        }
        return pathList;
    }

    private boolean isBridge(E edge) {
        V node1 = (V) graph.getEndpoints(edge).getFirst();
        V node2 = (V) graph.getEndpoints(edge).getSecond();
        graph.removeEdge(edge);
        boolean isConnected = connectivityChecker.depthFirstSearch(node1);
        graph.addEdge(edge,new Pair(node1,node2), EdgeType.UNDIRECTED);
        return !isConnected;
    }

//    public boolean isBridge(E edge) {
//        counter = 0;
//        order = new HashMap<V,Integer>(graph.getVertexCount());
//        deepestEdgeLink = new HashMap<V,Integer>(graph.getVertexCount());
//        V sourceVertex = (V) graph.getVertices().iterator().next();
//        Collection connectedVertices = graph.getNeighbors(sourceVertex);
//        Iterator iterator = connectedVertices.iterator();
//        V vertex;
//        while(iterator.hasNext()) {
//            vertex = (V) iterator.next();
//            if(order.get(vertex)==null) {//if null we haven't carried out a dfs
//                if( dfsBridgeCheck(edge, sourceVertex, vertex)){
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private boolean dfsBridgeCheck(E edge, V sourceVertex, V vertex) {
//        order.put(vertex,counter);
//        deepestEdgeLink.put(vertex,order.get(vertex));
//        Collection connectedVertices = graph.getNeighbors(vertex);
//        Iterator iterator = connectedVertices.iterator();
//        V adjVertex;
//        while(iterator.hasNext()) {
//            adjVertex = (V) iterator.next();
//            if(!pathList.contains(graph.findEdge(vertex,adjVertex))) {
//                if(order.get(adjVertex)==null) {
//                    dfsBridgeCheck(edge,vertex,adjVertex);
//                    deepestEdgeLink.put(vertex,Math.min(deepestEdgeLink.get(vertex),deepestEdgeLink.get(adjVertex)));
//                    if(deepestEdgeLink.get(adjVertex).equals(order.get(adjVertex))) {
//                        if(graph.findEdge(vertex,adjVertex).equals(edge)) {
////                            System.out.println(vertex +" "+ adjVertex + " is a bridge");
//                            return true;
//                        }
//                    }
//                }
//                else if(sourceVertex!=adjVertex) {
//                    deepestEdgeLink.put(vertex,order.get(adjVertex));
//                }
//            }
//        }
//        return false;
//    }
}
