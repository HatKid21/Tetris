package org.example.Events;

import org.example.util.Event;

public class LevelUpEvent extends Event {

    private final long previousLevel;
    private final long currentLevel;

    public LevelUpEvent(long previousLevel, long currentLevel){
        this.currentLevel = currentLevel;
        this.previousLevel = currentLevel;
    }

    public long getCurrentLevel() {
        return currentLevel;
    }

    public long getPreviousLevel() {
        return previousLevel;
    }

}
