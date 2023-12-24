package team.zxorg.skin.components;

import javafx.scene.image.Image;
import team.zxorg.zxncore.ZXLogger;

import java.io.InputStream;
import java.nio.file.Path;

import static team.zxorg.skin.uis.UISParser.getResource;

public class AnimationComponent {
    Image[] frames;
    double interval;
    int currentIndex;
    long timer = System.currentTimeMillis();
    boolean loop;

    public AnimationComponent(String frameInfo, double interval, Path uisPath) {
        this.interval = interval;
        int delimiter = frameInfo.lastIndexOf("/");
        String path = frameInfo.substring(0, delimiter);
        String[] indexes = frameInfo.substring(delimiter + 1).split("-");
        int from = Integer.parseInt(indexes[0]);
        int to = Integer.parseInt(indexes[1]);
        frames = new Image[to - from + 1];
        int index = 0;
        for (int i = from; i < to + 1; i++) {
            InputStream fileStream;
            fileStream = getResource(uisPath, path + "-" + i + ".png");
            if (fileStream != null) frames[index] = new Image(fileStream);
            else ZXLogger.warning("Could not find animation frame " + path + "-" + i + ".png");
            index++;
        }
    }

    public void setCurrentFrameTo(int index) {
        currentIndex = index;
    }

    public boolean update() {
        int time = (int) (System.currentTimeMillis() - timer);
        if (time > interval) {
            currentIndex++;
            timer = System.currentTimeMillis();
            if (currentIndex >= frames.length && loop) {
                currentIndex = 0;
            }
            return currentIndex == frames.length - 1;

        }
        return false;
    }

    public Image getCurrentFrame() {
        if (currentIndex >= frames.length || currentIndex < 0) return null;
        return frames[currentIndex];
    }

}
