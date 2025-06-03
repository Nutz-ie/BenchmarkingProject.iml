package testbench;

import bench.BubbleSortBenchmark;
import bench.IBenchmark;
import timing.ITimer;
import timing.Timer;
import logging.ILogger;
import logging.ConsoleLogger;

public class BubbleSortTestbench {
    public static void main(String[] args) {
        ITimer timer = new Timer();
        ILogger log = new ConsoleLogger();
        IBenchmark bench = new BubbleSortBenchmark();
        bench.initialize(10000);

        Thread benchmarkThread = new Thread(() -> bench.run()); //run in separate thread
        benchmarkThread.start();
        try {
            Thread.sleep(100);
            bench.cancel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

