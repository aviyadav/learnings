package main;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class ThreadExecutor {
    
    private static final Logger LOG = Logger.getLogger(ThreadExecutor.class.getName());

    public static void main(String[] args) throws FileNotFoundException {
        
        int threadCount = 3;
        
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        executorService.execute(new EditsThread());
        executorService.shutdown();
    }
    
}
