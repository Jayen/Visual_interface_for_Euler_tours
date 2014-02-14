package backend.algorithms;

import backend.fileparser.GraphParser;
import backend.fileparser.IncorrectFileFormatException;
import backend.internalgraph.Edge;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import frontend.gui.AppGUI;

import java.io.IOException;
import java.util.*;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class FleurysAlgorithm implements EulerTourAlgorithm {

    private Graph graph;
    private ArrayList nodePathList;
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
            nodePathList = new ArrayList(graph.getNodesCount());
            edgePathList = new ArrayList(graph.getEdgeCount());
            Node currentNode = graph.getNodes().iterator().next();
            nodePathList.add(currentNode);
            Iterator incidentEdges = graph.getIncidentEdges(currentNode).iterator();
            Edge edge = null;
            int edgesCount = graph.getEdgeCount();

            Node node1;
            Node node2;

            for(int i=0; i<edgesCount; i++) {
                while(incidentEdges.hasNext()) {
                    edge = (Edge) incidentEdges.next();
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
                currentNode = edge.getOpposite(currentNode);
                nodePathList.add(currentNode);
                node1 = edge.getFirstNode();
                node2 = edge.getSecondNode();
                graph.removeEdge(edge);
                incidentEdges = graph.getIncidentEdges(currentNode).iterator();
                //we remove the vertices that are no longer reachable in the reduced graph
                if(graph.degree(node1)==0) {
                    graph.removeNode(node1);
                }
                if(graph.degree(node2)==0) {
                    graph.removeNode(node2);
                }
            }
            System.out.println(nodePathList);
            return edgePathList;
        }
        return null;
    }

    private boolean isBridge(Edge edge) {
        Node node1 = edge.getFirstNode();
        Node node2 = edge.getSecondNode();
        graph.removeEdge(edge);
        connectivityChecker = new ConnectivityChecker(graph);
        boolean isConnected = connectivityChecker.depthFirstSearch(node2);
        graph.addEdge(edge);
        return !isConnected;
    }
}
