package benchmark.cpu;

import bench.IBenchmark;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.io.FileWriter;
import java.io.IOException;

public class CPUDigitsOfPi implements IBenchmark {

    private int digits = 1000; // Default precision
    private volatile boolean canceled = false;

    @Override
    public void initialize(Object... params) {
        if (params.length > 0 && params[0] instanceof Integer) {
            digits = (Integer) params[0];
        }
    }

    @Override
    public void run() {
        run((Object[]) null);
    }

    @Override
    public void run(Object... params) {
        canceled = false;

        boolean saveToFile = false;
        String filename = null;

        if (params != null && params.length > 0) {
            for (Object param : params) {
                if (param instanceof String) {
                    saveToFile = true;
                    filename = (String) param;
                }
            }
        }

        long start = System.nanoTime();
        BigDecimal pi = computePi(digits);
        long end = System.nanoTime();

        if (canceled) {
            System.out.println("Benchmark canceled.");
            return;
        }

        System.out.println("Pi to " + digits + " digits:");
        System.out.println(pi);
        System.out.println("\nTime: " + ((end - start) / 1_000_000) + " ms");

        if (saveToFile && filename != null) {
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write(pi.toPlainString());
                System.out.println("Pi written to file: " + filename);
            } catch (IOException e) {
                System.err.println("Failed to write file: " + e.getMessage());
            }
        }
    }

    private BigDecimal computePi(int digits) {
        MathContext mc = new MathContext(digits + 5, RoundingMode.HALF_EVEN);
        BigDecimal arctan1_5 = arctan(new BigDecimal("1").divide(new BigDecimal("5"), mc), mc);
        BigDecimal arctan1_239 = arctan(new BigDecimal("1").divide(new BigDecimal("239"), mc), mc);

        return arctan1_5.multiply(new BigDecimal("16"))
                .subtract(arctan1_239.multiply(new BigDecimal("4")))
                .round(new MathContext(digits, RoundingMode.HALF_EVEN));
    }

    private BigDecimal arctan(BigDecimal x, MathContext mc) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal term = x;
        BigDecimal x2 = x.multiply(x, mc);
        int k = 1;

        while (term.abs().compareTo(BigDecimal.ZERO) > 0 && !canceled) {
            if ((k / 2) % 2 == 0)
                result = result.add(term);
            else
                result = result.subtract(term);

            k += 2;
            term = term.multiply(x2, mc).divide(new BigDecimal(k), mc);
        }

        return result;
    }

    @Override
    public void clean() {
        // No resources to clean in this case
    }

    @Override
    public void cancel() {
        canceled = true;
    }
}
