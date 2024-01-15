package skin.plist;

import org.xml.sax.SAXException;
import team.zxorg.zxncore.ZXLogger;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PlistParser {
    /**
     * 搜索目录的plist合并图片，全部拆分并删除源文件。
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
/*

        Files.list(Path.of("D:\\malody\\skin\\Evans-进行中")).forEach(path -> {
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
                        g2d.drawImage(image, sizeImage.getWidth() / 2 - image.getWidth() / 2 + subImage.getSpriteOffset().x, sizeImage.getHeight() / 2 - image.getHeight() / 2 - subImage.getSpriteOffset().y, image.getWidth(), image.getHeight(), null);
                        g2d.dispose();

                        ImageIO.write(sizeImage, "png", path.getParent().resolve(subImage.getImageName()).toFile());
                    }

                }

                Files.delete(path);
                Files.delete(path.getParent().resolve(plistImageList.getTextureFileName()));

            } catch (Exception e) {
            }

        });
*/


    }

    /**
     * 解析Plist图片
     *
     * @param plistPath  plist文件路径
     * @param outputPath 输出路径
     */
    public static List<Path> parser(Path plistPath, Path outputPath, boolean skip) {
        List<Path> paths = new ArrayList<>();

        if (!plistPath.getFileName().toString().endsWith(".plist")) return paths;

        PlistImageList plistImageList;
        BufferedImage texture;
        try {
            plistImageList = new PlistImageList(plistPath);
            texture = plistImageList.getTexture();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            ZXLogger.warning("解析Plist文件失败 " + e.getMessage());
            return paths;
        }


        for (SubImage subImage : plistImageList.getImages()) {
            Path outPath = outputPath.resolve(subImage.getImageName());
            paths.add(outPath);
            if (Files.exists(outPath) && skip)
                continue;

            Dimension size = subImage.getSpriteSourceSize();
            BufferedImage in = subImage.getImage(texture);
            BufferedImage outImage;
            if (size == null) {
                outImage = in;
            } else {
                BufferedImage sizeImage = new BufferedImage(size.width, size.height, in.getType());
                Graphics2D g2d = sizeImage.createGraphics();
                g2d.drawImage(in, sizeImage.getWidth() / 2 - in.getWidth() / 2 + subImage.getSpriteOffset().x, sizeImage.getHeight() / 2 - in.getHeight() / 2 - subImage.getSpriteOffset().y, in.getWidth(), in.getHeight(), null);
                g2d.dispose();
                outImage = sizeImage;
            }


            try {
                ImageIO.write(outImage, "png", outPath.toFile());
            } catch (IOException e) {
                ZXLogger.warning("写出图片失败 " + e.getMessage());
            }
        }
        return paths;
    }
}
