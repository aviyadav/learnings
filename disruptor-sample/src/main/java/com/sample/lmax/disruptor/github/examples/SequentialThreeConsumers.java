package com.sample.lmax.disruptor.github.examples;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class SequentialThreeConsumers {

    private static class MyEvent {

        private Object a;
        private Object b;
        private Object c;
        private Object d;
    }

    private static EventFactory<MyEvent> factory = () -> new MyEvent();

    private static EventHandler<MyEvent> handler1 = (MyEvent event, long sequence, boolean endOfBatch) -> {
        event.b = event.a;
    };

    private static EventHandler<MyEvent> handler2 = (MyEvent event, long sequence, boolean endOfBatch) -> {
        event.c = event.b;
    };

    private static EventHandler<MyEvent> handler3 = (MyEvent event, long sequence, boolean endOfBatch) -> {
        event.d = event.c;
    };

    public static void main(String[] args) {
        Disruptor<MyEvent> disruptor = new Disruptor<MyEvent>(factory, 1024, DaemonThreadFactory.INSTANCE);

        disruptor.handleEventsWith(handler1).then(handler2).then(handler3);

        disruptor.start();
    }
}
