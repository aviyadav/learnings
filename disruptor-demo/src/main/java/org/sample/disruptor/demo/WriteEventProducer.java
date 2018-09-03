package org.sample.disruptor.demo;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteEventProducer {
    
    private static Logger logger = LoggerFactory.getLogger(WriteEventProducer.class);
    private final Disruptor<WriteEvent> disruptor;

    public WriteEventProducer(Disruptor<WriteEvent> disruptor) {
        this.disruptor = disruptor;
    }
    
    private static final EventTranslatorOneArg<WriteEvent, String> TRANSLATOR_ONE_ARG = new EventTranslatorOneArg<WriteEvent, String>() {
        @Override
        public void translateTo(WriteEvent writeEvent, long sequence, String message) {
            logger.debug("Inside translator");
            writeEvent.set(message);
        }
    };
    
    public void onData(String message) {
        logger.info("Publishing " + message);
        disruptor.publishEvent(TRANSLATOR_ONE_ARG, message);
    }
}
