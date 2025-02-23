package org.example.render;

import org.example.game.Shape;
import org.example.util.Pos;

import java.awt.*;
import java.util.List;

public class Scene {

    private Cell[][] content;
    private Pos coordinate;
    private int width;
    private int height;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Scene clone(){
        return new Scene(this);
    }

    public Scene(Scene originalScene){
        this.width = originalScene.width;
        this.height = originalScene.height;
        this.coordinate = new Pos(originalScene.coordinate.x(),originalScene.coordinate.y());
        this.content = new Cell[height][width];
        Cell[][] originalSceneContent = originalScene.content;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = originalSceneContent[i][j];
                if (originalScene.content[i][j] != null) {
                    this.content[i][j] = new Cell(cell.getColor(), cell.getVal(), cell.isOccupied());
                } else {
                    this.content[i][j] = Cell.getEmptyCell();
                }
            }
        }
    }

    public Scene(Cell[][] context){
        this.content = context;
        this.coordinate = new Pos(0,0);
        this.width = context[0].length;
        this.height = context.length;
    }

    public Scene(Cell[][] context, Pos coordinate){
        this.content = context;
        this.coordinate = coordinate;
        this.width = context[0].length;
        this.height = context.length;
    }

    public void addContentToScene(Shape shape, Pos coordinate){
        List<Pos> posList = shape.getShapePositions();
        Color color = shape.getColor();
        char val = shape.getVal();
        for (Pos pos : posList){
            Pos newPos = coordinate.add(pos);
            if (newPos.x() >= 0 && newPos.x() < width && newPos.y() >= 0 && newPos.y() < height){
                content[newPos.y()][newPos.x()] = new Cell(color,val,false);
            } else{
                System.out.println(newPos);
            }
        }
    }

    public void setCoordinate(Pos coordinate){
        this.coordinate = coordinate;
    }

    public void updateContent(Cell[][] newContent){
        this.content = newContent;
    }

    public Pos getCoordinate() {
        return coordinate;
    }

    public Cell[][] getContent() {
        return content;
    }
}
