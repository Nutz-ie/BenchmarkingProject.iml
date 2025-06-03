package bench;

public class DemoForCancellation implements IBenchmark {
    private volatile boolean running = true;

    @Override
    public void initialize(Object... params) {
        System.out.println("Initialized");
        running = true;
    }

    @Override
    public void run(Object... params) {
        System.out.println("Started running");

        int i = 0;
        while (running && i < 100) {
            System.out.println("Running iteration " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Interrupted during sleep");
            }
            i++;
        }

        if (!running) {
            System.out.println("Cancelled after " + i + " iterations");
        } else {
            System.out.println("Completed normally");
        }
    }

    @Override
    public void run() {
        run((Object[]) null);
    }

    @Override
    public void clean() {
        System.out.println("Clean-up complete");
    }

    @Override
    public void cancel() {
        System.out.println("Cancel called");
        running = false;
    }
}
