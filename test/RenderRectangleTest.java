import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import team.zxorg.zxnoter.ui.render.basis.RenderRectangle;

public class RenderRectangleTest {
    public static void main(String[] args) {
        RenderRectangle rectangle = new RenderRectangle(0, 0, 100, 100);
        rectangle.scale(Pos.CENTER,40, Orientation.HORIZONTAL);
        System.out.println(rectangle);
    }
}
