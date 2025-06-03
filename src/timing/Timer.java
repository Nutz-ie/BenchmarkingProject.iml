package timing;

public class Timer implements ITimer {
    private long startTime = 0;
    private long totalTime = 0;
    private boolean paused = false;

    @Override
    public void start() {
        totalTime = 0;
        startTime = System.nanoTime();
        paused = false;
    }

    @Override
    public long stop() {
        if (!paused) {
            totalTime += System.nanoTime() - startTime;
        }
        return totalTime;
    }

    @Override
    public void resume() {
        if (paused) {
            startTime = System.nanoTime();
            paused = false;
        }
    }

    @Override
    public long pause() {
        if (!paused) {
            long now = System.nanoTime();
            totalTime += now - startTime;
            paused = true;
        }
        return totalTime;
    }
}

