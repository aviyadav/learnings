package simple.thread.demo.volatiledemo;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VolatileSample {
    public static void main(String[] args) {
        Processor proc1 = new Processor();
        proc1.start();
        
        System.out.println("Press return to stop...");
        
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        
        proc1.shutdown();
    }
}

class Processor extends Thread {
    private volatile boolean running = true;

    @Override
    public void run() {
        while(running) {
            System.out.println("Hello");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void shutdown() {
        running = false;
    }
}
