package org.example.Events;

import org.example.game.Block;
import org.example.util.Event;

public class BlockShiftEvent extends Event {

    private final Block took;
    private final Block hold;

    public BlockShiftEvent(Block took, Block hold){
        this.hold = hold;
        this.took = took;
    }

    public Block getHold() {
        return hold;
    }

    public Block getTook() {
        return took;
    }
}
