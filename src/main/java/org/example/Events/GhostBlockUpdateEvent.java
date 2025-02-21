package org.example.Events;

import org.example.game.Block;
import org.example.util.Event;

public class GhostBlockUpdateEvent extends Event {

    private final Block previousGhostBlock;
    private final Block currentGhostBlock;

    public GhostBlockUpdateEvent(Block previousGhostBlock,Block currentGhostBlock){
        this.previousGhostBlock = previousGhostBlock;
        this.currentGhostBlock = currentGhostBlock;
    }

    public Block getPreviousGhostBlock() {
        return previousGhostBlock;
    }

    public Block getCurrentGhostBlock() {
        return currentGhostBlock;
    }
}
