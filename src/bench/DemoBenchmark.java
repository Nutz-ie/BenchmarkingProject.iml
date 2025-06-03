package bench;

public class DemoBenchmark implements IBenchmark {
    private int sleepTime;
    private volatile boolean running;

    @Override
    public void run() {
        running = true;
        try {
            if (running) Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void initialize(Object... params) {
        if (params.length > 0 && params[0] instanceof Integer) {
            sleepTime = (int) params[0];
        }
        running = true;
    }

    @Override
    public void clean() {
    }

    @Override
    public void cancel() {
        running = false;
    }

    @Override
    public void run(Object... params) {
        run();
    }
}
