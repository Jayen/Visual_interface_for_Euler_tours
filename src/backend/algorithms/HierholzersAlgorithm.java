package backend.algorithms;

import backend.fileparser.GraphParser;
import backend.fileparser.IncorrectFileFormatException;
import backend.internalgraph.Edge;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import frontend.gui.AlgorithmVisualiser;
import frontend.gui.AppGUI;

import java.io.IOException;
import java.util.*;

/**
 * This class implements Hierholzer's algorithm
 * to find a euler tour if one exists in the given graph
 * Jayen kumar Jaentilal k1189304
 */
public class HierholzersAlgorithm extends EulerTourAlgorithm {

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
            Node currentNode = graph.getNodes().iterator().next();
            nodePathList = new LinkedList<Node>();
            nodePathList.add(currentNode);
            Iterator incidentNodesIterator = graph.getIncidentNodes(currentNode).iterator();

            int edgesCount = graph.getNumberOfEdges();
            Node prevNode = currentNode;
            int edgesTravelled = 0;

            //first cycle of the algorithm
            while(incidentNodesIterator.hasNext()) {
                currentNode = (Node) incidentNodesIterator.next();
                nodePathList.add(currentNode);
                graph.removeEdge(prevNode,currentNode);
                edgesTravelled++;
                incidentNodesIterator = graph.getIncidentNodes(currentNode).iterator();
                prevNode = currentNode;
            }
            int insertionIndex = 0;
            int indexToCheckFrom = 0;
            while(edgesTravelled < edgesCount) {
                while(insertionIndex < nodePathList.size()) {
                    currentNode = nodePathList.get(indexToCheckFrom);//try a node already in the path list for untravelled edges.
                    incidentNodesIterator = graph.getIncidentNodes(currentNode).iterator();
                    while(incidentNodesIterator.hasNext()) {//we pick a node which is already in the pathList but has untravelled edges
                        prevNode = currentNode;
                        currentNode = (Node) incidentNodesIterator.next();
                        nodePathList.add(insertionIndex + 1, currentNode);//insert the nodes from the next cycle in between
                        insertionIndex++;
                        graph.removeEdge(prevNode, currentNode);
                        edgesTravelled++;
                        incidentNodesIterator = graph.getIncidentNodes(currentNode).iterator();
                    }
                    indexToCheckFrom++;
                    insertionIndex = indexToCheckFrom;
                }
            }
            System.out.println(nodePathList);
            return nodePathList;
        }
        return null;
    }
}
