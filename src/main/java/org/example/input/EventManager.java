package org.example.input;

import org.example.util.Event;
import org.example.Events.BlockLandedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    private static final Map<Class<? extends Event>, List<EventListener>> listeners = new HashMap<>();

    public static <T extends Event> void subscribe(Class<T> eventTye,EventListener<T> listener){
        listeners.computeIfAbsent(eventTye, k -> new ArrayList<>()).add(listener);
    }

    public static <T extends Event> void emit(T event){
        List<EventListener> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null){
            for (EventListener listener : eventListeners){
                listener.handle(event);
            }
        }
    }

    public static void emit(Class<BlockLandedEvent> blockLandedEventClass, Object clearLines) {

    }
}
