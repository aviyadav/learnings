package com.sample.lmax.disruptor.vrc;

import com.lmax.disruptor.EventHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Handler implements EventHandler<Event> {
    
    private String name;

    public Handler(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(Event event, long sequence, boolean batchEnd) throws Exception {
        event.add(name + "-" + sequence + "|");
        System.out.println(event);
    }

}
