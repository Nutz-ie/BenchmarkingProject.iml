package bench;

import java.util.Random;

public class BubbleSortBenchmark implements IBenchmark {
    private int[] array;
    private volatile boolean running = true;

    @Override
    public void initialize(Object... params) {
        int size = (int) params[0];
        array = new int[size];
        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(size);  //array filled with randoms
        }
    }

    @Override
    public void warmup() {

    }

    @Override
    public void run() {
        run(array);  //use internal array
    }

    @Override
    public void run(Object... params) {
        int[] data = (int[]) params[0];
        int n = data.length;

        for (int i = 0; i < n - 1 && running; i++) {
            for (int j = 0; j < n - i - 1 && running; j++) {
                if (data[j] > data[j + 1]) {
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                }
            }
        }
    }

    @Override
    public void clean() {
        array = null;
    }

    @Override
    public void cancel() {
        running = false;
    }
}

