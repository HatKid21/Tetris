package org.example.game;

import org.example.util.Matrix2D;
import org.example.util.Pos;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Block {

    private final Shape starterShape;
    private Shape shape;
    private Pos coordinate;

    private final List<Pos> bottomCells = new ArrayList<>();
    private final List<Pos> leftCells = new ArrayList<>();
    private final List<Pos> rightCells = new ArrayList<>();

    public Block(Shape shape){
        this.starterShape = shape;
        this.shape = shape;
        coordinate = TetrisGame.getBlockSpawnPos();
        updateBottomCells();
        updateSideCells();
    }

    public Block(Block other,boolean defaultShape){
        this.starterShape = other.starterShape;
        if (defaultShape){
            this.shape = other.starterShape.cloneShape();
        } else{
            this.shape = other.shape.cloneShape();
        }
        this.coordinate = other.coordinate;

        this.bottomCells.addAll(other.bottomCells);
        this.leftCells.addAll(other.leftCells);
        this.rightCells.addAll(other.rightCells);
    }

    private void updateBottomCells(){
        bottomCells.clear();
        Map<Integer, Integer> columnRow = new HashMap<>();
        for (Pos pos : shape.getShapePositions()){
            if (columnRow.containsKey(pos.x())){
                if (columnRow.get(pos.x()) < pos.y()){
                    columnRow.put(pos.x(), pos.y());
                }
            } else{
                columnRow.put(pos.x(), pos.y());
            }
        }
        for (int key : columnRow.keySet()){
            bottomCells.add(new Pos(key,columnRow.get(key)));
        }
    }

    private void updateSideCells(){
        leftCells.clear();
        rightCells.clear();
        Map<Integer, Integer> left = new HashMap<>();
        Map<Integer,Integer> right = new HashMap<>();
        for (Pos pos : shape.getShapePositions()){
            if (left.containsKey(pos.y())){
                if (left.get(pos.y()) > pos.x()){
                    left.put(pos.y(), pos.x());
                }
            } else{
                left.put(pos.y(), pos.x());
            }
            if (right.containsKey(pos.y())){
                if (right.get(pos.y()) < pos.x()){
                    right.put(pos.y(), pos.x());
                }
            } else{
                right.put(pos.y(), pos.x());
            }
        }
        for (int key : left.keySet()){
            leftCells.add(new Pos(left.get(key),key));
        }
        for (int key : right.keySet()){
            rightCells.add(new Pos(right.get(key),key));
        }
    }

    protected Pos getCoordinate(){
        return coordinate;
    }

    protected void setCoordinate(Pos coordinate){
        this.coordinate = coordinate;
    }

    protected void move(Pos pos){
        coordinate = coordinate.add(pos);
        updateSideCells();
        updateBottomCells();
    }

    public Shape getShape() {
        return shape;
    }

    public Block cloneBlockWithDefaultPosition() {
        return new Block(this,true);
    }

    public Block cloneBlock(){
        return new Block(this,false);
    }

    public void rotate(Matrix2D rotation){
        List<Pos> newShape = new ArrayList<>();
        Color color = shape.getColor();
        for (Pos pos : shape.getShapePositions()){
            Pos newPos = rotation.multiply(pos);
            newShape.add(newPos);
        }
        this.shape = new Shape(color,newShape);
        updateSideCells();
        updateBottomCells();
    }

}
