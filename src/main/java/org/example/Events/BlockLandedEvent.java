package org.example.Events;

import org.example.game.Block;
import org.example.util.Event;

public class BlockLandedEvent extends Event {

    private final Block block;

    public BlockLandedEvent(Block block){
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}
