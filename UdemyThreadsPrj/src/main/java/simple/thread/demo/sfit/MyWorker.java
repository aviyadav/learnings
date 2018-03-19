package simple.thread.demo.sfit;

import java.util.concurrent.TimeUnit;

public class MyWorker implements Runnable {

    private final int sleepTime;

    public MyWorker(final int sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        final long startTime = System.nanoTime();
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(sleepTime));
            Util.printLog("finished");
        } catch (InterruptedException ex) {
            Thread.currentThread().isInterrupted();
            final long interruptedAfter = System.nanoTime() - startTime;
            Util.printLog("Interrupted after %,d nano seconds", interruptedAfter);
        }
    }
}
