package org.example.input;

import org.example.util.Event;

public interface EventListener<T extends Event>{

    void handle(T event);

}
