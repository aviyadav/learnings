package simple.thread.demo.waitnotifydemo;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WaitNotifyDemo {
    public static void main(String[] args) throws InterruptedException {
        final Processor processor = new Processor();
        Thread t1 = new Thread(() -> {
            try {
                processor.produce();
            } catch (InterruptedException ex) {
                Logger.getLogger(WaitNotifyDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        Thread t2 = new Thread(() -> {
            try {
                processor.consume();
            } catch (InterruptedException ex) {
                Logger.getLogger(WaitNotifyDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
    }
}
