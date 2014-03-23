package backend.fileparser;

import backend.internalgraph.Graph;
import backend.internalgraph.Node;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     *
     * @param graphTextFile -graph file in .txt
     * @return Graph -LocationFixedSparseGraph created from file
     * @throws IncorrectFileFormatException
     * @throws IOException
     */
    public static Graph createGraphFromFile(File graphTextFile) throws IncorrectFileFormatException, IOException {

        graph = new Graph();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(graphTextFile));
        String currentLine = bufferedReader.readLine();

        if(currentLine.equals("#nodes")) {
            currentLine = bufferedReader.readLine();
        }
        else {
            throw new IncorrectFileFormatException("The file should start with #nodes");
        }

        Node node;
        Point2D location;
        Pattern pattern = Pattern.compile("([A-z|0-9]+)\\s\\((\\d+)[,](\\d+)\\)");
        Matcher matcher;
        while(!currentLine.isEmpty()) {
            matcher = pattern.matcher(currentLine);
            if(matcher.matches()) {
                location = new Point(Integer.parseInt(matcher.group(2)),Integer.parseInt(matcher.group(3)));
                node = new Node(matcher.group(1),location);
                graph.addNode(node);
            }
            else {
                throw new IncorrectFileFormatException(currentLine+"does not match the node format");
            }
            currentLine = bufferedReader.readLine();
        }
        currentLine = bufferedReader.readLine();

        if(currentLine.equals("#edges")) {
            currentLine = bufferedReader.readLine();
        }
        else {
            throw new IncorrectFileFormatException();
        }

        pattern = Pattern.compile("([A-z|0-9]+)[-]([A-z|0-9]+)");
        Node node1;
        Node node2;
        while(currentLine!=null) {
            matcher = pattern.matcher(currentLine);
            if(matcher.matches()) {
                node1 = new Node(matcher.group(1),null);
                node2 = new Node(matcher.group(2),null);
                if(graph.containsNode(node1) && graph.containsNode(node2)) {
                    graph.addEdge(node1,node2);
                }
                else {
                    throw new IncorrectFileFormatException("the node specified in edges does not exist in graph");
                }
            }
            currentLine = bufferedReader.readLine();
        }
        return graph;
    }
}
