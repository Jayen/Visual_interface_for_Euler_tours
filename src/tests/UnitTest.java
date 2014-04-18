package tests;

import backend.algorithms.ConnectivityChecker;
import backend.algorithms.EulerTourChecker;
import backend.algorithms.Heuristics;
import backend.fileparser.GraphParser;
import backend.fileparser.IncorrectFileFormatException;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import junit.framework.TestCase;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Unit tests for the application
 * jayen kumar Jaentilal
 */
public class UnitTest extends TestCase {
    public void setUp() throws Exception {
        super.setUp();
        testComputeEuclideanDistance();
        testGraphParser();
        testConnectivityChecker();
        testHasEulerTour();
    }

    public void testComputeEuclideanDistance() throws Exception {
        assertEquals(28.28, Heuristics.computeEuclideanDistance(new Node("A",new Point(30,30)),new Node("B",new Point(50,50))),0.2);
        assertEquals(56.56, Heuristics.computeEuclideanDistance(new Node("A",new Point(40,40)),new Node("B",new Point(80,80))),0.2);
        assertEquals(40,Heuristics.computeManhattanDistance(new Node("A",new Point(30,30)),new Node("B",new Point(50,50))),0.2);
        assertEquals(80,Heuristics.computeManhattanDistance(new Node("A",new Point(40,40)),new Node("B",new Point(80,80))),0.2);
    }

    public void testGraphParser(){
        try {
            assertNotNull(GraphParser.createGraphFromFile(new File("testGraphs/noEdges.txt")));
        } catch (IncorrectFileFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testConnectivityChecker() throws IncorrectFileFormatException, IOException {
        Graph graph = GraphParser.createGraphFromFile(new File("testGraphs/test.txt"));
        assertEquals(true,new ConnectivityChecker(graph).isGraphConnectedDFS(graph.getNodes().iterator().next()));
        graph = GraphParser.createGraphFromFile(new File("testGraphs/disconnectedGraph.txt"));
        assertEquals(false,new ConnectivityChecker(graph).isGraphConnectedDFS(graph.getNodes().iterator().next()));
    }

    public void testHasEulerTour() throws IncorrectFileFormatException, IOException {
        assertEquals(true, EulerTourChecker.hasEulerTour(GraphParser.createGraphFromFile(new File("testGraphs/test.txt"))));
        assertEquals(false, EulerTourChecker.hasEulerTour(GraphParser.createGraphFromFile(new File("testGraphs/disconnectedGraph.txt"))));
        assertEquals(true, EulerTourChecker.hasEulerTour(GraphParser.createGraphFromFile(new File("testGraphs/tinEulerised.txt"))));
        assertEquals(false, EulerTourChecker.hasEulerTour(GraphParser.createGraphFromFile(new File("testGraphs/tin.txt"))));
    }
}
