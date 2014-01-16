package backend.fileparser;

import backend.internalgraph.Graph;
import backend.internalgraph.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class creates a internal
 * representation of the graph from file.
 * @author Jayen kumar Jaentilal k1189304
 */

public class GraphParser {

    private static Graph graph;

    /**
     * Method to generate a graph from a text file.
     * Method throws IncorrectFileFormatException if the
     * files content does not follow the required format,
     * Also throws IOException if the file path is invalid
     * @param graphTextFile -graph file in .txt
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
     * @param connectNodeString -String describing the connection
     */
    private static void connectNodes(String connectNodeString) {
        String node1Name;
        String node2Name;

        int connectSymbolIndex = connectNodeString.indexOf("-");
        node1Name = connectNodeString.substring(0,connectSymbolIndex);
        node2Name = connectNodeString.substring(connectSymbolIndex-1,connectNodeString.length());
        graph.addEdge(graph.getNode(node1Name), graph.getNode(node2Name));
    }
}
