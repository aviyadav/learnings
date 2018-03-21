package simple.thread.demo.interruption;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InterruptDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");
        Thread t1 = new Thread(() -> {
            Random random = new Random();
            for (int i = 0; i < 1E8; i++) {
                if(Thread.currentThread().isInterrupted()) {
                    System.out.println("Interruptetd");
                    break;
                }
                
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(InterruptDemo.class.getName()).log(Level.SEVERE, null, ex);
//                    System.out.println("Interrupted..");
//                    break;
//                }
                
                Math.sin(random.nextDouble());
            }
        });
        
        t1.start();
        Thread.sleep(500);
        t1.interrupt();
        t1.join();
        System.out.println("Finished");
    }
    
}
