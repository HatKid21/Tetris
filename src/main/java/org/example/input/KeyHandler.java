package org.example.input;

import org.example.util.Matrix2D;
import org.example.util.Pos;
import org.example.game.TetrisGame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class KeyHandler extends KeyAdapter {

    private final Set<Integer> heldKeys = new HashSet<>();
    private Timer timer;
    private Pos currentMoving;
    private final TetrisGame game;

    public KeyHandler(TetrisGame game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (heldKeys.contains(keyCode) && keyCode != KeyEvent.VK_DOWN){
            return;
        }

        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT){
            if (heldKeys.contains(KeyEvent.VK_RIGHT)){
                heldKeys.remove(KeyEvent.VK_RIGHT);
                stopRepeatingMove();
            }
            if (heldKeys.contains(KeyEvent.VK_LEFT)){
                heldKeys.remove(KeyEvent.VK_LEFT);
                stopRepeatingMove();
            }
        }

        heldKeys.add(keyCode);
        if (keyCode == KeyEvent.VK_LEFT) {
            game.move(Pos.LEFT);
            startRepeatingMove(Pos.LEFT);
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            game.move(Pos.RIGHT);
            startRepeatingMove(Pos.RIGHT);
        } else if (keyCode == KeyEvent.VK_SPACE) {
            game.hardDrop();
        } else if (keyCode == KeyEvent.VK_A) {
            game.rotate(Matrix2D.ANTICLOCKWISE);
        } else if (keyCode == KeyEvent.VK_D) {
            game.rotate(Matrix2D.CLOCKWISE);
        } else if (keyCode == KeyEvent.VK_S) {
            game.rotate(Matrix2D.FLIP);
        } else if (keyCode == KeyEvent.VK_DOWN) {
            game.setSoftDrop(true);
        } else if (keyCode == KeyEvent.VK_SHIFT){
            game.holdPiece();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        heldKeys.remove(keyCode);

        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
            if (heldKeys.contains(KeyEvent.VK_LEFT)) {
                startRepeatingMove(Pos.LEFT);
            } else if (heldKeys.contains(KeyEvent.VK_RIGHT)) {
                startRepeatingMove(Pos.RIGHT);
            } else {
                stopRepeatingMove();
            }
        }

            if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_LEFT){
            stopRepeatingMove();
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            game.setSoftDrop(false);
        }
    }

    private void startRepeatingMove(Pos pos) {
        if (currentMoving != pos){
            stopRepeatingMove();
            currentMoving = pos;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    game.move(pos);
                }
            },90,50);
        }
    }

    private void stopRepeatingMove() {
        if (heldKeys.contains(KeyEvent.VK_LEFT) || heldKeys.contains(KeyEvent.VK_RIGHT)){
            return;
        }
        currentMoving = null;
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }


}
