package org.example.render;

import org.example.game.Block;

import java.awt.*;

public class Cell {

    private final Color color;
    private final char val;
    private final boolean isOccupied;

    private static final Cell EMPTY_CELL = new Cell(Color.BLACK,' ',false);

    public Cell(Block block){
        this.color = block.getShape().getColor();
        this.val = block.getShape().getVal();
        this.isOccupied = true;
    }

    public static Cell getEmptyCell() {
        return EMPTY_CELL;
    }

    public Cell(Color color, char val, boolean isOccupied){
        this.color = color;
        this.val = val;
        this.isOccupied = isOccupied;
    }

    public Color getColor() {
        return color;
    }

    public char getVal() {
        return val;
    }

    public boolean isOccupied() {
        return isOccupied;
    }
}
