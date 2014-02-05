package backend.algorithms;

import backend.fileparser.GraphParser;
import backend.fileparser.IncorrectFileFormatException;
import backend.internalgraph.LocationFixedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import frontend.gui.AppGUI;
import frontend.gui.OpenFileActionListener;

import java.io.IOException;
import java.util.*;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class FleurysAlgorithm<V,E> implements EulerTourAlgorithm {

    private LocationFixedSparseGraph graph;
    private ArrayList pathList;
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
}
