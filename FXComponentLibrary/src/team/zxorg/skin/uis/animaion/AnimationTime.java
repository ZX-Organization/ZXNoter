package team.zxorg.skin.uis.animaion;

public class AnimationTime {
    long startTime;
    long endTime;
    boolean flip;
    double fixedProgress;

    public double getProgress(long time) {
        if (!isFixedProgress())
            if (flip)
                return 1 - (double) (time - startTime) / (endTime - startTime);
            else
                return (double) (time - startTime) / (endTime - startTime);
        return fixedProgress;
    }


    public boolean isFixedProgress() {
        return fixedProgress != -1;
    }

    public AnimationTime(long startTime, long endTime, double fixedProgress) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.flip = false;
        this.fixedProgress = fixedProgress;
    }

    public AnimationTime(long startTime, long endTime, boolean flip) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.flip = flip;
        fixedProgress = -1;
    }

    public long getStartTime() {
        return startTime;
    }


    public long getEndTime() {
        return endTime;
    }

    public boolean isFlip() {
        return flip;
    }
}
