package org.example.Events;

import org.example.render.Window;
import org.example.util.Event;

public class UIUpdateEvent extends Event {

    private final Window window;

    public UIUpdateEvent(Window window){
        this.window = window;
    }

    public Window getWindow() {
        return window;
    }
}
