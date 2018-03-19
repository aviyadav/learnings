package simple.thread.demo.syncdemo;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SyncDemoApp {
    private /*volatile*/ int count = 0;
    
    public synchronized void increment() {
        count++;
    }
    
    public void doWork() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                increment();
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                increment();
            }
        });
        
        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(SyncDemoApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Count is " + count);
    }
    
    public static void main(String[] args) {
        SyncDemoApp app = new SyncDemoApp();
        app.doWork();
    }
}
