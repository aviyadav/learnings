package examples;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import com.scireum.open.nucleus.Nucleus;
import com.scireum.open.nucleus.core.Part;
import com.scireum.open.statistics.HealthService;
import com.scireum.open.statistics.ProbeLog;

public class ExampleStatistics {

    private static Part<HealthService> healthService = Part.of(HealthService.class);

    public static void main(String[] args) throws Exception {
        Nucleus.init();
        while (true) {
            System.out.println("Waiting one minute...");
            Thread.sleep(TimeUnit.MILLISECONDS.convert(60, TimeUnit.SECONDS));
            outputData();
            System.out.println("----------------------------------");
        }
    }

    private static void outputData() {
        for (ProbeLog log : healthService.get().getStatistics()) {
            System.out.println(log.getProbe().getCategory()
                    + " - "
                    + log.getProbe().getName()
                    + "\n"
                    + DecimalFormat.getNumberInstance().format(
                            log.getCurrentValue())
                    + " "
                    + log.getProbe().getUnit()
                    + ", Avg. 30min: "
                    + DecimalFormat.getNumberInstance().format(
                            log.getAvg30Min()) + " " + log.getProbe().getUnit()
                    + ", Avg 24h: "
                    + DecimalFormat.getNumberInstance().format(log.getAvg24h())
                    + " " + log.getProbe().getUnit() + "\n");
        }

    }

}
