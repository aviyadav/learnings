package examples;

import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;

import com.scireum.open.nucleus.Nucleus;
import com.scireum.open.nucleus.core.Part;
import com.scireum.open.nucleus.core.Register;
import com.scireum.open.nucleus.timer.EveryMinute;
import com.scireum.open.nucleus.timer.TimerInfo;

@Register(classes = EveryMinute.class)
public class ExampleNucleus implements EveryMinute {

    private static boolean enabled = false;

    private static Part<TimerInfo> timerInfo = Part.of(TimerInfo.class);

    public static void main(String[] args) throws Exception {
        Nucleus.LOG.setLevel(Level.FINE);
        Nucleus.init();

        enabled = true;

        while (true) {
            Thread.sleep(10000);
            System.out.println("Last invocation: " + timerInfo.get().getLastOneMinuteExecution());
        }

    }

    @Override
    public void runTimer() throws Exception {
        if (enabled) {
            System.out.println("The time is: " + DateFormat.getTimeInstance().format(new Date()));
        }
    }

}
