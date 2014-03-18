package backend.algorithms;

import backend.internalgraph.Graph;
import backend.internalgraph.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * User: jayen
 * Date: 15/03/14, Time: 17:42
 */
public class EulerisationAlgorithm {

    /* A graph may contain many sub-graphs which need to be connected up
     * and in the worst case all nodes are unconnected meaning for n node there will be n sub-graphs
     * The HashSet represents each sub-graph the HashSet contains a list
     * of nodes that are in that particular sub-graph TODO Disjoint set?
     */
    public HashSet<Node[]> subGraphs;

    public Graph graph;
    public HashMap<Node,Node> nodes;

    public double computeDistance(Node currentNode, Node nextNode) {
        //euclidean distance (pythagoras)
        return Math.sqrt((currentNode.getX()-nextNode.getX())*(currentNode.getX()-nextNode.getX())
                +(currentNode.getY()-nextNode.getY())*(currentNode.getY()-nextNode.getY()));
    }

    public void findSubGraphs() {
        HashMap<Node,Node> marked = new HashMap<Node,Node>();
        Node[] nodeKeys = nodes.keySet().toArray(new Node[marked.size()]);
        int counter = 0;
        while(counter<nodeKeys.length) {
            if(nodes.get(nodeKeys[counter])!=null) {
                connectedNodesDFS(nodes.get(nodeKeys[counter]), marked);
                subGraphs.add(marked.values().toArray(new Node[marked.size()]));
                marked = new HashMap<Node, Node>();
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

    public void printSubGraphs() {
        Iterator iterator = subGraphs.iterator();
        System.out.println("printing sub-graphs");
        Node[] subGraph;
        while(iterator.hasNext()) {
            subGraph = (Node[]) iterator.next();
            for(int i=0; i<subGraph.length; i++) {
                System.out.print(subGraph[i]);
            }
            System.out.println();
        }
    }
}
