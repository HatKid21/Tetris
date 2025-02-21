package org.example.Events;

import org.example.game.Block;
import org.example.util.Event;
import org.example.util.Matrix2D;

public class BlockRotatedEvent extends Event {

    private final Matrix2D rotation;
    private final Block beforeRotation;
    private final Block afterRotation;

    public BlockRotatedEvent(Matrix2D rotation,Block beforeRotation,Block afterRotation){
        this.rotation = rotation;
        this.beforeRotation = beforeRotation;
        this.afterRotation = afterRotation;
    }

    public Block getAfterRotation() {
        return afterRotation;
    }

    public Block getBeforeRotation() {
        return beforeRotation;
    }

    public Matrix2D getRotation() {
        return rotation;
    }
}
