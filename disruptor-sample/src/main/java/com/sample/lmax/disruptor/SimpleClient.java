package com.sample.lmax.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleClient {

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        Disruptor<ValueEvent> disruptor = new Disruptor<>(ValueEvent.EVENT_FACTORY, 1024, service);
        final EventHandler<ValueEvent> handler = (final ValueEvent event, final long sequence, final boolean endOfBatch) -> {
            System.out.println("Sequence: " + sequence);
            System.out.println("ValueEvent: " + event.getValue());
        };
        
        disruptor.handleEventsWith(handler);
        RingBuffer<ValueEvent> ringBuffer = disruptor.start();
        
        for (int i = 10; i < 5000; i++) {
            String uuid = UUID.randomUUID().toString();
            long seq = ringBuffer.next();
            ValueEvent valueEvent = ringBuffer.get(seq);
            valueEvent.setValue(uuid);
            ringBuffer.publish(seq);
        }
        
        disruptor.shutdown();
        service.shutdown();
    }
}
