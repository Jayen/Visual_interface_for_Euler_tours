package graphView.model;

import backend.internalgraph.Edge;
import backend.internalgraph.Graph;
import backend.internalgraph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * The ViewGrid class maintains the
 * grid backing of the main graph view.
 * @author Jayen kumar Jaentilal k1189304
 */
public class ViewGrid {

    //stores the key string (edge name) and the occupancy values associated with the edge
    //a edge may have multiple values if there are multiple edges between the same nodes
    private static HashMap<String,ArrayList<Short>> occupancyType;

    private short[][] grid;
    private int xLength = 500;//number of columns
    private int yLength = 500;//number of rows

    public ViewGrid() {
        this.grid = new short[xLength][yLength];
    }

    /**
     * This method loads a new graph
     * for the internal grid representation
     * it uses the PathRouter class to compute
     * the edges for the graph
     * @param graph -Graph
     */
    public void loadNewGraph(Graph graph) {
        occupancyType = new HashMap<String,ArrayList<Short>>();
        occupancyType.put("unoccupied",new ArrayList<Short>());
        occupancyType.get("unoccupied").add((short)0);
        occupancyType.put("node", new ArrayList<Short>());
        occupancyType.get("node").add((short)1);
        occupancyType.put("paddingOccupied",new ArrayList<Short>());
        occupancyType.get("paddingOccupied").add((short)2);

        //initialise the grid with all cells unoccupied
        for(int row=0; row<grid.length; row++) {
            for(int col=0; col<grid[0].length; col++) {
                grid[row][col] = occupancyType.get("unoccupied").get(0);
            }
        }

        short[] traversableValues;
        short occupancyValue = 3;
        Node originNode;
        Node nextNode;
        String key;
        PathRouter router;
        List<GridCell> path;

        Iterator<Edge> edgesIterator = graph.getEdges().iterator();
        Edge edge;
        while (edgesIterator.hasNext()) {
            edge = edgesIterator.next();
            originNode = edge.getFirstNode();
            nextNode = edge.getSecondNode();
            key = originNode.toString()+" "+nextNode.toString();
            if(!occupancyType.containsKey(key)) {
                occupancyType.put(key, new ArrayList<Short>());
                occupancyType.get(key).add(occupancyValue);
            }
            else {
                occupancyType.get(key).add(occupancyValue);
            }
            traversableValues = new short[]{occupancyType.get("unoccupied").get(0),
                                            occupancyType.get("paddingOccupied").get(0)};

            router = new PathRouter((int)originNode.getY(),(int)originNode.getX(),
                                    (int)nextNode.getY(),(int)nextNode.getX(),
                                    traversableValues,this);
            path = router.getPath();

            if(path==null) {//path not found due to edge crossing, try to find a path with edge crossing
                traversableValues = new short[occupancyType.size()];
                Iterator keyIterator = occupancyType.keySet().iterator();
                String valueKey;
                int counter = 0;
                while(keyIterator.hasNext()) {
                    valueKey = (String) keyIterator.next();
                    if(!valueKey.equals("node")) {
                        traversableValues[counter] = occupancyType.get(valueKey).get(0);
                    }
                    counter++;
                }
                router = new PathRouter((int)originNode.getY(),(int)originNode.getX(),
                                        (int)nextNode.getY(),(int)nextNode.getX(),
                                        traversableValues,this);
                path = router.getPath();
            }
            this.markPath(path,occupancyValue,10);
            path = null;
            occupancyValue++;
        }
    }

    /**
     * Method to mark a certain a path which is a edge
     * with a certain occupancy value with a set layer padding
     * @param path -the path to mark
     * @param occupancyValue -the value to mark with
     * @param layers -number of layers
     */
    private void markPath(List<GridCell> path, short occupancyValue,int layers) {
        if(path==null) {
            return;
        }
        GridCell cell;
        boolean notOutOfBounds;
        int lastI = path.size()-1;
        for(int i=lastI; i>=0; i--) {
            cell = path.get(i);
            //loop to apply the required layers padding
            for(int row=cell.getRow()-layers; row<=cell.getRow()+layers; row++) {
                for(int col=cell.getCol()-layers; col<=cell.getCol()+layers; col++) {
                    notOutOfBounds = row>=0 && col>=0 && row<this.rowLength() && col<this.colLength();
                    if(notOutOfBounds) {
                        if(path.contains(new GridCell(row,col))) {//if the cell is part of the path don't mark as padding
                            if(i==lastI || i==0) {
                                this.setOccupancyGridValue(cell.getRow(),cell.getCol(),occupancyType.get("node").get(0));
                            }
                            else {
                                this.setOccupancyGridValue(cell.getRow(),cell.getCol(),occupancyValue);
                            }
                        }
                        else if(this.getOccupancyGridValue(row,col)==occupancyType.get("unoccupied").get(0)){
                            this.setOccupancyGridValue(row,col,occupancyType.get("paddingOccupied").get(0));
                        }
                    }
                }
            }
        }
    }

    public ArrayList<Short> getOccupancyType(String key) {
        return occupancyType.get(key);
    }

    public void setOccupancyGridValue(int row, int col, Short occupancyValue) {
        grid[row][col] = occupancyValue;
    }

    public short getOccupancyGridValue(int row, int col) {
        return grid[row][col];
    }

    public int rowLength() {
        return grid.length;
    }

    public int colLength() {
        return grid[0].length;
    }

    public ArrayList<Short> getEdgeValue(Node node1, Node node2) {
        ArrayList<Short> values = null;
        if(occupancyType.get(node1.toString()+" "+node2.toString())!=null) {
            values = occupancyType.get(node1.toString()+" "+node2.toString());
        }
        else if(occupancyType.get(node2.toString()+" "+node1.toString())!=null) {
            values = occupancyType.get(node2.toString()+" "+node1.toString());
        }
        return values;
    }

    public void printGrid() {
        for (short[] rowGrid : grid) {
            for (int col = 0; col < grid.length; col++) {
                System.out.print(rowGrid[col]);
            }
            System.out.println();
        }
    }
}
