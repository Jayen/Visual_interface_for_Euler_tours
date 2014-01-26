package backend.fileparser;

import backend.internalgraph.LocationFixedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

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

    private static LocationFixedSparseGraph<String,String> graph;

    /**
     * Method to generate a graph from a text file.
     * Method throws IncorrectFileFormatException if the
     * files content does not follow the required format,
     * Also throws IOException if the file path is invalid
     * @param graphTextFile -graph file in .txt
     * @return Graph -LocationFixedSparseGraph created from file
     * @throws IncorrectFileFormatException
     * @throws IOException
     */
    public static LocationFixedSparseGraph<String,String> createGraphFromFile(File graphTextFile) throws IncorrectFileFormatException, IOException {

        graph = new LocationFixedSparseGraph<String, String>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(graphTextFile));
        String line = bufferedReader.readLine();

        if(line.equals("#nodes")) {
            line = bufferedReader.readLine();
        }
        else {
            throw new IncorrectFileFormatException();
        }

        String nodeName;
        Point2D location;
        Pattern pattern = Pattern.compile("([A-z|0-9]+)\\s\\((\\d+)[,](\\d+)\\)");
        Matcher matcher;
        while(!line.isEmpty()) {
            matcher = pattern.matcher(line);
            if(matcher.matches()) {
                nodeName = matcher.group(1);
                location = new Point(Integer.parseInt(matcher.group(2)),Integer.parseInt(matcher.group(3)));
                graph.addVertex(nodeName);
                graph.addVertexLocation(nodeName,location);
            }
            else {
                throw new IncorrectFileFormatException();
            }
            line = bufferedReader.readLine();
        }
        line = bufferedReader.readLine();

        if(line.equals("#edges")) {
            line = bufferedReader.readLine();
        }
        else {
            throw new IncorrectFileFormatException();
        }

        pattern = Pattern.compile("([A-z|0-9]+)[-]([A-z|0-9]+)");
        String node1;
        String node2;
        while(line!=null) {
            matcher = pattern.matcher(line);
            if(matcher.matches()) {
                node1 = matcher.group(1);
                node2 = matcher.group(2);
                if(graph.containsVertex(node1) && graph.containsVertex(node2)) {
                    graph.addEdge(node1+node2,new Pair(node1,node2), EdgeType.UNDIRECTED);
                }
                else {
                    throw new IncorrectFileFormatException();
                }
            }
            line = bufferedReader.readLine();
        }
        return graph;
    }
}
