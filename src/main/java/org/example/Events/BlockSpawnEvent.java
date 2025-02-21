package org.example.Events;

import org.example.game.Block;
import org.example.util.Event;

public class BlockSpawnEvent extends Event {

    private final Block block;

    public BlockSpawnEvent(Block block){
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}
