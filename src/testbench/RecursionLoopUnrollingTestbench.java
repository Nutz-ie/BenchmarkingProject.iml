package testbench;

import bench.RecursionLoopUnrolling;
import bench.IBenchmark;
import logging.ConsoleLogger;
import logging.TimeUnit;
import timing.Timer;

public class RecursionLoopUnrollingTestbench {
    public static void main(String[] args) {
        Timer timer = new Timer();
        ConsoleLogger logger = new ConsoleLogger();
        IBenchmark bench = new RecursionLoopUnrolling();

        int workload = 2000; // Adjust as needed for testing
        bench.initialize(workload);

        // No unrolling
        System.out.println("\n[No Unrolling]");
        timer.start();
        bench.run(false);
        logger.writeTime("Finished in ", timer.stop(), TimeUnit.Milli);

        // Unrolling of level 1
        System.out.println("\n[Unrolling level 1]");
        timer.start();
        bench.run(true, 1);
        logger.writeTime("Finished in ", timer.stop(), TimeUnit.Milli);

        // Unrolling of level 5
        System.out.println("\n[Unrolling level 5]");
        timer.start();
        bench.run(true, 5);
        logger.writeTime("Finished in ", timer.stop(), TimeUnit.Milli);

        // Unrolling of level 15
        System.out.println("\n[Unrolling level 15]");
        timer.start();
        bench.run(true, 15);
        logger.writeTime("Finished in ", timer.stop(), TimeUnit.Milli);
    }
}