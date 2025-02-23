package org.example.Events;

import org.example.util.Event;

public class RowClearedEvent extends Event {

    private final int rowsCleared;

    public RowClearedEvent(int rowsCleared){
        this.rowsCleared = rowsCleared;
    }

    public int getRowsCleared() {
        return rowsCleared;
    }
}
