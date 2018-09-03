package org.sample.disruptor.demo;

import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteEventHandler implements EventHandler<WriteEvent> {
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onEvent(WriteEvent writeEvent, long sequence, boolean endOfBatch) throws Exception {
        if(writeEvent != null && writeEvent.get() != null) {
            String message = writeEvent.get();
            
            logger.error(message + " processed.");
        }
    }
}
