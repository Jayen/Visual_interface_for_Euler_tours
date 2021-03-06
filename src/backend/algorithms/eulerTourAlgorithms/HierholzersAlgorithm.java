package backend.algorithms.eulerTourAlgorithms;

import backend.algorithms.EulerTourChecker;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements Hierholzer's algorithm
 * to find a euler tour if one exists in the given graph
 * Jayen kumar Jaentilal k1189304
 */
public class HierholzersAlgorithm extends EulerTourAlgorithm {

    @Override
    public List getEulerTour(Graph graphToCheck) {
        graph = new Graph(graphToCheck);

        if(EulerTourChecker.hasEulerTour(graph)) {
            Node currentNode = graph.getNodes().iterator().next();
            nodePathList = new LinkedList<Node>();
            nodePathList.add(currentNode);
            Iterator incidentNodesIterator = graph.getIncidentNodes(currentNode).iterator();

            int totalEdges = graph.getNumberOfEdges();
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
            while(edgesTravelled < totalEdges) {

                while(insertionIndex < nodePathList.size()) {
                    //try a node already in the path list for untravelled edges.
                    currentNode = nodePathList.get(indexToCheckFrom);
                    incidentNodesIterator = graph.getIncidentNodes(currentNode).iterator();

                    //we pick a node which is already in the pathList but has untravelled edges
                    while(incidentNodesIterator.hasNext()) {
                        prevNode = currentNode;
                        currentNode = (Node) incidentNodesIterator.next();
                        //insert the nodes from the next cycle in between
                        nodePathList.add(insertionIndex + 1, currentNode);
                        insertionIndex++;
                        //remove edge as we have now travelled it
                        graph.removeEdge(prevNode, currentNode);
                        edgesTravelled++;
                        //get nodes to visit from the current node
                        incidentNodesIterator = graph.getIncidentNodes(currentNode).iterator();
                    }
                    indexToCheckFrom++;
                    insertionIndex = indexToCheckFrom;
                }
            }
            return nodePathList;
        }
        return null;
    }
}
