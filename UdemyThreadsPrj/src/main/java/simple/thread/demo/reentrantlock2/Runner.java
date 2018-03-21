package simple.thread.demo.reentrantlock2;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Runner {
    
    private Account ac1 = new Account();
    private Account ac2 = new Account();
    
    private Lock lock1 = new ReentrantLock();
    private Lock lock2 = new ReentrantLock();
    
    private void acquireLocks(Lock l1, Lock l2) throws InterruptedException {
        while(true) {
            //Acquire Locks
            boolean gotFirstLock = false;
            boolean gotSecondLock = false;
            
            try {
                gotFirstLock = l1.tryLock();
                gotSecondLock = l2.tryLock();
            } finally {
                if(gotFirstLock && gotSecondLock) {
                    return;
                }
                
                if(gotFirstLock) {
                    l1.unlock();
                }
                
                if(gotSecondLock) {
                    l2.unlock();
                }
            }
            
            Thread.sleep(1);
        }
    }
    
    public void firstThread() throws InterruptedException {
        Random random = new Random();
        
        for (int i = 0; i < 10000; i++) {
            try {
                acquireLocks(lock1, lock2);
                Account.transfer(ac1, ac2, random.nextInt(1000));
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }
    
    public void secondThread() throws InterruptedException {
        Random random = new Random();
        
        for (int i = 0; i < 10000; i++) {
            try {
                acquireLocks(lock2, lock1);
                Account.transfer(ac2, ac1, random.nextInt(1000));
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }
    
    public void finished() {
        System.out.println("Account 1 balance : " + ac1.getBalance());
        System.out.println("Account 2 balance : " + ac2.getBalance());
        System.out.println("Total balance : " + (ac1.getBalance() + ac2.getBalance()));
    }
}
