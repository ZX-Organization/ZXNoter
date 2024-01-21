package team.zxorg.fxcl.resource;

import javafx.scene.shape.SVGPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.stream.IntStream;

public class ResourceUtils {
    public static String readSvg(String svg) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(svg)));

            StringBuilder stringBuilder = new StringBuilder();

            Element svgElement = document.getDocumentElement();
            String viewBox = svgElement.getAttribute("viewBox");
            String[] size = viewBox.split(" ");
            if (size.length != 4) {
                throw new RuntimeException("没有指定大小！");
            }

            // 使用 Stream API 简化元素遍历和过滤的操作
            NodeList pathList = svgElement.getElementsByTagName("path");
            IntStream.range(0, pathList.getLength())
                    .mapToObj(pathList::item)
                    .filter(node -> node instanceof Element)
                    .map(node -> (Element) node)
                    .filter(element -> !element.getAttribute("fill").equalsIgnoreCase("none"))
                    .map(element -> element.getAttribute("d"))
                    .filter(dAttribute -> !dAttribute.equalsIgnoreCase("M" + size[0] + "," + size[1] + "h" + size[2] + "v" + size[3] + "h-" + size[2] + "Z"))
                    .forEach(dAttribute -> stringBuilder.append(dAttribute).append(" "));

            // 处理 <polygon> 元素
            NodeList polygonList = svgElement.getElementsByTagName("polygon");
            IntStream.range(0, polygonList.getLength())
                    .mapToObj(polygonList::item)
                    .filter(node -> node instanceof Element)
                    .map(node -> (Element) node)
                    .map(element -> element.getAttribute("points"))
                    .map(ResourceUtils::convertPolygonToPath)
                    .forEach(pathData -> stringBuilder.append(pathData).append(" "));

            if (stringBuilder.isEmpty()) {
                return "";
            }

            stringBuilder.append("M").append(size[0]).append(",").append(size[1]).append("z ");
            stringBuilder.append("M").append(size[2]).append(",").append(size[3]).append("z");

            return stringBuilder.toString();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return "";
        }
    }

    private static String convertPolygonToPath(String points) {
        StringBuilder pathBuilder = new StringBuilder("M");
        String[] coordinates = points.split("\\s+");

        for (int i = 0; i < coordinates.length; i++) {
            String coordinate = coordinates[i];
            String[] xy = coordinate.split(",");
            double x = Double.parseDouble(xy[0]);
            double y = Double.parseDouble(xy[1]);

            if (i == 0) {
                pathBuilder.append(x).append(",").append(y);
            } else {
                pathBuilder.append(" L").append(x).append(",").append(y);
            }
        }

        pathBuilder.append(" Z");
        return pathBuilder.toString();
    }

    public static String getFileExtension(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return ""; // 文件没有扩展名
        }
        return fileName.substring(dotIndex + 1);
    }

}
