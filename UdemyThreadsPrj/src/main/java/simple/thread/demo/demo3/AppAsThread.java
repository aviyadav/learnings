package simple.thread.demo.demo3;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AppAsThread {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("Hello " + i);
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AppAsThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        t1.start();
    }
}
