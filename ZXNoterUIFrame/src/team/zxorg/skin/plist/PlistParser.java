package team.zxorg.skin.plist;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;

public class PlistParser {
    /**
     * 搜索目录的plist合并图片，全部拆分并删除源文件。
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Files.list(Path.of("D:\\malody\\skin\\测试中")).forEach(path -> {
            if (!path.getFileName().toString().endsWith(".plist")) return;
            try {
                PlistImageList plistImageList = new PlistImageList(path);
                for (SubImage subImage : plistImageList.getImages()) {
                    Dimension size = subImage.getSpriteSourceSize();
                    if (size == null) {
                        ImageIO.write(subImage.getImage(), "png", path.getParent().resolve(subImage.getImageName()).toFile());
                    } else {
                        BufferedImage sizeImage = new BufferedImage(size.width, size.height, subImage.getImage().getType());
                        Graphics2D g2d = sizeImage.createGraphics();
                        BufferedImage image = subImage.getImage();
                        g2d.drawImage(image, sizeImage.getWidth() / 2 - image.getWidth() / 2+subImage.getSpriteOffset().x, sizeImage.getHeight() / 2 - image.getHeight() / 2+subImage.getSpriteOffset().y, image.getWidth(), image.getHeight(), null);
                        g2d.dispose();

                        ImageIO.write(sizeImage, "png", path.getParent().resolve(subImage.getImageName()).toFile());
                    }

                }

                Files.delete(path);
                Files.delete(path.getParent().resolve(plistImageList.getTextureFileName()));

            } catch (Exception e) {
            }

        });


    }


}
