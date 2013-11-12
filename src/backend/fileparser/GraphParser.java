package backend.fileparser;

import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import java.io.*;

/**
 * This class creates a internal
 * representation of the graph
 * from file.
 * @author Jayen kumar Jaentilal k1189304
 */

public class GraphParser {

    private static Graph graph;

    /**
     * Method to generate a graph from a text file.
     * Method throws IncorrectFileFormatException if the
     * files content does not follow the required format,
     * Also throws IOException if the file path is invalid
     * @param graphTextFile
     * @return Graph -graph created from file
     * @throws IncorrectFileFormatException
     * @throws IOException
     */
    public static Graph createGraphFromFile(File graphTextFile) throws IncorrectFileFormatException, IOException {

        graph = new Graph();
        BufferedReader br = new BufferedReader(new FileReader(graphTextFile));
        String line = br.readLine();

        if(line.equals("#nodes")) {
            line = br.readLine();
        }
        else {
            throw new IncorrectFileFormatException();
        }
        while(!line.contains("#")) {
            graph.addNode(new Node(line));
            line = br.readLine();
        }
        if(line.equals("#edges")) {
            line = br.readLine();
        }
        else {
            throw new IncorrectFileFormatException();
        }
        while(line!=null) {
            connectNodes(line);
            line = br.readLine();
        }
        return graph;
    }

    /**
     * Method to connect nodes
     * based on the input
     * @param connectedNodeString
     */
    private static void connectNodes(String connectedNodeString) {
        String node1Name;
        String node2Name;

        int connectSymbolIndex = connectedNodeString.indexOf("-");
        node1Name = connectedNodeString.substring(0,connectSymbolIndex);
        node2Name = connectedNodeString.substring(connectSymbolIndex-1,connectedNodeString.length());
        graph.connectNodes(graph.getNode(node1Name),graph.getNode(node2Name));
    }
}
