package simple.thread.demo.execdemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

class Processor implements Runnable {
    
    private int id;

    public Processor(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Starting... " + id);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Completed... " + id);
    }    
}

public class ExecutorDemo {
    
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        for (int i = 0; i < 15; i++) {
            executor.submit(new Processor(i + 1));
        }
        
        executor.shutdown();
        
        System.out.println("All tasks submitted");
        
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            Logger.getLogger(ExecutorDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("All tasks completed");
    }

}
