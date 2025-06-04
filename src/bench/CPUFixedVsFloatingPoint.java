package bench;

public class CPUFixedVsFloatingPoint implements IBenchmark {
    public enum NumberRepresentation { FIXED, FLOATING }
    private int size = 100_000_000;
    private NumberRepresentation mode;

    @Override
    public void initialize(Object... params) {
        if (params.length > 0 && params[0] instanceof NumberRepresentation)
            mode = (NumberRepresentation) params[0];
        if (params.length > 1 && params[1] instanceof Integer)
            size = (Integer) params[1];
    }

    @Override
    public void run(Object... params) {
        if (mode == null) throw new IllegalStateException("Not initialized!");
        if (mode == NumberRepresentation.FIXED)
            runFixed();
        else
            runFloat();
    }
    @Override
    public void run(){

    }

    private void runFixed() {
        long sum = 0;
        for (int i = 0; i < size; i++) {
            sum += i >> 8;
        }
        System.out.println("Sum (fixed): " + sum);
    }

    private void runFloat() {
        float sum = 0;
        for (int i = 0; i < size; i++) {
            sum += i / 256.0f;
        }
        System.out.println("Sum (float): " + sum);
    }

    @Override
    public void warmup() {
        //simple warmup for both
        for (int i = 0; i < 10_000; i++) {
            int dummy = i >> 8;
            float dummy2 = i / 256.0f;
        }
    }

    @Override
    public void clean() {}
    @Override
    public void cancel() {}
}