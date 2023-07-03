package team.zxorg.zxnoter.resource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;

public class ResourceUtils {
    public static String readSvg(Path path) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 使用 try-with-resources 确保解析器和输入流在使用后自动关闭
            try (var inputStream = Files.newInputStream(path)) {
                Document document = builder.parse(inputStream);

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
                        .map(points -> convertPolygonToPath(points, size))
                        .forEach(pathData -> stringBuilder.append(pathData).append(" "));


                if (stringBuilder.length() == 0) {
                    throw new RuntimeException("不OK！");
                }

                stringBuilder.append("M").append(size[0]).append(",").append(size[1]).append("z ");
                stringBuilder.append("M").append(size[2]).append(",").append(size[3]).append("z");

                return stringBuilder.toString();
            }

        } catch (ParserConfigurationException | org.xml.sax.SAXException e) {
            e.printStackTrace();
            throw new IOException("解析 SVG 文件时出错！");
        }
    }

    private static String convertPolygonToPath(String points, String[] size) {
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

}
