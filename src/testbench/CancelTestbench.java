package testbench;

import bench.DemoForCancellation;
import bench.IBenchmark;

public class CancelTestbench {
    public static void main(String[] args) {
        IBenchmark bench = new DemoForCancellation();
        bench.initialize();

        System.out.println("Starting benchmark thread");
        Thread benchmarkThread = new Thread(() -> bench.run());
        benchmarkThread.start();

        try {
            Thread.sleep(500);
            System.out.println("Calling cancel");
            bench.cancel();
            benchmarkThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bench.clean();
        System.out.println("Done");
    }
}
