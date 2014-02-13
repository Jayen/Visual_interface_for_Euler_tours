package backend.algorithms;

import backend.fileparser.GraphParser;
import backend.fileparser.IncorrectFileFormatException;
import backend.internalgraph.LocationFixedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import frontend.gui.AppGUI;

import java.io.IOException;
import java.util.*;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class FleurysAlgorithm<V,E> implements EulerTourAlgorithm {

    private LocationFixedSparseGraph graph;
    private ArrayList vertexPathList;
    private ArrayList edgePathList;
    private ConnectivityChecker connectivityChecker;

    @Override
    public List getEulerTour() {
        graph = null;
        try {
            graph = GraphParser.createGraphFromFile(AppGUI.currentFile);
        } catch (IncorrectFileFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(EulerTourChecker.hasEulerTour(graph)) {
            connectivityChecker = new ConnectivityChecker(graph);
            vertexPathList = new ArrayList(graph.getVertexCount());
            edgePathList = new ArrayList(graph.getEdgeCount());
            V currentVertex = (V) graph.getVertices().iterator().next();
            vertexPathList.add(currentVertex);
            Iterator incidentEdges = graph.getIncidentEdges(currentVertex).iterator();
            E edge = null;
            int edgesCount = graph.getEdgeCount();

            V vertex1;
            V vertex2;

            for(int i=0; i<edgesCount; i++) {
                while(incidentEdges.hasNext()) {
                    edge = (E) incidentEdges.next();
                    if(!edgePathList.contains(edge)) {//make sure we haven't already travelled the edge
                        if (!this.isBridge(edge)) {
                            edgePathList.add(edge);
                            break;
                        }
                        else if(!incidentEdges.hasNext()) {//only take the bridge edge is there is no other edge to take
                            edgePathList.add(edge);
                            break;
                        }
                    }
                }
                currentVertex = (V) graph.getOpposite(currentVertex,edge);
                vertexPathList.add(currentVertex);
                vertex1 = (V) graph.getEndpoints(edge).getFirst();
                vertex2 = (V) graph.getEndpoints(edge).getSecond();
                graph.removeEdge(edge);
                incidentEdges = graph.getIncidentEdges(currentVertex).iterator();
                //we remove the vertices that are no longer reachable in the reduced graph
                if(graph.degree(vertex1)==0) {
                    graph.removeVertex(vertex1);
                }
                if(graph.degree(vertex2)==0) {
                    graph.removeVertex(vertex2);
                }
            }
            System.out.println(vertexPathList);
            return edgePathList;
        }
        return null;
    }

    private boolean isBridge(E edge) {
        V node1 = (V) graph.getEndpoints(edge).getFirst();
        V node2 = (V) graph.getEndpoints(edge).getSecond();
        graph.removeEdge(edge);
        connectivityChecker = new ConnectivityChecker(graph);
        boolean isConnected = connectivityChecker.depthFirstSearch(node2);
        graph.addEdge(edge,new Pair(node1,node2), EdgeType.UNDIRECTED);
        return !isConnected;
    }
}
