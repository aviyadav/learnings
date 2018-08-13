package org.sample.disruptor.demo;

import com.lmax.disruptor.EventFactory;

public class WriteEventFactory implements EventFactory<WriteEvent> {

    @Override
    public WriteEvent newInstance() {
        return new WriteEvent();
    }

}
