package team.zxorg.zxnoter.audiomixer;

public class CanPauseThread extends Thread {

    public interface CanPauseThreadCallBack{
        void loop();
        void threadEvent(CanPauseThreadEvent cpte);
    }
    public enum CanPauseThreadEvent{
        loop,pause
    }

    private boolean pause = true;
    CanPauseThreadCallBack callBack;

    public CanPauseThread(CanPauseThreadCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                if (pause) {
                    try {
                        callBack.threadEvent(CanPauseThreadEvent.pause);
                        wait();
                        callBack.threadEvent(CanPauseThreadEvent.loop);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
            callBack.loop();
        }
    }

    public synchronized void pause(boolean pause) {
        this.pause = pause;
        if (!pause)
            notifyAll();

    }

    public boolean isPause() {
        return pause;
    }
}

