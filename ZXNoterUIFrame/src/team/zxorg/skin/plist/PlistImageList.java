package team.zxorg.skin.plist;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PlistImageList {
    private final List<SubImage> images;
    private final String textureFileName;
    private final Path textureFilePath;

    public List<SubImage> getImages() {
        return images;
    }

    public String getTextureFileName() {
        return textureFileName;
    }

    public PlistImageList(Path plistPath) throws ParserConfigurationException, IOException, SAXException {
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
         textureFilePath = plistPath.getParent().resolve(textureFileName);
            images = new ArrayList<>();
            Node frameNode = frames.getFirstChild().getNextSibling();
            while (frameNode != null) {
                String name = frameNode.getTextContent();
                Node frameInfo = frameNode.getNextSibling().getNextSibling();
                SubImage image = new SubImage(name, (Element) frameInfo);
                images.add(image);
                frameNode = frameInfo.getNextSibling().getNextSibling();
            }
    }

    public BufferedImage getTexture() throws IOException {
        return ImageIO.read(textureFilePath.toFile());
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
