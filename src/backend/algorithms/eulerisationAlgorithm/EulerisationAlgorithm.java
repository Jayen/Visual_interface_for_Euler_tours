package backend.algorithms.eulerisationAlgorithm;

import backend.internalgraph.Graph;
import backend.internalgraph.Node;

import java.util.HashMap;
import java.util.Iterator;

/**
 * This class sets up the foundation
 * for the eulerisation algorithms
 * Algorithms implementing eulerisation
 * should extend this class.
 * Jayen kumar Jaentilal k1189304
 */
public abstract class EulerisationAlgorithm {

    /* A graph may contain many sub-graphs which need to be connected up
     * The HashMap represents each sub-graph the HashMap contains a array
     * of nodes that are in that particular sub-graph
     */
    public HashMap<String,Node[]> subGraphs;
    public Graph graph;
    public HashMap<Node,Node> nodes;

    /**
     * This method find all the sub-graphs
     * (connected components) of the main graph
     */
    public void findSubGraphs() {
        HashMap<Node,Node> marked = new HashMap<Node,Node>();
        Node[] nodeKeys = nodes.keySet().toArray(new Node[marked.size()]);
        int counter = 0;
        int subGraphCounter = 0;//also used as a id for sub-graph names
        while(counter<nodeKeys.length) {
            if(nodes.get(nodeKeys[counter])!=null) {
                connectedNodesDFS(nodes.get(nodeKeys[counter]), marked);
                subGraphs.put("subGraph" + subGraphCounter, marked.values().toArray(new Node[marked.size()]));
                marked.clear();
                subGraphCounter++;
            }
            counter++;
        }
    }

    /**
     * This method carries out a DFS and marks
     * all the nodes reachable from the source node
     * @param sourceNode the node to search from
     * @param marked the marked nodes are all the nodes reachable
     *               from the source node
     */
    public void connectedNodesDFS(Node sourceNode,HashMap<Node,Node> marked) {
        marked.put(sourceNode,sourceNode);
        Iterator iterator = graph.getConnectedNodes(sourceNode).iterator();
        nodes.remove(sourceNode);
        Node node;
        while(iterator.hasNext()) {
            node = (Node) iterator.next();
            if(marked.get(node)==null) {
                connectedNodesDFS(node, marked);
            }
        }
    }

    /**
     * Put all the nodes in the
     * nodes HashMap
     * @param nodes The HashMap to put the nodes in
     */
    public void putAllNodes(HashMap<Node, Node> nodes) {
        Iterator iterator = graph.getNodes().iterator();
        Node node;
        while(iterator.hasNext()) {
            node = (Node) iterator.next();
            nodes.put(node, node);
        }
    }

    /**
     * Put all the odd degree
     * nodes in the HashMap
     * @param nodes The HashMap to put the nodes in
     */
    public void putAllOddNodes(HashMap<Node, Node> nodes) {
        Iterator iterator = graph.getNodes().iterator();
        Node node;
        while(iterator.hasNext()) {
            node = (Node) iterator.next();
            if(graph.degree(node)%2!=0) {
                nodes.put(node,node);
            }
        }
    }

    public void printSubGraphs() {
        Iterator iterator = subGraphs.keySet().iterator();
        System.out.println("printing sub-graphs");
        Node[] subGraph;
        while(iterator.hasNext()) {
            subGraph = subGraphs.get(iterator.next());
            for (Node aSubGraph : subGraph) {
                System.out.print(aSubGraph);
            }
            System.out.println();
        }
    }

    public void euleriseGraph(boolean visualise) {

    }
}
