package graphView.model;

import backend.internalgraph.Edge;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import frontend.gui.AppGUI;

import java.util.*;

/**
 * The ViewGrid class maintains the
 * grid backing of the main graph view.
 * @author Jayen kumar Jaentilal k1189304
 */
public class ViewGrid {

    private static HashMap<String,Byte> occupancyType;

    private byte[][] grid;
    private int xLength = 500;//number of columns
    private int yLength = 500;//number of rows

    public ViewGrid() {
        this.grid = new byte[xLength][yLength];
    }

    public void loadNewGraph(Graph graph) {
        occupancyType = new HashMap<String, Byte>();
        occupancyType.put("unoccupied", (byte) 0);
        occupancyType.put("paddingOccupied", (byte) 1);

        //initialise the grid with all cells unoccupied
        for(int row=0; row<grid.length; row++) {
            for(int col=0; col<grid[0].length; col++) {
                grid[row][col] = occupancyType.get("unoccupied");
            }
        }

        byte occupancyValue = 2;
        Node originNode;
        Node nextNode;
        String key;
        PathRouter router;

        Iterator<Edge> edgesIterator = graph.getEdges().iterator();
        ArrayList<Edge> currentlyDrawnEdges = new ArrayList<Edge>(graph.getNumberOfEdges());
        Edge edge;
        while (edgesIterator.hasNext()) {
            edge = edgesIterator.next();
            originNode = edge.getFirstNode();
            nextNode = edge.getSecondNode();
            key = originNode.toString()+" "+nextNode.toString();
            if(!occupancyType.containsKey(key)) {
                occupancyType.put(key, occupancyValue);
            }

            if(currentlyDrawnEdges.size()!=0) {
                for(int i=0; i<currentlyDrawnEdges.size(); i++) {
                    if(edgeCrosses(originNode,nextNode,currentlyDrawnEdges.get(i))) {
                        router = new PathRouter((int)originNode.getLocation().getY(),(int)originNode.getLocation().getX(),
                                (int)nextNode.getLocation().getY(),(int)nextNode.getLocation().getX(),
                                occupancyType.get("unoccupied"),this);
                        this.markPath(router.getPath(),occupancyValue);
                    }
                    else {
                        this.drawStraightPath();
                    }
                }
            }
            occupancyValue++;
        }
    }

    private void drawStraightPath() {

    }

    private boolean edgeCrosses(Node originNode, Node nextNode, Edge edge) {
        double y1;
        double c1;
        double gradient1;

        gradient1 = (nextNode.getY()-originNode.getY()) / (nextNode.getX()-originNode.getX());
        c1 = originNode.getY() - (gradient1*originNode.getX());
        y1 = (gradient1*originNode.getX()) + c1;

        double y2;
        double c2;
        double gradient2;

        Node otherLineOriginNode = edge.getFirstNode();
        Node otherLineNextNode = edge.getSecondNode();

        gradient2 = (otherLineNextNode.getY()-otherLineOriginNode.getY()) / (otherLineNextNode.getX()-otherLineOriginNode.getY());
        c2 = originNode.getY() - (gradient2*otherLineOriginNode.getX());
        y2 = (gradient2*otherLineOriginNode.getX()) + c2;

        double xIntersection;
        double yIntersection;

        return false;
    }

    /**
     * Method to mark a certain a path which is a edge
     * with a certain occupancy value
      * @param path
     * @param occupancyValue
     */
    private void markPath(List<GridCell> path, byte occupancyValue) {
        GridCell cell;
        for(int i=0; i<path.size(); i++) {
            cell = path.get(i);
            this.setOccupancyGridValue(cell.getRow(),cell.getCol(),occupancyValue);
        }
    }

    public Byte getOccupancyType(String key) {
        return occupancyType.get(key);
    }

    public void setOccupancyGridValue(int row, int col, byte occupancyValue) {
        grid[row][col] = occupancyValue;
    }

    public byte getOccupancyGridValue(int row,int col) {
        return grid[row][col];
    }

    public int rowLength() {
        return grid.length;
    }

    public int colLength() {
        return grid[0].length;
    }

    public void printGrid() {
        for(int row=0; row<grid.length; row++) {
            for(int col=0; col<grid.length; col++) {
                System.out.print(grid[row][col]);
            }
            System.out.println();
        }
    }
}
