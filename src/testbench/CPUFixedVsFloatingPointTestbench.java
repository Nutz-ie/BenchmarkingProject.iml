package testbench;

import bench.IBenchmark;
import bench.CPUFixedVsFloatingPoint;

public class CPUFixedVsFloatingPointTestbench {
    public static void main(String[] args) {
        IBenchmark bench = new CPUFixedVsFloatingPoint();

        // Test fixed-point
        bench.initialize(CPUFixedVsFloatingPoint.NumberRepresentation.FIXED, 100_000_000);
        bench.warmup();
        bench.run();

        // Test float
        bench.initialize(CPUFixedVsFloatingPoint.NumberRepresentation.FLOATING, 100_000_000);
        bench.warmup();
        bench.run();
    }
}