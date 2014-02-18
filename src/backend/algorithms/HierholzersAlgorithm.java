package backend.algorithms;

import backend.fileparser.GraphParser;
import backend.fileparser.IncorrectFileFormatException;
import backend.internalgraph.Edge;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import frontend.gui.AppGUI;

import java.io.IOException;
import java.util.*;

/**
 * This class implements Hierholzers algorithm
 * to find a euler tour if one exists in the given graph
 * Jayen kumar Jaentilal k1189304
 */
public class HierholzersAlgorithm implements EulerTourAlgorithm{

    private Graph graph;
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
            Node currentNode = graph.getNodes().iterator().next();
            nodePathList.add(currentNode);
            Iterator incidentNodesIterator = graph.getIncidentNodes(currentNode).iterator();

            nodeCount = graph.getNumberOfNodes();
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
            while(edgesTravelled <= edgesCount) {
                while(insertionIndex < nodePathList.size()) {
                    currentNode = nodePathList.get(insertionIndex);//try a node already in the path list for untravelled edges.
                    incidentNodesIterator = graph.getIncidentNodes(currentNode).iterator();
                    while(incidentNodesIterator.hasNext()) {//we pick a node which is already in the pathList but has untravelled edges
                        currentNode = (Node) incidentNodesIterator.next();
                        nodePathList.add(insertionIndex,currentNode);//insert the nodes from the next cycle in between
                        graph.removeEdge(prevNode, currentNode);
                        edgesTravelled++;
                        incidentNodesIterator = graph.getIncidentNodes(currentNode).iterator();
                        prevNode = currentNode;
                    }
                    insertionIndex++;
                }
            }
            System.out.println(nodePathList);
            return nodePathList;
        }
        return null;
    }
}
