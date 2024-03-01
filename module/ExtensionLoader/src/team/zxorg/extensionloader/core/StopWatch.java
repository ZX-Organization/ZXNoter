package team.zxorg.extensionloader.core;

public class StopWatch {
    private long startTime;
    private long stopTime;
    private boolean isRunning;

    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            stopTime = System.currentTimeMillis();
            isRunning = false;
        }
    }

    public long getTime() {
        if (isRunning) {
            return System.currentTimeMillis() - startTime;
        } else {
            return stopTime - startTime;
        }
    }

    public void reset() {
        startTime = 0;
        stopTime = 0;
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
