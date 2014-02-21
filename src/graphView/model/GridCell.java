package graphView.model;

/**
 * A GridCell holds the data for each cell
 * in the grid. PathRouter uses this class
 * for path planning.
 * @author Jayen kumar Jaentilal k1189304
 */
public class GridCell {

    private int row;
    private int col;
    private byte occupancy;
    private int heuristicValue;
    private int movementCost;
    private GridCell parent;

    public GridCell(int row, int col)  {
        this.row = row;
        this.col = col;
    }


    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public byte getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(byte occupancy) {
        this.occupancy = occupancy;
    }

    public int getHeuristicValue() {
        return heuristicValue;
    }

    public void setHeuristicValue(int heuristicValue) {
        this.heuristicValue = heuristicValue;
    }

    public int getMovementCost() {
        return movementCost;
    }

    public void setMovementCost(int movementCost) {
        this.movementCost = movementCost;
    }

    public GridCell getParent() {
        return parent;
    }

    public void setParent(GridCell parent) {
        this.parent = parent;

    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null) {
            return false;
        }
        if(obj.getClass()!=this.getClass()) {
            return false;
        }
        GridCell otherCell = (GridCell) obj;
        return (this.getRow()==otherCell.getRow()) && (this.getCol()==otherCell.getCol());
    }

    @Override
    public String toString() {
        return "row: "+this.row+" col: "+this.col;
    }
}
