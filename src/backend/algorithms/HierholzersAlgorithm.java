package backend.algorithms;

import backend.fileparser.GraphParser;
import backend.fileparser.IncorrectFileFormatException;
import backend.internalgraph.Edge;
import backend.internalgraph.Graph;
import backend.internalgraph.LocationFixedSparseGraph;
import backend.internalgraph.Node;
import frontend.gui.AppGUI;
import java.io.IOException;
import java.util.*;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class HierholzersAlgorithm implements EulerTourAlgorithm{

    private Graph graph;
    private ArrayList edgePathList;
    private LinkedList<Node> nodePathList;
    private int nodeCount;

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
            nodePathList = new LinkedList<Node>();
            edgePathList = new ArrayList(graph.getEdgeCount());
            Node currentNode = graph.getNodes().iterator().next();
            nodePathList.add(currentNode);
            Iterator incidentEdges = graph.getIncidentEdges(currentNode).iterator();
            Edge edge;
            nodeCount = graph.getNodesCount();
            int edgesCount = graph.getEdgeCount();

            while(incidentEdges.hasNext()) {
                edge = (Edge) incidentEdges.next();
                edgePathList.add(edge);
                currentNode = edge.getOpposite(currentNode);
                nodePathList.add(currentNode);
                graph.removeEdge(edge);
                incidentEdges = graph.getIncidentEdges(currentNode).iterator();
            }

            int insertionIndex = 0;
            while(edgePathList.size() < edgesCount) {
                while(insertionIndex < nodePathList.size()) {
                    currentNode = nodePathList.get(insertionIndex);
                    incidentEdges = graph.getIncidentEdges(currentNode).iterator();
                    while(incidentEdges.hasNext()) {
                        edge = (Edge) incidentEdges.next();
                        edgePathList.add(edge);
                        currentNode = edge.getOpposite(currentNode);
                        nodePathList.add(insertionIndex,currentNode);
                        graph.removeEdge(edge);
                        incidentEdges = graph.getIncidentEdges(currentNode).iterator();
                    }
                    insertionIndex++;
                }
            }
            System.out.println(nodePathList);
            return nodePathList;
        }
        return null;
    }

    private void addNextCycle() {
        if(nodePathList.size()==nodeCount) {
            return;
        }
        else {
            int nextIndexPointer;
            Node currenNode;
            Edge edge;
            Iterator incidentEdges;
            ListIterator pathListIterator = nodePathList.listIterator();
            while(pathListIterator.hasNext()) {
                currenNode = (Node) pathListIterator.next();
                incidentEdges = graph.getIncidentEdges(currenNode).iterator();
                if(incidentEdges.hasNext()){
                    nextIndexPointer = nodePathList.indexOf(currenNode);
                    edge = (Edge) incidentEdges.next();
                    edgePathList.add(edge);
                    currenNode = edge.getOpposite(currenNode);
                    nodePathList.add(nextIndexPointer, currenNode);
                    graph.removeEdge(edge);
                }
            }
        }
    }
}
