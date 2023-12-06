import team.zxorg.zxncore.lib.plist.PlistImageList;
import team.zxorg.zxncore.lib.plist.SubImage;

import javax.imageio.ImageIO;
import java.nio.file.Files;
import java.nio.file.Path;

public class PlistTest {

    public static void main(String[] args) throws Exception {

        Files.list(Path.of("D:\\malody\\skin\\ttb5测试")).forEach(path -> {
            if (!path.getFileName().toString().endsWith(".plist"))
                return;
            try {
                PlistImageList plistImageList = new PlistImageList(path);
                for (SubImage image : plistImageList.getImages()) {
                    ImageIO.write(image.getImage(), "png", path.getParent().resolve(image.getImageName()).toFile());
                }

                Files.delete(path);
                Files.delete(path.getParent().resolve(plistImageList.getTextureFileName()));

            } catch (Exception e) {
            }

        });


    }


}
