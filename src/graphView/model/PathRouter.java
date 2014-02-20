package graphView.model;

import java.util.*;

/**
 * PathRouter implements the
 * A* search algorithm to find a path
 * between 2 cells
 * @author Jayen kumar Jaentilal k1189304
 */
public class PathRouter {

    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;
    private byte traversableValue;//the occupancy value the path planner is allowed to walk
    private ViewGrid viewGrid;

    private PriorityQueue<GridCell> unvisitedQueue;// A queue of GridCells whose lowest cost has not been found
    private Set<GridCell> settledCells;// The set of GridCells whose lowest cost has been found
    private Map<GridCell,GridCell> smallestCostMap;//Map maintains all the cells that have been explored and being explored
    private ArrayList<GridCell> route;

    private static int straightCost = 4;
    private static int diagnolCost = 2;

    public PathRouter(int startRow,int startCol, int endRow, int endCol, byte traversableValue,ViewGrid viewGrid) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.traversableValue = traversableValue;
        this.viewGrid = viewGrid;
        setupDataStructures();
    }

    /**
     * Method to initialise all
     * the data structures used for path planning
     */
    private void setupDataStructures() {
        Comparator<GridCell> gridCellComparator = new Comparator<GridCell>() {
            @Override
            public int compare(GridCell gridCell1, GridCell gridCell2) {
                return Integer.compare(gridCell1.getMovementCost()+gridCell1.getHeuristicValue(),
                                       gridCell2.getMovementCost()+gridCell2.getHeuristicValue());
            }
        };
        unvisitedQueue = new PriorityQueue<GridCell>(50,gridCellComparator);
        settledCells = new HashSet<GridCell>(150);
        smallestCostMap = new HashMap<GridCell, GridCell>(150);
        route = new ArrayList<GridCell>(50);
    }

    public List<GridCell> getPath() {
        System.out.println("finding path");
        GridCell currentCell = new GridCell(startRow, startCol);
        currentCell.setOccupancy(viewGrid.getOccupancyGridValue(startRow, startCol));
        currentCell.setParent(null);
        currentCell.setHeuristicValue(calculateHeuristic(startRow, startCol, endRow, endCol));
        currentCell.setMovementCost(calculateMovementCost(null, 0));
        smallestCostMap.put(currentCell, currentCell);
        unvisitedQueue.add(currentCell);
        addChildCellsToUnvisitedQueue(currentCell);
        settledCells.add(currentCell);

        boolean currentCellIsEndCell;

        while(!unvisitedQueue.isEmpty()) {
            currentCell = unvisitedQueue.poll();
            currentCellIsEndCell = currentCell.getRow()==endRow && currentCell.getCol()==endCol;
            if(currentCellIsEndCell) {
                break;//we reached the end cell
            }
            addChildCellsToUnvisitedQueue(currentCell);
            settledCells.add(currentCell);
        }
        System.out.println("found path");
        return computePath(currentCell);
    }

    /**
     * Method to compute the path from
     * start cell to end cell
     * @param currentCell
     * @return List<GridCell> path
     */
    private List<GridCell> computePath(GridCell currentCell) {
        ArrayList<GridCell> path = new ArrayList<GridCell>();
        while(currentCell.getParent()!=null) {
            path.add(currentCell);
            currentCell = currentCell.getParent();
        }
        return path;
    }

    private int calculateMovementCost(GridCell parentCell, int cost) {
        if(smallestCostMap.get(parentCell)==null) {//if the parent cell is null then the original cell is the starting cell
            return cost;
        }
        else {
            return smallestCostMap.get(parentCell).getMovementCost()+cost;
        }
    }

    private int calculateHeuristic(int currenctCellRow, int currentCellCol, int endRow, int endCol) {
        int rowDiff = Math.abs(currenctCellRow-endRow);
        int colDiff = Math.abs(currentCellCol-endCol);
        return rowDiff+colDiff;
    }

    private void addChildCellsToUnvisitedQueue(GridCell parentCell) {
        int parentsRow=parentCell.getRow();
        int parentsCol=parentCell.getCol();

        // the first cell (top left), the last cell (down right)
        int firstRowCell = parentsRow - 1;
        int firstColCell = parentsCol - 1;
        int lastCellsRow = parentsRow + 1;
        int lastCellsCol = parentsCol + 1;

        int heuristic;
        int movementCost;
        boolean NotOutOfBounds;
        boolean isCuttingCorners;

        for(int row=firstRowCell; row<=lastCellsRow; row++) {
            for(int col=firstColCell; col<=lastCellsCol; col++) {
                NotOutOfBounds = row>=0 && col>=0 && row<viewGrid.rowLength() && col<viewGrid.colLength();
                if(!(row==parentsRow && col==parentsCol) && NotOutOfBounds) {
                    GridCell cell;
                    if(viewGrid.getOccupancyGridValue(row,col)==traversableValue) {
                        isCuttingCorners= isCuttingCorners(row, col, parentsRow, parentsCol);
                        if(!isSettled(row,col) && !isCuttingCorners) {
                            cell = new GridCell(row,col);
                            if(!cellisDiagonal(cell,parentCell)) {
                                heuristic = calculateHeuristic(row,col,endRow,endCol);
                                cell.setHeuristicValue(heuristic);
                                if(cellisDiagonal(cell, parentCell)) {
                                    movementCost = calculateMovementCost(parentCell,diagnolCost);
                                    cell.setMovementCost(movementCost);
                                }
                                else {
                                    movementCost = calculateMovementCost(parentCell,straightCost);
                                    cell.setMovementCost(movementCost);
                                }
                                //we check if the current cost is less than the one we have calculated before
                                if(cell.getHeuristicValue()+cell.getMovementCost()<getSmallestCost(cell)) {
                                    setSmallestCost(cell);
                                    cell.setParent(parentCell);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * set the new smallest cost
     * @param cell -The cell to backtrack from
     */
    private void setSmallestCost(GridCell cell) {
        unvisitedQueue.remove(cell);
        smallestCostMap.put(cell,cell);
        unvisitedQueue.add(cell);
    }

    /**
     * Get the current smallest cost of the cell
     * Integer.Max_Value is returned if cell has no associated cost
     * @param cell -cell to check
     * @return int -the smallest cost
     */
    private int getSmallestCost(GridCell cell) {
        if(smallestCostMap.containsKey(cell)) {
            return smallestCostMap.get(cell).getMovementCost()+smallestCostMap.get(cell).getHeuristicValue();
        }
        else {
            return Integer.MAX_VALUE;//represents infinity
        }
    }

    /**
     * Method to check if a cell
     * is diagonal to the parent cell
     * @param cell
     * @param parentCell
     * @return
     */
    private boolean cellisDiagonal(GridCell cell, GridCell parentCell) {
        //check diagonal north east
        if(cell.getRow()==parentCell.getRow()-1 && cell.getCol()==parentCell.getCol()-1) {
            return true;
        }
        //check diagonal north west
        else if(cell.getRow() ==parentCell.getRow()-1 && cell.getCol()==parentCell.getCol()+1) {
            return true;
        }
        //check diagonal south east
        else if(cell.getRow()==parentCell.getRow()+1 && cell.getCol()==parentCell.getCol()-1) {
            return true;
        }
        //check diagonal south west
        else if(cell.getRow()==parentCell.getRow()+1 && cell.getCol()==parentCell.getCol()+1) {
            return true;
        }
        return false;
    }

    /**
     * Method to check if a certain
     * cell is in the settled set
     * @param row
     * @param col
     * @return true if the cell is in the settled set else false
     */
    private boolean isSettled(int row, int col) {
        settledCells.contains(new GridCell(row,col));//contains only needs to make sure that the row and cell are equal
        return false;
    }

    /**
     * Method to check if a certain cell
     * is cutting a corner between 2 blocked cells
     * @param row
     * @param col
     * @param parentsRow
     * @param parentsCol
     * @return -boolean true if cutting corners else false
     */
    private boolean isCuttingCorners(int row, int col, int parentsRow, int parentsCol) {
        //check diagonal north east
        if(row==parentsRow-1 && col==parentsCol-1) {
            //check to see if the east and the north cells are not traversable
            if(viewGrid.getOccupancyGridValue(parentsRow,parentsCol-1)!=traversableValue
               && viewGrid.getOccupancyGridValue(parentsRow-1,parentsCol)!=traversableValue) {
                return true;
            }
            return false;
        }
        //check diagonal north west
        else if(row ==parentsRow-1 && col==parentsCol+1) {
            //check to see if the west and the north cells are not traversable
            if(viewGrid.getOccupancyGridValue(parentsRow,parentsCol+1)!=traversableValue
               && viewGrid.getOccupancyGridValue(parentsRow-1,parentsCol)!=traversableValue) {
                return true;
            }
            return false;
        }
        //check diagonal south east
        else if(row==parentsRow+1 && col==parentsCol-1) {
            //check to see if the east and the south cells are not traversable
            if(viewGrid.getOccupancyGridValue(parentsRow,parentsCol-1)!=traversableValue
               && viewGrid.getOccupancyGridValue(parentsRow+1,parentsCol)!=traversableValue) {
                return true;
            }
            return false;
        }
        //check diagonal south west
        else if(row==parentsRow+1 && col==parentsCol+1) {
            //check to see if the west and the south cells are not traversable
            if(viewGrid.getOccupancyGridValue(parentsRow,parentsCol+1)!=traversableValue
               && viewGrid.getOccupancyGridValue(parentsRow+1,parentsCol)!=traversableValue) {
                return true;
            }
            return false;
        }
        return false;
    }

}