package backend.algorithms;

import backend.fileparser.GraphParser;
import backend.fileparser.IncorrectFileFormatException;
import backend.internalgraph.Edge;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import frontend.gui.AppGUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implements Fleury's algorithm
 * to find a euler tour if it exists.
 * Jayen kumar Jaentilal k1189304
 */
public class FleurysAlgorithm implements EulerTourAlgorithm {

    private Graph graph;
    private ArrayList<Node> nodePathList;
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
            nodePathList = new ArrayList<Node>(graph.getNumberOfNodes());
            Node currentNode = graph.getNodes().iterator().next();
            nodePathList.add(currentNode);
            Iterator incidentNodesIterator = graph.getIncidentNodes(currentNode).iterator();

            Node prevNode = currentNode;
            int edgesCount = graph.getNumberOfEdges();
            //for loop to make sure we go through all the edges in the graph
            for(int i=0; i<edgesCount; i++) {
                while(incidentNodesIterator.hasNext()) {
                    currentNode = (Node) incidentNodesIterator.next();//next node we will travel to
                    if (!this.isBridge(prevNode, currentNode)) {//travel to the node only if the edge to it is not a bridge
                        nodePathList.add(currentNode);
                        break;
                    }
                    else if(!incidentNodesIterator.hasNext()) {//only take the bridge edge is there is no other edge to take
                        nodePathList.add(currentNode);
                        break;
                    }
                }
                graph.removeEdge(prevNode,currentNode);//remove the edge for the reduced graph
                incidentNodesIterator = graph.getIncidentNodes(currentNode).iterator();
                //we remove the vertices that are no longer reachable in the reduced graph
                if(graph.degree(prevNode)==0) {
                    graph.removeNode(prevNode);
                }
                if(graph.degree(currentNode)==0) {
                    graph.removeNode(currentNode);
                }
                prevNode = currentNode;
            }
            System.out.println(nodePathList);
            return nodePathList;
        }
        return null;
    }

    /**
     * Tests if a edge between
     * two nodes is a bridge.
     * @param node1
     * @param node2
     * @return
     */
    private boolean isBridge(Node node1, Node node2) {
        graph.removeEdge(node1,node2);
        connectivityChecker = new ConnectivityChecker(graph);
        //if the graph is connected then the edge is not a bridge i.e isConnected = true
        boolean isConnected = connectivityChecker.depthFirstSearch(node2);
        graph.addEdge(node1,node2);//add the edge back to the original graph
        return !isConnected;
    }
}
