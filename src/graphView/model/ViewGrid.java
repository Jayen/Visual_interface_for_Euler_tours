package graphView.model;

import backend.internalgraph.Edge;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import frontend.gui.AppGUI;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
        Edge edge;
        while (edgesIterator.hasNext()) {
            edge = edgesIterator.next();
            originNode = edge.getFirstNode();
            nextNode = edge.getSecondNode();
            key = originNode.toString()+" "+nextNode.toString();
            if(!occupancyType.containsKey(key)) {
                occupancyType.put(key, occupancyValue);
            }
            router = new PathRouter((int)originNode.getLocation().getY(),(int)originNode.getLocation().getX(),
                    (int)nextNode.getLocation().getY(),(int)nextNode.getLocation().getX(),
                    occupancyType.get("unoccupied"),this);
            this.markPath(router.getPath(),occupancyValue);
            occupancyValue++;
        }
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
