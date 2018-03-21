package simple.thread.demo.futuresdemo;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FutureDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> future = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Random random = new Random();
                int duration = random.nextInt(4000);
                
                if(duration > 2000) {
                    throw new IOException("Sleeping for too long.");
                }
                
                System.out.println("Starting...");
                
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                System.out.println("Finished...");
                return duration;
            }
            
        });
        
        executor.shutdown();
        
        try {
            System.out.println("Result is : " + future.get());
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(FutureDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
