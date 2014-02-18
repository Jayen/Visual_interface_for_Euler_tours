package graphView.model;

import backend.internalgraph.Graph;
import backend.internalgraph.Node;
import frontend.gui.AppGUI;

import java.util.HashMap;
import java.util.Iterator;

/**
 * The ViewGrid class maintains the
 * grid backing of the main graph view.
 * @author Jayen kumar Jaentilal k1189304
 */
public class ViewGrid {

    public static HashMap<String,Byte> occupancyType;

    private byte[][] grid;
    private int xLength = 500;//number of columns
    private int yLength = 500;//number of rows

    public ViewGrid() {
        this.grid = new byte[xLength][yLength];
        setupOccupancyTypes();
    }

    private void setupOccupancyTypes() {
        occupancyType = new HashMap<String, Byte>();
        occupancyType.put("unoccupied", (byte) 0);
        occupancyType.put("paddingOccupied", (byte) 1);
        byte occupancyValue = 2;
        Iterator nodesIterator = AppGUI.graph.getNodes().iterator();
        Iterator incidentNodesIterator;
        Node originNode;
        Node nextNode;
        String key;
        while(nodesIterator.hasNext()) {
            originNode = (Node) nodesIterator.next();
            incidentNodesIterator = AppGUI.graph.getIncidentNodes(originNode).iterator();
            while(incidentNodesIterator.hasNext()) {
                nextNode = (Node) incidentNodesIterator.next();
                key = originNode.toString()+" "+nextNode.toString();
                if(!occupancyType.containsKey(key)) {
                    occupancyType.put(key,occupancyValue);
                }
            }
            occupancyValue++;
        }
    }

    public void setupGridWithGraph(Graph graph) {

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
}
