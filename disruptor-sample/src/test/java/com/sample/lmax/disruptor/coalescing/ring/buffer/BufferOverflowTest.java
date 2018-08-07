package com.sample.lmax.disruptor.coalescing.ring.buffer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BufferOverflowTest {

    private static final int POSITION_PILL = -1;

    @Test
    public void shouldBeAbleToReuseCapacity() throws Exception {
        CoalescingBuffer<Integer, Integer> buffer = new CoalescingRingBuffer<>(32);

        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);

        producer.start();
        consumer.start();

        producer.join();
        assertFalse("ring buffer has overflowed", producer.hasOverflowed);
    }

    private static class Producer extends Thread {

        private final CoalescingBuffer<Integer, Integer> buffer;
        private boolean hasOverflowed;

        public Producer(CoalescingBuffer<Integer, Integer> buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            for(int run = 0; run < 1000000; run++) {
                for(int message = 0; message < 10; message++) {
                    boolean success = buffer.offer(message, message);

                    if(!success) {
                        hasOverflowed = true;
                        buffer.offer(POSITION_PILL);
                        return;
                    }
                }
            }

            buffer.offer(POSITION_PILL);
        }
    }

    private static class Consumer extends Thread {

        public static final int CAPACITY = 100;
        private final CoalescingBuffer<Integer, Integer> buffer;

        public Consumer(CoalescingBuffer<Integer, Integer> buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            List<Integer> values = new ArrayList<>(CAPACITY);

            while (true) {
                buffer.poll(values, CAPACITY);

                if(values.contains(POSITION_PILL)) {
                    return;
                }
            }
        }
    }
}