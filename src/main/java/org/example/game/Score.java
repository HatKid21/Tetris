package org.example.game;

import org.example.Events.RowClearedEvent;
import org.example.input.EventManager;
import org.example.render.Cell;
import org.example.render.Scene;

import java.awt.*;

public class Score {

    private long score;
    private long level;
    private Scene scoreScene;
    private long linesCleared;
    private float combo;

    public Score(){
        this.score = 0;
        this.level = 1;
        this.linesCleared = 0;
        this.combo = 0;
        initScoreScene();
        applyScoreToScene();
        EventManager.subscribe(RowClearedEvent.class, this::applyScore);
    }

    private void initScoreScene(){
        this.scoreScene = new Scene(new Cell[3][15]);
        int col = 0;
        for (char c : "Score:".toCharArray()){
            scoreScene.getContent()[0][col] = new Cell(Color.WHITE,c,false);
            col++;
        }
        col = 0;
        for (char c : "Level:".toCharArray()){
            scoreScene.getContent()[1][col] = new Cell(Color.WHITE,c,false);
            col++;
        }
    }

    private void applyScoreToScene(){
        String stringScore = String.valueOf(score);
        int col = 7;
        for(char c : stringScore.toCharArray()){
            if (col >= scoreScene.getContent()[0].length){
                continue;
            }
            scoreScene.getContent()[0][col] = new Cell(Color.WHITE,c,false);
            col++;
        }
        col = 7;
        String stringLevel = String.valueOf(level);
        for (char c : stringLevel.toCharArray()){
            if (col >= scoreScene.getContent()[0].length){
                continue;
            }
            scoreScene.getContent()[1][col] = new Cell(Color.WHITE,c,false);
            col++;
        }
    }

    private void applyScore(RowClearedEvent event){
        long rowsCleared = event.getRowsCleared();
        if (rowsCleared == 0){
            combo = 1;
        } else{
            combo += 0.5f;
        }
        linesCleared += rowsCleared;
        if (rowsCleared == 0){
            score += 10;
        } else{
            score += (long) (combo * level * 100 * rowsCleared);
        }
        level = linesCleared / 10 + 1;
        applyScoreToScene();
    }

    public Scene getScoreScene() {
        return scoreScene;
    }

    public long getLevel() {
        return level;
    }

    public long getScore() {
        return score;
    }

}
