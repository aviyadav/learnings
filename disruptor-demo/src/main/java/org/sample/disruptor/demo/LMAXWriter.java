package org.sample.disruptor.demo;

import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LMAXWriter {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Disruptor<WriteEvent> disruptor;
    private WriteEventProducer producer;

    private int ringBufferSize;

    public void setRingBufferSize(int ringBufferSize) {
        this.ringBufferSize = ringBufferSize;
    }

    public void init() {

        Executor executor = Executors.newCachedThreadPool();

        WriteEventFactory factory = new WriteEventFactory();

        if (ringBufferSize == 0) {
            ringBufferSize = 1024;
        }

        double power = Math.log(ringBufferSize) / Math.log(2);

        if (power % 1 != 0) {
            power = Math.ceil(power);
            ringBufferSize = (int) Math.pow(2, power);
            logger.info("New ring buffer size = " + ringBufferSize);
        }

        WriteEventHandler handler = new WriteEventHandler();

        disruptor = new Disruptor<>(factory, ringBufferSize, executor);
        disruptor.handleEventsWith(handler);

        ExceptionHandler exceptionHandler = new WriteExceptionHandler();
        disruptor.handleExceptionsFor(handler).with(exceptionHandler);

        disruptor.start();

        producer = new WriteEventProducer(disruptor);
        logger.info("Disruptor engine started successfully.");
    }

    public void close() {
        if (disruptor != null) {
            disruptor.halt();
            disruptor.shutdown();
        }
    }

    public void submitMessage(String message) {
        if (producer != null) {
            producer.onData(message);
        }
    }
}
