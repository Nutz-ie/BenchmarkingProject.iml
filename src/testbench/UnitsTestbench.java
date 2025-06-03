package testbench;

import bench.DemoBenchmark;
import bench.IBenchmark;
import logging.ConsoleLogger;
import logging.ILogger;
import logging.TimeUnit;
import timing.ITimer;
import timing.Timer;

public class UnitsTestbench {
    public static void main(String[] args) {
        final int sleepMillis = 100;
        IBenchmark bench = new DemoBenchmark();
        ITimer timer = new Timer();
        ILogger log = new ConsoleLogger();

        bench.initialize(sleepMillis);
        timer.start();
        bench.run();
        long elapsed = timer.stop();

        log.writeTime("Finished in", elapsed, TimeUnit.Milli);
        log.writeTime("Finished in", elapsed, TimeUnit.Micro);
        log.writeTime("Finished in", elapsed, TimeUnit.Nano);
        log.close();
    }
}
