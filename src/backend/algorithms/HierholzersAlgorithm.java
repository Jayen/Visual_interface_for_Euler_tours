package backend.algorithms;

import backend.fileparser.GraphParser;
import backend.fileparser.IncorrectFileFormatException;
import backend.internalgraph.LocationFixedSparseGraph;
import frontend.gui.AppGUI;
import java.io.IOException;
import java.util.*;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class HierholzersAlgorithm<V,E> implements EulerTourAlgorithm{

    private LocationFixedSparseGraph graph;
    private ArrayList edgePathList;
    private LinkedList<V> vertexPathList;
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
            vertexPathList = new LinkedList<V>();
            edgePathList = new ArrayList(graph.getEdgeCount());
            V currentVertex = (V) graph.getVertices().iterator().next();
            vertexPathList.add(currentVertex);
            Iterator incidentEdges = graph.getIncidentEdges(currentVertex).iterator();
            E edge;
            nodeCount = graph.getVertexCount();
            int edgesCount = graph.getEdgeCount();

            while(incidentEdges.hasNext()) {
                edge = (E) incidentEdges.next();
                edgePathList.add(edge);
                currentVertex = (V) graph.getOpposite(currentVertex,edge);
                vertexPathList.add(currentVertex);
                graph.removeEdge(edge);
                incidentEdges = graph.getIncidentEdges(currentVertex).iterator();
            }

            int insertionIndex = 0;
            while(edgePathList.size() < edgesCount) {
                while(insertionIndex < vertexPathList.size()) {
                    currentVertex = vertexPathList.get(insertionIndex);
                    incidentEdges = graph.getIncidentEdges(currentVertex).iterator();
                    while(incidentEdges.hasNext()) {
                        edge = (E) incidentEdges.next();
                        edgePathList.add(edge);
                        currentVertex = (V) graph.getOpposite(currentVertex,edge);
                        vertexPathList.add(insertionIndex,currentVertex);
                        graph.removeEdge(edge);
                        incidentEdges = graph.getIncidentEdges(currentVertex).iterator();
                    }
                    insertionIndex++;
                }
            }
            System.out.println(vertexPathList);
            return vertexPathList;
        }
        return null;
    }

    private void addNextCycle() {
        if(vertexPathList.size()==nodeCount) {
            return;
        }
        else {
            int nextIndexPointer;
            V currentVertex;
            E edge;
            Iterator incidentEdges;
            ListIterator pathListIterator = vertexPathList.listIterator();
            while(pathListIterator.hasNext()) {
                currentVertex = (V) pathListIterator.next();
                incidentEdges = graph.getIncidentEdges(currentVertex).iterator();
                if(incidentEdges.hasNext()){
                    nextIndexPointer = vertexPathList.indexOf(currentVertex);
                    edge = (E) incidentEdges.next();
                    edgePathList.add(edge);
                    currentVertex = (V) graph.getOpposite(currentVertex,edge);
                    vertexPathList.add(nextIndexPointer, currentVertex);
                    graph.removeEdge(edge);
                }
            }
        }
    }
}
