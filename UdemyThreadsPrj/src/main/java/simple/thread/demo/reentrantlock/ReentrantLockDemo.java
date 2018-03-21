package simple.thread.demo.reentrantlock;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ReentrantLockDemo {
    public static void main(String[] args) throws InterruptedException {
        final Runner runner = new Runner();
        
        Thread t1 = new Thread(() -> {
            try {
                runner.firstThread();
            } catch (InterruptedException ex) {
                Logger.getLogger(ReentrantLockDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        Thread t2 = new Thread(() -> {
            try {
                runner.secondThread();
            } catch (InterruptedException ex) {
                Logger.getLogger(ReentrantLockDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        runner.finished();
    }
}
