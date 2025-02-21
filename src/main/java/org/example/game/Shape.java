package org.example.game;


import org.example.util.Pos;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Shape {

    private boolean occupiesCell;
    private Color shapeColor;
    private final char val;
    private final List<Pos> shapePositions;

    public Shape(Color color,List<Pos> shapeData){
        this.val = '#';
        this.shapeColor = color;
        this.shapePositions = shapeData;
        this.occupiesCell = true;
    }

    private Shape(Shape other){
        this.val = other.val;
        this.shapeColor = new Color(other.getColor().getRGB());
        this.occupiesCell = other.occupiesCell;
        this.shapePositions = other.shapePositions.stream()
                .map(pos -> new Pos(pos.x(),pos.y()))
                .collect(Collectors.toList());
    }

    public Shape cloneShape(){
        return new Shape(this);
    }

    protected void setOccupiesCell(boolean state){
        this.occupiesCell = state;
    }

    public boolean isOccupiesCell() {
        return occupiesCell;
    }

    protected void changeColor(Color color){
        this.shapeColor = color;
    }

    public List<Pos> getShapePositions() {
        return shapePositions;
    }

    public char getVal() {
        return val;
    }

    public Color getColor() {
        return shapeColor;
    }
}
