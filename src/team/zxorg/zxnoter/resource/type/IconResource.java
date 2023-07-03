package team.zxorg.zxnoter.resource.type;

import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;

public class IconResource {
    public String name;
    public SVGPath svgPath;
    public IconResource(String name, String shape) {
        this.name = name;
        this.svgPath = new SVGPath();
        svgPath.setContent(shape);
    }
}
