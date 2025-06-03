package bench;

public class DemoForPausing implements IBenchmark {
    @Override
    public void initialize(Object... params) {
        System.out.println("Initialized");
    }

    @Override
    public void run(Object... params) {
        System.out.println("Doing stuff");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        run((Object[]) null);
    }

    @Override
    public void clean() {
        System.out.println("Clean-up done");
    }

    @Override
    public void cancel() {
    }
}
