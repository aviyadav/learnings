package com.sample.lmax.disruptor.github.examples;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventPoller;
import com.lmax.disruptor.RingBuffer;

public class PullWithPoller {

    private static Object getNextValue(EventPoller<DataEvent<Object>> poller) throws Exception {
        final Object[] out = new Object[1];
        
        poller.poll((DataEvent<Object> event, long sequence, boolean endOfBatch) -> {
            out[0] = event.copyOfData();
            
            return false;
        });
        
        return out[0];
    }
    
    public static class DataEvent<T> {
        
        T data;
        
        public static <T> EventFactory<DataEvent<T>> factory() {
            return () -> new DataEvent<>();
        }
        
        public T copyOfData() {
            return data;
        }
    }
    
    public static void main(String[] args) throws Exception {
        RingBuffer<DataEvent<Object>> ringBuffer = RingBuffer.createMultiProducer(DataEvent.factory(), 1024);
        final EventPoller<DataEvent<Object>> poller = ringBuffer.newPoller();
        
        Object value = getNextValue(poller);
        
        if(null != value) {
            // Process Value
        }
    }
}
