package examples;

import com.scireum.open.statistics.Watch;

public class ExampleWatch {

    public static void main(String[] args) throws Exception {

        Watch w = Watch.start();

        Thread.sleep(100);

        System.out.println(w.durationReset());

        Thread.sleep(1);
        System.out.println(w.microDuration(false));
    }
}
