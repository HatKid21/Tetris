package org.example.Events;

import org.example.game.Block;
import org.example.util.Event;

public class BlockMovedEvent extends Event {

    private final Block beforeMoving;
    private final Block afterMoving;

    public BlockMovedEvent(Block beforeMoving,Block afterMoving){
        this.afterMoving = afterMoving;
        this.beforeMoving = beforeMoving;
    }

    public Block getAfterMoving() {
        return afterMoving;
    }

    public Block getBeforeMoving() {
        return beforeMoving;
    }
}
