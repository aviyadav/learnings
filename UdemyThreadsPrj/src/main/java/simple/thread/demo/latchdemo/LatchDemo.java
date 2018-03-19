package simple.thread.demo.latchdemo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LatchDemo {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(3);
        ExecutorService exeService = Executors.newFixedThreadPool(3);
        
        for (int i = 0; i < 3; i++) {
            exeService.submit(new Processor(latch));
        }
        
        try {
            latch.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(LatchDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Completed...");
    }
}

class Processor implements Runnable {
    
    private CountDownLatch latch;

    public Processor(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        System.out.println("Started....");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }
        latch.countDown();
    }
}
