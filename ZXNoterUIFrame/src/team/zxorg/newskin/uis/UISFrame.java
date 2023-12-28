package team.zxorg.newskin.uis;

import javafx.scene.image.Image;

import java.util.List;

public class UISFrame {
    List<Image> frames;
    double interval;
    int currentIndex;
    long timer = System.currentTimeMillis();
    boolean loop = true;

    public UISFrame() {
        frames = null;
    }

    public UISFrame(UISComponent component, String name) {
        this.interval = component.getDouble("interval", 30);
        frames = component.getImageList(name);
    }

    @Override
    public String toString() {
        return "UISFrame{" +
                "frames=" + frames +
                ", interval=" + interval +
                ", loop=" + loop +
                '}';
    }

    public void setCurrentFrameTo(int index) {
        currentIndex = index;
    }

    public boolean update() {
        if (frames == null) return false;
        int time = (int) (System.currentTimeMillis() - timer);
        if (time > interval) {
            currentIndex++;
            timer = System.currentTimeMillis();
            if (currentIndex >= frames.size() && loop) {
                currentIndex = 0;
            }
            return currentIndex == frames.size() - 1;

        }
        return false;
    }

    public Image getCurrentFrame() {
        if (frames == null) return null;
        Image result = null;
        if (currentIndex < frames.size() && currentIndex >= 0) {
            result = frames.get(currentIndex);
        }
        return result;
    }

}
