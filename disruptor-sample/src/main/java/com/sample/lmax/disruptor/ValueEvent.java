package com.sample.lmax.disruptor;

import com.lmax.disruptor.EventFactory;

public class ValueEvent {
    
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public final static EventFactory<ValueEvent> EVENT_FACTORY = ValueEvent::new;
}
