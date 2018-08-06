package com.sample.lmax.disruptor.vrc;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Publisher implements EventTranslator<Event>, Runnable {
    
    private Disruptor disruptor;
    private String name;
    private long count;
    private boolean stop;
    private boolean slowDown;

    public Publisher(String name, long count, Disruptor disruptor) {
        this.name = name;
        this.count = count;
        this.disruptor = disruptor;
    }

    @Override
    public void translateTo(Event event, long sequence) {
        event.setValue(name + "-" + sequence + "|");
        System.out.println(event);
    }

    @Override
    public void run() {
        int i = 0;
        while(count >= i && !stop) {
            disruptor.publishEvent(this);
            ++i;
            if(slowDown) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Publisher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public Publisher stop() {
        stop = true;
        return this;
    }
    
    public Publisher goSlow() {
        slowDown = true;
        return this;
    }

}
