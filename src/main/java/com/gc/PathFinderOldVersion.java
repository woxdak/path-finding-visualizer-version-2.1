package com.gc;

import java.awt.Color;
import java.awt.Point;
import java.util.*;

public class PathFinderOldVersion {

    public static final int DIJKSTRA = 0;
    public static final int ASTAR = 1;
    private ArrayList<Cell>closedList;
    private PriorityQueue<Cell>openList;
    public Random random = new Random();

    private boolean isRunning;

    public PathFinderOldVersion(){
        openList = new PriorityQueue<Cell>();
        closedList = new ArrayList<Cell>();
    }

    public ArrayList<Cell> findShortestPath(Cell start, Cell goal, Grid grid, int step, int algorithm){
        start.setWall(false); //set cell to path
        Set<Cell> frontierCells = new HashSet<>(frontierCellsOf(start));


        isRunning = true;
        start.setDistanceFromStart(0);
        if(algorithm == ASTAR)
            start.setTotalCost(euclideanDistance(start.position, goal.position));
        if(algorithm == DIJKSTRA)
            start.setTotalCost(0);
        openList.add(start);

        long currentTime = System.currentTimeMillis();

        switch(algorithm){
            case DIJKSTRA: {
                while(!openList.isEmpty() && isRunning){

                    if(!isRunning)
                        break;

                    long timeSinceLastStep = System.currentTimeMillis() - currentTime;

                    if(timeSinceLastStep >= step){
                        currentTime = System.currentTimeMillis();

                        grid.update();

                        Cell current = openList.poll();
                        current.setColor(Color.GREEN);

                        if(algorithm == ASTAR)
                            closedList.add(current);

                        if(current == start)
                            start.setColor(Color.MAGENTA);

                        if(current == goal){
                            goal.setColor(Color.RED);
                            grid.update();
                            break;
                        }
                        for(Edge e : current.getEdges()){


                            Cell next = (Cell) e.getDestination();
                            double distanceFromStart = current.getDistanceFromStart() + e.getCost();
                            if(next.getColor() != Color.GREEN && next.getColor() != Color.MAGENTA)
                                next.setColor(Color.BLUE);

                            if(distanceFromStart < next.getDistanceFromStart()){
                                openList.remove(next);
                                next.setDistanceFromStart(distanceFromStart);
                                next.setTotalCost(distanceFromStart);
                                next.setPredecessor(current);
                                openList.add(next);
                            }



                        }
                    }
                }
                break;

            }
            case ASTAR: {



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
                break;
//							if(next.getColor() != Color.GREEN && next.getColor() != Color.MAGENTA)
//								next.setColor(Color.BLUE);
//

            }

        }

        ArrayList<Cell> shortestPath = new ArrayList<Cell>();
        Cell current = goal;
        shortestPath.add(current);

        while(current.getPredecessor()!= null){

            shortestPath.add((Cell)current.getPredecessor());
            current = (Cell)current.getPredecessor();

            if(current != start)
                current.setColor(Color.CYAN);
        }

        grid.update();
        Collections.reverse(shortestPath);

        return shortestPath;
    }

    public void stop(){
        isRunning = false;
    }

    public double euclideanDistance(Point p1, Point p2){
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }
    private static final int[][] DIRECTIONS = { //distance of 2 to each side
            { 0 ,-2}, // north
            { 0 , 2}, // south
            { 2 , 0}, // east
            {-2 , 0}, // west
    };






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
        frontierCellModelView.setColor(Color.BLACK);
        neighbour.setColor(Color.BLACK);
        Grid.cells[inBetweenRow][inBetweenCol].setColor(Color.BLACK);
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < Grid.cells.length
                && col >= 0 && col < Grid.cells[0].length;
    }

    public void createMaze(Cell start, Cell goal, Grid grid, int step){
        start.setWall(false); //set cell to path
        Set<Cell> frontierCells = new HashSet<>(frontierCellsOf(start));

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
}
