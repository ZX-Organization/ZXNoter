package team.zxorg.newskin.plist;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SubImage {
    private final String imageName;
    private Point spriteOffset;
    private Dimension spriteSize;
    private Dimension spriteSourceSize;
    private Rectangle textureRect;
    private Boolean textureRotated;

    public Point getSpriteOffset() {
        return spriteOffset;
    }

    public Dimension getSpriteSize() {
        return spriteSize;
    }

    public Dimension getSpriteSourceSize() {
        return spriteSourceSize;
    }

    public Rectangle getTextureRect() {
        return textureRect;
    }

    public Boolean getTextureRotated() {
        return textureRotated;
    }

    private BufferedImage image;

    public String getImageName() {
        return imageName;
    }

    public BufferedImage getImage() {
        return image;
    }

    public SubImage(BufferedImage sourceImage, String imageName, Element info) {
        //System.out.println(imageName);
        this.imageName = imageName;

        // Parse spriteOffset
        this.spriteOffset = parsePoint(info, "spriteOffset");
        if (spriteOffset == null)
            this.spriteOffset = parsePoint(info, "offset");

        // Parse spriteSize
        this.spriteSize = parseDimension(info, "spriteSize");

        // Parse spriteSourceSize
        this.spriteSourceSize = parseDimension(info, "spriteSourceSize");
        if (spriteSourceSize == null)
            this.spriteSourceSize = parseDimension(info, "sourceSize");

        // Parse textureRect
        this.textureRect = parseRectangle(info, "textureRect");
        if (textureRect == null)
            this.textureRect = parseRectangle(info, "frame");

        // Parse textureRotated
        textureRotated = parseBoolean(info, "textureRotated");
        if (textureRotated == null)
            this.textureRotated = parseBoolean(info, "rotated");


        this.image = extractImage(sourceImage, this.textureRect, textureRotated);
        //System.out.println(textureRotated);

    }

    private BufferedImage extractImage(BufferedImage sourceImage, Rectangle region, boolean rotated) {
        if (rotated) {
            BufferedImage subImage = sourceImage.getSubimage(region.x, region.y, region.height, region.width);
            return rotateImage(subImage);
        } else {
            //System.out.println(region.x + region.width);
            return sourceImage.getSubimage(region.x, region.y, region.width, region.height);
        }
    }

    private BufferedImage rotateImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage rotatedImage = new BufferedImage(height, width, image.getType());
        Graphics2D g2d = rotatedImage.createGraphics();
        g2d.rotate(Math.toRadians(-90), width / 2.0, height / 2.0);
        g2d.drawImage(image, (height - width) / 2, (height - width) / 2, null);
        g2d.dispose();

        return rotatedImage;
    }

    private Point parsePoint(Element parent, String keyName) {
        Node node = PlistImageList.getNode(parent, keyName);
        if (node == null) return null;
        String pointString = node.getTextContent();
        String[] values = pointString.replaceAll("[{}]", "").split(",");
        int x = (int) Float.parseFloat(values[0].trim());
        int y = (int) Float.parseFloat(values[1].trim());
        return new Point(x, y);
    }

    private Dimension parseDimension(Element parent, String keyName) {
        Node node = PlistImageList.getNode(parent, keyName);
        if (node == null) return null;
        String dimensionString = node.getTextContent();
        String[] values = dimensionString.replaceAll("[{}]", "").split(",");
        int width = Integer.parseInt(values[0].trim());
        int height = Integer.parseInt(values[1].trim());
        return new Dimension(width, height);
    }

    private Rectangle parseRectangle(Element parent, String keyName) {
        Node node = PlistImageList.getNode(parent, keyName);
        if (node == null) return null;
        String rectangleString = node.getTextContent();
        String[] values = rectangleString.replaceAll("[{}]", "").split(",");
        int x = Integer.parseInt(values[0].trim());
        int y = Integer.parseInt(values[1].trim());
        int width = Integer.parseInt(values[2].trim());
        int height = Integer.parseInt(values[3].trim());
        return new Rectangle(x, y, width, height);
    }

    private Boolean parseBoolean(Element parent, String keyName) {
        Node node = PlistImageList.getNode(parent, keyName);
        if (node == null) return null;
        return node.getNodeName().equals("true");
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "imageName='" + imageName + '\'' +
                ", spriteOffset=" + spriteOffset +
                ", spriteSize=" + spriteSize +
                ", spriteSourceSize=" + spriteSourceSize +
                ", textureRect=" + textureRect +
                ", textureRotated=" + textureRotated +
                '}';
    }
}
