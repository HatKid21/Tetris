package org.example.game;

import org.example.Events.*;
import org.example.input.EventManager;
import org.example.render.GameApplication;
import org.example.render.Scene;
import org.example.render.Window;
import org.example.util.Matrix2D;
import org.example.util.Pos;
import org.example.util.TextToSceneConverter;

import java.awt.Color;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TetrisGame {

    private Timer lockDelay;
    private boolean isLocked = false;

    private final BlockQueue blockQueue = new BlockQueue();
    private boolean gameOver = false;

    private Block heldBlock;
    private boolean canHoldPiece = true;

    private Block ghostBlock;
    private Score score;

    private final Scene gridScene;
    private final Grid grid;
    private static final Pos blockSpawnPos = new Pos(4, 1);
    private Block currentBlock;

    private final int softDropSpeed = 20;

    private Thread gameThread;
    private int gameSpeed = 500;
    private volatile boolean skipSleep = false;
    private volatile boolean softDropActive = false;
    private final AtomicInteger loopsNotLanded = new AtomicInteger(0);

    private Scene emptyHoldBlockScene;
    private Scene holdBlockScene;

    private Scene emptyNextBlocks;
    private Scene nextBlocks;

    private Window window;

    public TetrisGame() {
        grid = new Grid();
        new GameApplication(this);
        currentBlock = blockQueue.nextBlock();
        setupWindow();
        gridScene = new Scene(grid.getVisualGrid(), window.getBackgroundData("GridFrame").getCoordinate().add(1, 0));
        window.changeForegroundContent("GameGrid",gridScene);
        EventManager.emit(new BlockSpawnEvent(currentBlock));
        EventManager.subscribe(GameOverEvent.class, this::gameOver);
        EventManager.subscribe(LevelUpEvent.class, this:: updateGameSpeed);
    }

    private void updateGameSpeed(LevelUpEvent event){
        gameSpeed -= (int) (gameSpeed * 0.1);
    }

    private void setupWindow(){
        window = new org.example.render.Window(35,22);
        window.changeBackgroundContent("GridFrame", TextToSceneConverter.convert("Visual\\gameGridFrame.txt",new Pos(7,0)));

        Pos nextBlockPos = window.getBackgroundData("GridFrame").getCoordinate();
        nextBlockPos = nextBlockPos.add(window.getBackgroundData("GridFrame").getWidth(),0);
        emptyNextBlocks = TextToSceneConverter.convert("Visual\\nextBlocks.txt",nextBlockPos);
        assert emptyNextBlocks != null;
        nextBlocks = emptyNextBlocks.clone();
        window.changeBackgroundContent("NextBlocks", nextBlocks);
        updateNextBlocks();

        this.score = new Score();
        score.getScoreScene().setCoordinate(nextBlockPos.add(0,emptyNextBlocks.getHeight()));
        window.changeForegroundContent("GameData",score.getScoreScene());

        emptyHoldBlockScene = TextToSceneConverter.convert("Visual\\holdPiece.txt");
        holdBlockScene = emptyHoldBlockScene.clone();
        window.changeBackgroundContent("HoldPiece",holdBlockScene);
    }

    private void gameOver(GameOverEvent event) {
        System.out.println("Game Over!");
        cancelLockDelay();
        gameOver = true;
        if (gameThread != null) {
            gameThread.interrupt();
            gameThread = null;
        }
    }

    public void holdPiece() {
        if (canHoldPiece) {
            if (heldBlock == null) {
                currentBlock.setCoordinate(blockSpawnPos);
                heldBlock = currentBlock.cloneBlockWithDefaultPosition();
                currentBlock = blockQueue.nextBlock();
                holdBlockScene.addContentToScene(heldBlock.getShape(),new Pos(holdBlockScene.getWidth()/2,holdBlockScene.getHeight()/2));
                updateNextBlocks();
            } else {
                Block temp = heldBlock;
                currentBlock.setCoordinate(blockSpawnPos);
                heldBlock = currentBlock.cloneBlockWithDefaultPosition();
                currentBlock = temp;
                holdBlockScene = emptyHoldBlockScene.clone();
                holdBlockScene.addContentToScene(heldBlock.getShape(),new Pos(holdBlockScene.getWidth()/2-1,holdBlockScene.getHeight()/2+1));
                window.changeBackgroundContent("HoldPiece",holdBlockScene);
            }
        } else {
            return;
        }
        canHoldPiece = false;
        EventManager.emit(new BlockShiftEvent(currentBlock,heldBlock));
        gameGridUpdate();
        EventManager.emit(new UIUpdateEvent(window));
    }

    public static Pos getBlockSpawnPos() {
        return blockSpawnPos;
    }

    private void updateNextBlocks(){
        nextBlocks = emptyNextBlocks.clone();
        List<Block> nextBlocksList = blockQueue.peekBlock();
        int xCoord = window.getBackgroundData("NextBlocks").getWidth() / 2;
        for (int i = 0; i < 5; i++) {
            Shape shape = nextBlocksList.get(i).getShape();
            nextBlocks.addContentToScene(shape,new Pos(xCoord,i*3+3));
        }
        window.changeBackgroundContent("NextBlocks",nextBlocks);
    }

    public void start() {
        gameThread = new Thread(() -> {
            while (!gameOver) {
                proceedGameLoop();
                if (!skipSleep) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(softDropActive ? softDropSpeed : gameSpeed);
                    } catch (InterruptedException e) {
                        if (gameOver) {
                            return;
                        }
                        throw new RuntimeException(e);
                    }
                }
                skipSleep = false;
                gridScene.updateContent(grid.getVisualGrid());
                window.changeForegroundContent("GameGrid", gridScene);
                EventManager.emit(new UIUpdateEvent(window));

            }
        });
        gameThread.start();
    }

    private void proceedGameLoop(){
        if(isValidMove(currentBlock,Pos.DOWN)){
            Block beforeMoving = currentBlock.cloneBlock();
            move(Pos.DOWN);
            EventManager.emit(new BlockMovedEvent(beforeMoving, currentBlock));
            loopsNotLanded.set(0);
        } else{
            int temp = loopsNotLanded.getAndIncrement();
            if (temp >= 2 && !softDropActive){
                placeBlock();
                loopsNotLanded.set(0);
            } else if (temp > 50 && softDropActive) {
                placeBlock();
                loopsNotLanded.set(0);
            }
        }
    }

    private void placeBlock() {
        isLocked = false;
        canHoldPiece = true;
        EventManager.emit(new BlockLandedEvent(currentBlock));
        currentBlock = blockQueue.nextBlock();
        updateGhostBlock();
        EventManager.emit(new BlockSpawnEvent(currentBlock));
        if (gameOver){
            return;
        }
        window.changeForegroundContent("GameData",score.getScoreScene());
        gameGridUpdate();
        updateNextBlocks();
        EventManager.emit(new UIUpdateEvent(window));
    }

    private void startLockDelay() {
        if (!isValidMove(currentBlock, Pos.DOWN)) {
            isLocked = true;
            if (lockDelay == null) {
                lockDelay = new Timer();
                lockDelay.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        placeBlock();
                    }
                }, 500);
            }
        }
    }

    public void move(Pos moving) {
        if (moving.equals(Pos.DOWN) && !isValidMove(currentBlock, moving)) {
            if (!isLocked) {
                startLockDelay();
            }
            return;
        } else if (isValidMove(currentBlock, moving)) {
            Block beforeMoving = currentBlock.cloneBlock();
            currentBlock.move(moving);
            updateGhostBlock();
            EventManager.emit(new BlockMovedEvent(beforeMoving, currentBlock));

            if (isValidMove(currentBlock, Pos.DOWN)) {
                cancelLockDelay();
            }

        }
        gameGridUpdate();
        EventManager.emit(new UIUpdateEvent(window));
    }

    private void updateGhostBlock(){
        Block previousGhostBlock = ghostBlock;
        simulateHardDrop();
        EventManager.emit(new GhostBlockUpdateEvent(previousGhostBlock,ghostBlock));
    }

    private void simulateHardDrop(){
        ghostBlock = currentBlock.cloneBlock();
        ghostBlock.getShape().setOccupiesCell(false);
        ghostBlock.getShape().changeColor(Color.GRAY);
        while (isValidMove(ghostBlock, Pos.DOWN)) {
            ghostBlock.move(Pos.DOWN);
        }
    }

    private boolean isTouchingGround() {
        return !isValidMove(currentBlock, Pos.DOWN);
    }

    public boolean isValidMove(Block block, Pos moving) {
        Pos coordinate = block.getCoordinate().add(moving);
        for (Pos pos : block.getShape().getShapePositions()) {
            Pos newPos = coordinate.add(pos);
            if (grid.isOccupied(newPos)) {
                return false;
            }
        }
        return true;
    }

    public void setSoftDrop(boolean softDrop) {
        this.softDropActive = softDrop;
    }

    public void hardDrop() {
        cancelLockDelay();
        while (isValidMove(currentBlock, Pos.DOWN)) {
            currentBlock.move(Pos.DOWN);
        }
        placeBlock();
        skipSleep = true;
        EventManager.emit(new BlockSpawnEvent(currentBlock));
        gameGridUpdate();
        EventManager.emit(new UIUpdateEvent(window));
    }

    private void cancelLockDelay() {
        if (lockDelay != null) {
            lockDelay.cancel();
            lockDelay = null;
        }
    }

    private void gameGridUpdate(){
        gridScene.updateContent(grid.getVisualGrid());
        window.changeForegroundContent("GameGrid",gridScene);
    }

    public void rotate(Matrix2D rotation) {
        Block temp = currentBlock.cloneBlock();
        if (isValidRotation(currentBlock, rotation)) {
            currentBlock.rotate(rotation);
            updateGhostBlock();
            EventManager.emit(new BlockRotatedEvent(rotation, temp, currentBlock));
            gameGridUpdate();
            EventManager.emit(new UIUpdateEvent(window));

        } else {
            tryShift(rotation);
        }
        if (!isTouchingGround()) {
            cancelLockDelay();
        }
    }

    private void tryShift(Matrix2D rotation) {
        Block clone = currentBlock.cloneBlock();
        Block initBlock = currentBlock;
        for (int j = 1; j > -2; j--) {
            for (int i = 1; i > -2; i--) {
                if (i != 0 || j != 0) {
                    Pos shiftedCoord = initBlock.getCoordinate().add(new Pos(i, j));
                    clone.setCoordinate(shiftedCoord);
                    if (isValidRotation(clone, rotation)) {
                        currentBlock.setCoordinate(shiftedCoord);
                        currentBlock.rotate(rotation);
                        EventManager.emit(new BlockRotatedEvent(rotation, initBlock, currentBlock));
                        gameGridUpdate();
                        EventManager.emit(new UIUpdateEvent(window));
                        return;
                    }
                }
            }
        }
    }

    private boolean isValidRotation(Block block, Matrix2D rotation) {
        for (Pos pos : block.getShape().getShapePositions()) {
            Pos newPos = rotation.multiply(pos);
            newPos = block.getCoordinate().add(newPos);
            if (grid.isOccupied(newPos)) {
                return false;
            }
        }
        return true;
    }


}
