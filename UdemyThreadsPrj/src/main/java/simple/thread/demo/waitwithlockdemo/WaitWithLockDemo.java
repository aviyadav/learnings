package simple.thread.demo.waitwithlockdemo;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WaitWithLockDemo {
    public static void main(String[] args) throws InterruptedException {
        final Processor processor = new Processor();
        
        Thread t1 = new Thread(() -> {
            try {
                processor.produce();
            } catch (InterruptedException ex) {
                Logger.getLogger(WaitWithLockDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        Thread t2 = new Thread(() -> {
            try {
                processor.consume();
            } catch (InterruptedException ex) {
                Logger.getLogger(WaitWithLockDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
    }
}
