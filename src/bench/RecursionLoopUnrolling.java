package bench;

public class RecursionLoopUnrolling implements IBenchmark {
    private int size = 10000;
    private volatile boolean canceled = false;
    private long lastPrime = 0;
    private int totalCalls = 0;

    public void warmup() {

    }

    @Override
    public void initialize(Object... params) {
        if (params.length > 0 && params[0] instanceof Integer) {
            size = (Integer) params[0];
        }
    }
    @Override
    public void run() {
        run((Object[]) null);
    }
    @Override
    public void run(Object... params) {
        canceled = false;
        lastPrime = 0;
        totalCalls = 0;
        long startTime = System.nanoTime();

        boolean unroll = (params != null && params.length > 0 && params[0] instanceof Boolean) ? (Boolean) params[0] : false;
        int unrollLevel = (params != null && params.length > 1 && params[1] instanceof Integer) ? (Integer) params[1] : 1;

        long sum = 0;
        if (!unroll) {
            try {
                sum = recursive(1, size, 0);
                System.out.println("Finished. Sum of primes: " + sum);
            } catch (StackOverflowError e) {
                //handled inside recursive
            }
        } else {
            try {
                sum = recursiveUnrolled(1, unrollLevel, size, 0);
                System.out.println("Finished. Sum of primes: " + sum);
            } catch (StackOverflowError e) {
                //handled inside recursiveUnrolled
            }
        }
        long elapsedMillis = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("Finished in %.4f Milli\n", (double) elapsedMillis);

        double score = computeScore(totalCalls, lastPrime, elapsedMillis);
        System.out.printf("Score: %.2f\n", score);
    }

    //recursion without unrolling
    private long recursive(long start, long size, int counter) {
        if (canceled) return 0;
        try {
            if (start > size) {
                System.out.printf("Reached nr %d/%d after %d calls. Last prime: %d\n", size, size, counter, lastPrime);
                return 0;
            }
            long sum = 0;
            if (isPrime((int) start)) {
                sum += start;
                lastPrime = start;
            }
            totalCalls = counter + 1;
            return sum + recursive(start + 1, size, counter + 1);
        } catch (StackOverflowError e) {
            System.out.printf("Reached nr %d/%d after %d calls. Last prime: %d\n", start, size, counter, lastPrime);
            return 0;
        }
    }

    // Recursion with unrolling
    private long recursiveUnrolled(long start, int unrollLevel, int size, int counter) {
        if (canceled) return 0;
        try {
            long sum = 0;
            int ops = 0;
            long curr = start;
            while (ops < unrollLevel && curr <= size) {
                if (isPrime((int) curr)) {
                    sum += curr;
                    lastPrime = curr;
                }
                curr++;
                ops++;
            }
            if (curr > size) {
                System.out.printf("Reached nr %d/%d at %d levels after %d calls. Last prime: %d\n", size, size, unrollLevel, (counter + 1), lastPrime);
                return sum;
            }
            totalCalls = counter + 1;
            return sum + recursiveUnrolled(curr, unrollLevel, size, counter + 1);
        } catch (StackOverflowError e) {
            System.out.printf("Reached nr %d/%d at %d levels after %d calls. Last prime: %d\n", start, size, unrollLevel, counter, lastPrime);
            return 0;
        }
    }

    private boolean isPrime(int x) {
        if (x < 2) return false;
        if (x == 2) return true;
        if (x % 2 == 0) return false;
        for (int i = 3; i <= Math.sqrt(x); i += 2) {
            if (x % i == 0) return false;
        }
        return true;
    }

    // Example score formula: more calls, bigger lastPrime, less time => higher score
    private double computeScore(int calls, long lastPrime, long elapsedMillis) {
        if (elapsedMillis == 0) elapsedMillis = 1;
        return (calls * Math.log1p(lastPrime)) / (elapsedMillis);
    }

    @Override
    public void clean() {}

    @Override
    public void cancel() { canceled = true; }
}