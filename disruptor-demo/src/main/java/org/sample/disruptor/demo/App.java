package org.sample.disruptor.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);
    
    public static void main(String[] args) {
        LMAXWriter writer = new LMAXWriter();
        logger.info("Initializing lmax disruptor.");
        writer.setRingBufferSize(7);
        writer.init();
        
        for (int i = 0; i < 10; i++) {
            writer.submitMessage("Message Sequence " + i);
        }
        
        logger.info("All message submitted.");
        writer.close();
        logger.info("Program executed successfully.");
    }
}
