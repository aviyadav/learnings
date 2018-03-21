package simple.thread.demo.semaphore;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection {
    private static Connection instance = new Connection();
    private Semaphore semaphore = new Semaphore(10, true);
    private int connections = 0;

    public Connection() {
    }

    public static Connection getInstance() {
        return instance;
    }
    
    public void connect() {
        try {
            semaphore.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            doConnect();
        } finally {
            semaphore.release();
        }
    }

    private void doConnect() {
        synchronized (this) {
            connections++;
            System.out.println("Current Connection: " + connections);
        }
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        synchronized(this) {
            connections--;
        }
    }
}
