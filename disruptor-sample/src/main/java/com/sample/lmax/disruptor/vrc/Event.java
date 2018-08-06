package com.sample.lmax.disruptor.vrc;

import com.lmax.disruptor.EventFactory;

public class Event {

    private String value;
    public static final EventFactory<Event> factory = Event::new;

    public void setValue(String value) {
        this.value = value;
    }
    
    public void add(String suffix) {
        this.value = value + "-" + suffix;
    }

    @Override
    public String toString() {
        return value;
    }
}
