package com.sample.lmax.disruptor.perf.queue;

import com.lmax.disruptor.util.DaemonThreadFactory;
import com.sample.lmax.disruptor.perf.AbstractPerfTestQueue;
import com.sample.lmax.disruptor.perf.support.PerfTestUtil;
import com.sample.lmax.disruptor.perf.support.ValueAdditionBatchQueueProcessor;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class OneToOneQueueBatchedThroughputTest extends AbstractPerfTestQueue {

    private static final int BUFFER_SIZE = 1024 * 64;
    private static final long ITERATIONS = 1000L * 1000L * 10L;
    private final ExecutorService executor = Executors.newSingleThreadExecutor(DaemonThreadFactory.INSTANCE);
    private final long expectedResult = ITERATIONS * 3L;

    private final BlockingQueue<Long> blockingQueue = new LinkedBlockingQueue<>(BUFFER_SIZE);
    private final ValueAdditionBatchQueueProcessor queueProcessor = new ValueAdditionBatchQueueProcessor(blockingQueue, ITERATIONS);

    @Override
    protected int getRequiredProcessorCount() {
        return 2;
    }

    @Override
    protected long runQueuePass() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        queueProcessor.reset(latch);
        Future<?> future = executor.submit(queueProcessor);
        long start = System.currentTimeMillis();

        for (long i = 0; i < ITERATIONS; i++) {
            blockingQueue.put(3L);
        }

        latch.await();
        long opsPerSecond = (ITERATIONS * 1000L) / (System.currentTimeMillis() - start);
        queueProcessor.halt();
        future.cancel(true);

        PerfTestUtil.failIf(expectedResult, 0);

        return opsPerSecond;
    }
    
    public static void main(String[] args) throws Exception {
        OneToOneQueueBatchedThroughputTest test = new OneToOneQueueBatchedThroughputTest();
        test.testImplementations();
    }
}
