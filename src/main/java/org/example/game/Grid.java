package org.example.game;

import org.example.Events.*;
import org.example.render.Cell;
import org.example.input.EventManager;
import org.example.util.Pos;

import java.util.Arrays;

public class Grid {

    public final int WIDTH = 10;
    public final int HEIGHT = 20;
    private Cell[][] visualGrid = new Cell[20][10];
    private final Cell[][] lockedGrid = new Cell[20][10];

    public Grid() {
        initGrid();
        EventManager.subscribe(BlockMovedEvent.class, this::move);
        EventManager.subscribe(BlockLandedEvent.class,this::lockBlock);
        EventManager.subscribe(BlockRotatedEvent.class, this::rotate);
        EventManager.subscribe(BlockSpawnEvent.class,this::spawn);
        EventManager.subscribe(BlockShiftEvent.class,this::updateHeldBlock);
        EventManager.subscribe(GhostBlockUpdateEvent.class,this::updateGhostBlock);
    }

    private void updateGhostBlock(GhostBlockUpdateEvent event){
        Block previousGhost = event.getPreviousGhostBlock();
        Block newGhost = event.getCurrentGhostBlock();
        if (previousGhost != null){
            clearBlock(previousGhost);
        }
        visualGrid = cloneGrid(lockedGrid);
        applyBlockToGrid(newGhost);
    }

    private void updateHeldBlock(BlockShiftEvent event){
        Block took = event.getTook();
        visualGrid = cloneGrid(lockedGrid);
        applyBlockToGrid(took);
    }

    private void initGrid(){
        for (int i = 0; i < lockedGrid.length; i++) {
            for (int j = 0; j < lockedGrid[0].length; j++) {
                lockedGrid[i][j] = Cell.getEmptyCell();
            }
        }
        for (int i = 0; i < visualGrid.length; i++) {
            for (int j = 0; j < visualGrid[0].length; j++) {
                visualGrid[i][j] = Cell.getEmptyCell();
            }
        }
    }

    private void lockBlock(BlockLandedEvent event){
        Block block = event.getBlock();
        Pos coord = block.getCoordinate();
        for (Pos pos : block.getShape().getShapePositions()){
            Pos newPos = coord.add(pos);
            if (isOccupied(newPos)){
                EventManager.emit(new GameOverEvent());
                return;
            }
            if (newPos.y() < 0 || newPos.x() > WIDTH || newPos.x() < 0){
                return;
            }
            lockedGrid[newPos.y()][newPos.x()] = new Cell(block);
        }
        clearLines();
        visualGrid = cloneGrid(lockedGrid);
    }

    private Cell[][] cloneGrid(Cell[][] grid){
        Cell[][] clonedGrid = new Cell[grid.length][grid[0].length];
        for (int y = 0; y < grid.length; y++) {
            System.arraycopy(grid[y], 0, clonedGrid[y], 0, grid[y].length);
        }
        return clonedGrid;
    }

    private void spawn(BlockSpawnEvent event){
        visualGrid = cloneGrid(lockedGrid);
        Block block = event.getBlock();
        Pos coord = block.getCoordinate();
        for (Pos pos : block.getShape().getShapePositions()){
            Pos newPos = coord.add(pos);
            if (isOccupied(newPos)){
                EventManager.emit(new GameOverEvent());
                return;
            }
        }
        applyBlockToGrid(event.getBlock());
    }

    private void clearBlock(Block block) {
        Pos coord = block.getCoordinate();
        Shape shape = block.getShape();
        for (Pos pos : shape.getShapePositions()) {
            Pos temp = coord.add(pos);
            if (temp.y() >= 0 && temp.y() < visualGrid.length) {
                visualGrid[temp.y()][temp.x()] = Cell.getEmptyCell();
            }
        }
    }

    public boolean isOccupied(Pos pos){
        int x = pos.x();
        int y = pos.y();
        if (y < 0){
            return false;
        }
        if (x < 0 || x >= WIDTH || y >= HEIGHT){
            return true;
        }
        return lockedGrid[pos.y()][pos.x()].isOccupied();
    }

    protected void move(BlockMovedEvent event){
        Block beforeMoving = event.getBeforeMoving();
        Block afterMoving = event.getAfterMoving();
        clearBlock(beforeMoving);
//        visualGrid = cloneGrid(lockedGrid);
        applyBlockToGrid(afterMoving);
    }

    public void applyBlockToGrid(Block block ){
        Pos coord = block.getCoordinate();

        for (Pos pos : block.getShape().getShapePositions()){
            Pos newPos = coord.add(pos);
            if (newPos.y() < 0){
                continue;
            }
            visualGrid[newPos.y()][newPos.x()] = new Cell(block);
        }

    }

    public void rotate(BlockRotatedEvent event){
        clearBlock(event.getBeforeRotation());
        applyBlockToGrid(event.getAfterRotation());
    }

    public Cell[][] getVisualGrid() {
        return visualGrid;
    }

    public String toString() {
        StringBuilder stringGrid = new StringBuilder();
        for (Cell[] cell : visualGrid) {
            for (int j = 0; j < visualGrid[0].length; j++) {
                stringGrid.append(cell[j].getVal());
            }
            stringGrid.append("\n");
        }
        return stringGrid.toString();

    }

    private boolean isRowFull(int y){
        for (int x = 0; x < lockedGrid[0].length; x++) {
            if (!lockedGrid[y][x].isOccupied()) return false;
        }
        return true;
    }

    private void removeRow(int y){
        for (int x = 0; x < lockedGrid[0].length; x++) {
            lockedGrid[y][x] = Cell.getEmptyCell();
        }
    }

    private void shiftRows(int from){
        for (int y = from; y > 0; y--) {
            System.arraycopy(lockedGrid[y - 1], 0, lockedGrid[y], 0, lockedGrid[0].length);
        }
        Arrays.fill(lockedGrid[0],  Cell.getEmptyCell());
    }

    protected void clearLines(){
        int rowsCleared = 0;
        for (int y = lockedGrid.length-1; y > -1; y--) {
            if (isRowFull(y)){
                rowsCleared++;
                removeRow(y);
                shiftRows(y);
                y++;
            }
        }
        visualGrid = cloneGrid(lockedGrid);
        EventManager.emit(new RowClearedEvent(rowsCleared));
    }
}
