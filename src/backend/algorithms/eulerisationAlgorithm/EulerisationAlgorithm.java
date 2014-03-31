package backend.algorithms.eulerisationAlgorithm;

import backend.internalgraph.Graph;
import backend.internalgraph.Node;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Jayen kumar Jaentilal k1189304
 */
public abstract class EulerisationAlgorithm {

    /* A graph may contain many sub-graphs which need to be connected up
     * and in the worst case all nodes are unconnected meaning for n nodes there will be n sub-graphs
     * The HashMap represents each sub-graph the HashMap contains a list
     * of nodes that are in that particular sub-graph
     */
    public HashMap<String,Node[]> subGraphs;
    public Graph graph;
    public HashMap<Node,Node> nodes;

    public void findSubGraphs() {
        HashMap<Node,Node> marked = new HashMap<Node,Node>();
        Node[] nodeKeys = nodes.keySet().toArray(new Node[marked.size()]);
        int counter = 0;
        int subGraphCounter = 0;//also used as a id for sub-graph names
        while(counter<nodeKeys.length) {
            if(nodes.get(nodeKeys[counter])!=null) {
                connectedNodesDFS(nodes.get(nodeKeys[counter]), marked);
                subGraphs.put("subGraph" + subGraphCounter, marked.values().toArray(new Node[marked.size()]));
                marked = new HashMap<Node, Node>();
                subGraphCounter++;
            }
            counter++;
        }
        this.printSubGraphs();
    }

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

    public void putAllNodes(HashMap<Node, Node> nodes) {
        Iterator iterator = graph.getNodes().iterator();
        Node node;
        while(iterator.hasNext()) {
            node = (Node) iterator.next();
            nodes.put(node, node);
        }
    }

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
