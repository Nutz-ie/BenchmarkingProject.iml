package testbench;

import bench.DemoForPausing;
import bench.IBenchmark;
import timing.Timer;
import logging.ConsoleLogger;
import logging.ILogger;
import logging.TimeUnit;

public class PauseTestbench {
    public static void main(String[] args) {
        IBenchmark bench = new DemoForPausing();
        Timer timer = new Timer();
        ILogger logger = new ConsoleLogger();

        bench.initialize();

        timer.start();
        for (int i = 0; i < 5; i++) {
            timer.resume();

            bench.run();

            long elapsed = timer.pause();
            logger.writeTime("Pause #" + (i + 1) + " time:", elapsed, TimeUnit.Milli);
        }

        long total = timer.stop();
        logger.writeTime("Total time:", total, TimeUnit.Milli);

        bench.clean();
        logger.close();

        System.out.println("Done.");
    }
}
