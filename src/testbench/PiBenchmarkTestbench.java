package testbench;

import bench.IBenchmark;
import benchmark.cpu.CPUDigitsOfPi;
import logging.ConsoleLogger;
import logging.TimeUnit;
import timing.Timer;

public class PiBenchmarkTestbench {
    public static void main(String[] args) {
        Timer timer = new Timer();
        ConsoleLogger logger = new ConsoleLogger();
        IBenchmark bench = new CPUDigitsOfPi();

        int digits = 500; // adjust precision as needed
        bench.initialize(digits);

        Thread benchmarkThread = new Thread(() -> {
            timer.start();
            bench.run("pi_output.txt"); // writes Pi to file as well
            long Elapsed = timer.stop();
            logger.writeTime("Elapsed time: ", Elapsed, TimeUnit.Milli);
        });

        benchmarkThread.start();

        try {
            Thread.sleep(100); // Simulate early cancel if needed
            bench.cancel();    // Optional: test cancellation support
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
