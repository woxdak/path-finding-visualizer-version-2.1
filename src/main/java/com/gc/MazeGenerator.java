package com.gc;


import java.awt.Color;
import java.awt.Point;
import java.util.*;

public class MazeGenerator {
    private boolean isRunning;
    public Random random = new Random();

    public void createMaze( Grid grid, int step){
        int x = random.nextInt(grid.cells.length/2) * 2;
        int y = random.nextInt(grid.cells[0].length/2)*2;

        grid.cells[x][y].setWall(false);


        Set<Cell> frontierCells = new HashSet<>(frontierCellsOf(grid.cells[x][y]));

        isRunning = true;
        long currentTime = System.currentTimeMillis();

        while (!frontierCells.isEmpty()) {
            long timeSinceLastStep = System.currentTimeMillis() - currentTime;
            if (timeSinceLastStep >= step) {

                currentTime = System.currentTimeMillis();

                grid.update();
                //Pick a random cell from the frontier collection
                Cell frontierCell = frontierCells.stream().skip(random.nextInt(frontierCells.size())).findFirst().orElse(null);

                //Get its neighbors: cells in distance 2 in state path (no wall)
                List<Cell> frontierNeighbors = passageCellsOf(frontierCell);

                if (!frontierNeighbors.isEmpty()) {
                    //Pick a random neighbor
                    Cell neighbor = frontierNeighbors.get(random.nextInt(frontierNeighbors.size()));
                    //Connect the frontier cell with the neighbor
                    connect(frontierCell, neighbor);
                }

                //Compute the frontier cells of the chosen frontier cell and add them to the frontier collection
                frontierCells.addAll(frontierCellsOf(frontierCell));
                //Remove frontier cell from the frontier collection
                frontierCells.remove(frontierCell);

            }
        }
        grid.update();
    }
    private static final int[][] DIRECTIONS = { //distance of 2 to each side
            { 0 ,-2}, // north
            { 0 , 2}, // south
            { 2 , 0}, // east
            {-2 , 0}, // west
    };



    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < Grid.cells.length
                && col >= 0 && col < Grid.cells[0].length;
    }

    //Frontier cells: wall cells in a distance of 2
    private List<Cell> frontierCellsOf(Cell cell) {

        return cellsAround(cell, true);
    }

    //Frontier cells: passage (no wall) cells in a distance of 2
    private List<Cell> passageCellsOf(Cell cell) {

        return cellsAround(cell, false);
    }

    private List<Cell> cellsAround(Cell cell, boolean isWall) {

        List<Cell> frontier = new ArrayList<>();
        for(int[] direction : DIRECTIONS){
            int newRow = cell.getRow() + direction[0];
            int newCol = cell.getColumn() + direction[1];
            if(isValidPosition(newRow, newCol) && Grid.cells[newRow][newCol].isWall() == isWall){
                frontier.add(Grid.cells[newRow][newCol]);
            }
        }

        return frontier;
    }

    //connects cells which are distance 2 apart
    private void connect( Cell frontierCellModelView, Cell neighbour) {

        int inBetweenRow = (neighbour.getRow() + frontierCellModelView.getRow())/2;
        int inBetweenCol = (neighbour.getColumn() + frontierCellModelView.getColumn())/2;
        frontierCellModelView.setWall(false);
        Grid.cells[inBetweenRow][inBetweenCol].setWall(false);
        neighbour.setWall(false);
        frontierCellModelView.setColor(Color.WHITE);
        neighbour.setColor(Color.WHITE);
        Grid.cells[inBetweenRow][inBetweenCol].setColor(Color.WHITE);
    }
}
