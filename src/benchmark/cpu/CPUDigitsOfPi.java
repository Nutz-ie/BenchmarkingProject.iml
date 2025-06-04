package benchmark.cpu;

import bench.IBenchmark;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.io.FileWriter;
import java.io.IOException;

public class CPUDigitsOfPi implements IBenchmark {

    private int digits = 1000; // Default precision
    private volatile boolean canceled = false;

    @Override
    public void warmup() {
        if (canceled) return;
        computePi(digits); // You can switch to computePiChudnovsky(digits) here for Chudnovsky warmup
    }

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
        BigDecimal pi = computePiChudnovsky(digits);
        long end = System.nanoTime();

        if (canceled) {
            System.out.println("Benchmark canceled.");
            return;
        }

        System.out.println("Pi to " + digits + " digits:");
        System.out.println(pi);

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
        BigDecimal epsilon = BigDecimal.ONE.scaleByPowerOfTen(-(digits + 5));

        BigDecimal arctan1_5 = arctan(new BigDecimal("1").divide(new BigDecimal("5"), mc), mc, epsilon);
        BigDecimal arctan1_239 = arctan(new BigDecimal("1").divide(new BigDecimal("239"), mc), mc, epsilon);

        return arctan1_5.multiply(new BigDecimal("16"), mc)
                .subtract(arctan1_239.multiply(new BigDecimal("4"), mc), mc)
                .round(new MathContext(digits, RoundingMode.HALF_EVEN));
    }

    private BigDecimal arctan(BigDecimal x, MathContext mc, BigDecimal epsilon) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal term = x;
        BigDecimal x2 = x.multiply(x, mc);
        int k = 1;

        while (term.abs().compareTo(epsilon) >= 0 && !canceled) {
            if ((k / 2) % 2 == 0)
                result = result.add(term, mc);
            else
                result = result.subtract(term, mc);

            k += 2;
            term = term.multiply(x2, mc).divide(new BigDecimal(k), mc);
        }

        return result;
    }

    // --- Chudnovsky Formula ---
    private BigDecimal computePiChudnovsky(int digits) {
        MathContext mc = new MathContext(digits + 10, RoundingMode.HALF_EVEN);
        BigDecimal epsilon = BigDecimal.ONE.scaleByPowerOfTen(-(digits + 5));

        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal C = new BigDecimal("426880");
        BigDecimal sqrt10005 = sqrt(new BigDecimal("10005"), mc);
        BigDecimal constant = C.multiply(sqrt10005, mc);

        int k = 0;
        while (!canceled) {
            BigDecimal term = chudnovskyTerm(k, mc);
            if (term.abs().compareTo(epsilon) < 0) break;
            sum = sum.add(term, mc);
            k++;
        }

        return constant.divide(sum, mc).round(new MathContext(digits, RoundingMode.HALF_EVEN));
    }

    private BigDecimal chudnovskyTerm(int k, MathContext mc) {
        BigDecimal multiplier = new BigDecimal(545140134L * k + 13591409L);
        BigDecimal numerator = new BigDecimal(factorial(6 * k)).multiply(multiplier, mc);
        BigDecimal denominator = new BigDecimal(factorial(3 * k))
                .multiply(new BigDecimal(factorial(k)).pow(3), mc)
                .multiply(BigDecimal.valueOf(640320).pow(3 * k), mc);

        if (k % 2 != 0) numerator = numerator.negate();

        return numerator.divide(denominator, mc);
    }

    private BigInteger factorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    private BigDecimal sqrt(BigDecimal x, MathContext mc) {
        BigDecimal guess = new BigDecimal(Math.sqrt(x.doubleValue()));
        BigDecimal two = BigDecimal.valueOf(2);
        BigDecimal lastGuess;

        do {
            lastGuess = guess;
            guess = x.divide(guess, mc).add(guess).divide(two, mc);
        } while (!lastGuess.equals(guess));

        return guess;
    }

    @Override
    public void clean() {
        // No resources to clean
    }

    @Override
    public void cancel() {
        canceled = true;
    }
}
