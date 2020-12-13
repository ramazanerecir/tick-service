package com.rerecir.ticksserver.event.listener;

import org.springframework.context.ApplicationEvent;

public class TickEventCreated extends ApplicationEvent {

    public TickEventCreated(Object source) {
        super(source);
    }
}
