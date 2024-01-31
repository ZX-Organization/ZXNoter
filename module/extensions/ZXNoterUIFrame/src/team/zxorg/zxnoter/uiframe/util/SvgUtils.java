package team.zxorg.zxnoter.uiframe.util;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.attributes.ViewBox;
import com.github.weisj.jsvg.geometry.size.FloatSize;
import com.github.weisj.jsvg.parser.SVGLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SvgUtils {
    public static void main(String[] args) throws IOException {
        Path iconRootPath = Path.of("D:\\Projects\\ZXNoter\\resourcepacks\\iconPack-line-fast\\icon");
        double iconSize = 256;
        SVGLoader svgLoader = new SVGLoader();
        for (Path file : Files.walk(iconRootPath).toList()) {
            if (file.toString().endsWith(".svg")) {
                SVGDocument svgDocument = svgLoader.load(Files.newInputStream(file));
                FloatSize size = svgDocument.size();
                size.setSize(iconSize, iconSize);

                BufferedImage image = new BufferedImage((int) size.width, (int) size.height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                svgDocument.render(null, g, new ViewBox(size));
                g.dispose();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    ImageIO.write(image, "png", outputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Files.delete(file);

                String fileName = file.getFileName().toString().substring(0, file.getFileName().toString().lastIndexOf("."));
                Files.write(file.getParent().resolve(fileName + ".png"), outputStream.toByteArray());
            }
        }
    }
}
