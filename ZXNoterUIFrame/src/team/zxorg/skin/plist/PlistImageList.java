package team.zxorg.skin.plist;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PlistImageList {
    private final List<SubImage> images;
    private String textureFileName;

    public List<SubImage> getImages() {
        return images;
    }

    public String getTextureFileName() {
        return textureFileName;
    }

    public PlistImageList(Path plistPath) {
        try {
            // 读取plist文件
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(plistPath.toFile());
            // 获取根元素
            Element rootElement = document.getDocumentElement();
            Element rootPlist = (Element) rootElement.getElementsByTagName("dict").item(0);
            Element frames = (Element) getNode(rootPlist, "frames");
            Element metadata = (Element) getNode(rootPlist, "metadata");
            textureFileName = getNode(metadata, "textureFileName").getTextContent();
            Path textureFilePath = plistPath.getParent().resolve(textureFileName);
            BufferedImage textureImage = ImageIO.read(textureFilePath.toFile());
            images = new ArrayList<>();
            Node frameNode = frames.getFirstChild().getNextSibling();
            while (frameNode != null) {
                String name = frameNode.getTextContent();
                Node frameInfo = frameNode.getNextSibling().getNextSibling();
                SubImage image = new SubImage(textureImage, name, (Element) frameInfo);
                images.add(image);
                frameNode = frameInfo.getNextSibling().getNextSibling();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static Node getNode(Element parent, String keyName) {
        NodeList nodeList = parent.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("key")) {
                if (node.getTextContent().equals(keyName)) {
                    return node.getNextSibling().getNextSibling();
                }
            }
        }
        return null;
    }
}
