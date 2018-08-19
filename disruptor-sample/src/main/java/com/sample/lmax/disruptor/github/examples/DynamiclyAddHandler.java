package com.sample.lmax.disruptor.github.examples;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.LifecycleAware;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.sample.lmax.disruptor.github.support.StubEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DynamiclyAddHandler {

    private static class DynamicHandler implements EventHandler<StubEvent>, LifecycleAware {

        private final CountDownLatch shutdownLatch = new CountDownLatch(1);

        @Override
        public void onEvent(final StubEvent event, final long sequence, final boolean endOfBatch) throws Exception {
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onShutdown() {
            shutdownLatch.countDown();
        }

        public void awaitShutdown() throws InterruptedException {
            shutdownLatch.await();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool(DaemonThreadFactory.INSTANCE);

        Disruptor<StubEvent> disruptor = new Disruptor<>(StubEvent.EVENT_FACTORY, 1024, DaemonThreadFactory.INSTANCE);
        RingBuffer<StubEvent> ringBuffer = disruptor.start();

        DynamicHandler handler1 = new DynamicHandler();
        BatchEventProcessor<StubEvent> procecssor1 = new BatchEventProcessor<>(ringBuffer, ringBuffer.newBarrier(), handler1);

        DynamicHandler handler2 = new DynamicHandler();
        BatchEventProcessor<StubEvent> procecssor2 = new BatchEventProcessor<>(ringBuffer, ringBuffer.newBarrier(procecssor1.getSequence()), handler2);

        ringBuffer.addGatingSequences(procecssor1.getSequence(), procecssor2.getSequence());

        executor.execute(procecssor1);
        executor.execute(procecssor2);

        procecssor2.halt();
        handler2.awaitShutdown();
        ringBuffer.removeGatingSequence(procecssor2.getSequence());
    }
}
