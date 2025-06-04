package bench;

import bench.IBenchmark;

public class CPUFixedPoint implements IBenchmark {
    private int size = 1_000_000;

    @Override
    public void initialize(Object... params) {
        if (params.length > 0 && params[0] instanceof Integer)
            size = (Integer) params[0];
    }

    @Override
    public void run() {

    }

    @Override
    public void run(Object... params) {
        testIntegerArithmetic();
        testBranching();
        testArrayAccess();
    }

    public void testIntegerArithmetic() {
        int[] num = {1, 2, 3, 4};
        int[] res = new int[size];
        int j = 1, k = 2, l = 3;
        for (int i = 2; i < size; i++) {
            j = num[1] * (k - j) * (l - k);
            k = num[3] * k - (l - j) * k;
            l = (l - k) * (num[2] + j);
            res[l - 2 < 0 ? 0 : l - 2] = j + k + l;
            res[k - 2 < 0 ? 0 : k - 2] = j * k * l;
        }
    }

    public void testBranching() {
        int[] num = {0, 1, 2, 3};
        int j = 1;
        for (int i = 0; i < size; i++) {
            if (j == 1) {
                j = num[2];
            } else {
                j = num[3];
            }
            if (j > 2) {
                j = num[0];
            } else {
                j = num[1];
            }
            if (j < 1) {
                j = num[1];
            } else {
                j = num[0];
            }
        }
    }

    public void testArrayAccess() {
        int[] a = new int[size];
        int[] b = new int[size];
        int[] c = new int[size];
        for (int i = 0; i < size; i++) {
            a[i] = i;
            b[i] = size - i - 1;
        }
        for (int i = 0; i < size; i++) {
            int bi = b[i];
            if (bi >= 0 && bi < size)
                c[i] = a[bi];
        }
        // Swap arrays
        for (int i = 0; i < size; i++) {
            int tmp = a[i];
            a[i] = b[i];
            b[i] = tmp;
        }
    }

    @Override
    public void warmup() {}
    @Override
    public void clean() {}
    @Override
    public void cancel() {}

    // MOPS calculation (see below for details)
    public double getMOPS(long elapsedMillis, int opCount) {
        return opCount / (elapsedMillis / 1000.0) / 1_000_000;
    }
}