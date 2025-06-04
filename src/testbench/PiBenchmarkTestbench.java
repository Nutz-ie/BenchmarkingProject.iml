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

        int digits = 10000; //adjust precision as needed
        bench.initialize(digits);

        Thread benchmarkThread = new Thread(() -> {
            timer.start();
            bench.run("output/pi_output.txt");
            long Elapsed = timer.stop();
            logger.writeTime("Elapsed time: ", Elapsed, TimeUnit.Milli);
        });

        benchmarkThread.start();

    }
}
