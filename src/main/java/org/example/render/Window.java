package org.example.render;

import org.example.util.Pos;

import java.util.HashMap;
import java.util.Map;

public class Window {

    private Map<String,Scene> foreGround;
    private Map<String,Scene> backGround;

    private final Cell[][] visual;
    private int width;
    private int height;

    public Scene getBackgroundData(String name){
        if (backGround.containsKey(name)){
            return backGround.get(name);
        }
        return null;
    }

    public Scene getForeground(String name){
        if (foreGround.containsKey(name)){
            return foreGround.get(name);
        }
        return null;
    }

    public Window(int width,int height){
        this.visual = new Cell[height][width];
        this.width = width;
        this.height = height;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                visual[y][x] = Cell.getEmptyCell();
            }
        }
        this.foreGround = new HashMap<>();
        this.backGround = new HashMap<>();
    }

    public void changeForegroundContent(String name, Scene content){
        foreGround.put(name,content);
        applyScenesToWindow();
    }

    public void changeBackgroundContent(String name, Scene content){
        backGround.put(name,content);
        applyScenesToWindow();
    }

    private void applyScenesToWindow(){
        for (Scene scene : backGround.values()){
            Pos coordinate = scene.getCoordinate();
            Cell[][] content = scene.getContent();
            for (int y = 0; y < content.length; y++) {
                for (int x = 0; x < content[0].length; x++) {
                    int xCoord = x + coordinate.x();
                    int yCoord = y + coordinate.y();
                    if (xCoord >= 0 && xCoord < width && yCoord >= 0 && yCoord < height){
                        visual[yCoord][xCoord] = content[y][x];
                    }
                }
            }
        }
        for (Scene scene : foreGround.values()){
            Pos coordinate = scene.getCoordinate();
            Cell[][] content = scene.getContent();
            for (int y = 0; y < content.length; y++) {
                for (int x = 0; x < content[0].length; x++) {
                    int xCoord = x + coordinate.x();
                    int yCoord = y + coordinate.y();
                    if (xCoord >= 0 && xCoord < width && yCoord >= 0 && yCoord < height){
                        visual[yCoord][xCoord] = content[y][x];
                    }
                }
            }
        }
    }

    public Cell[][] getVisual() {
        return visual;
    }
}
