import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import team.zxorg.skin.uis.UISPerspectiveTransform;
import team.zxorg.ui.component.ResizableCanvas;
import team.zxorg.zxncore.ZXLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UISPerspectiveTransformTest {
    public static void main(String[] args) {

        ZXLogger.info("UISPerspectiveTransformTest");
        //屏蔽javafx歌姬初始化时的异常
        Logging.getJavaFXLogger().disableLogging();
        ZXLogger.info("初始化图形系统");
        PlatformImpl.startup(() -> {
            //再次开启javafx日志
            Logging.getJavaFXLogger().enableLogging();


            try {
                testWindow();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }

    public static void testWindow() throws IOException {
        Image testImage = new Image(Files.newInputStream(Path.of("D:\\malody\\skin\\Editor测试\\preview.png")));
        ResizableCanvas canvas = new ResizableCanvas();
        HBox.setHgrow(canvas, Priority.ALWAYS);
        HBox hBox = new HBox(canvas);
        Scene scene = new Scene(hBox);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMinWidth(1280);
        stage.setMinHeight(720);
        stage.setMaxWidth(1280);
        stage.setMaxHeight(720);
        stage.show();
        double canvasWidth = 1280;
        double canvasHeight = 720;


        GraphicsContext gc = canvas.getGraphicsContext2D();


        UISPerspectiveTransform uisPt = new UISPerspectiveTransform();
        UISPerspectiveTransform texPt = new UISPerspectiveTransform();
        uisPt.setSize(1280, 720);
        uisPt.setAngle(45);

        PerspectiveTransform pt = new PerspectiveTransform();
        new AnimationTimer() {
            @Override
            public void handle(long l) {

                double x = 400;
                double y = 100;
                double w = 1200;
                double h = 600;

                double texX = 0;
                double texY = 0;
                double texW = testImage.getWidth();
                double texH = testImage.getHeight();


                Point2D ul = uisPt.transform(new Point2D(x, y));
                Point2D ur = uisPt.transform(new Point2D(x + w, y));
                Point2D lr = uisPt.transform(new Point2D(x + w, y + h));
                Point2D ll = uisPt.transform(new Point2D(x, y + h));

                texPt.setSize(texW, texH);
                texPt.setUnitQuadMapping(ul.getX(), ul.getY(), ur.getX(), ur.getY(), lr.getX(), lr.getY(), ll.getX(), ll.getY());


                Point2D cutoff = new Point2D(640, 360);

                //限制在屏幕内的图形区域
                Point2D inUL = new Point2D(Math.max(0, Math.min(canvasWidth, ul.getX())), Math.max(0, Math.min(canvasHeight, ul.getY())));
                Point2D inUR = new Point2D(Math.max(0, Math.min(canvasWidth, ur.getX())), Math.max(0, Math.min(canvasHeight, ur.getY())));
                Point2D inLR = new Point2D(Math.max(0, Math.min(canvasWidth, lr.getX())), Math.max(0, Math.min(canvasHeight, lr.getY())));
                Point2D inLL = new Point2D(Math.max(0, Math.min(canvasWidth, ll.getX())), Math.max(0, Math.min(canvasHeight, ll.getY())));


                Point2D texUL = texPt.untransform(inUL);
                Point2D texUR = texPt.untransform(inUR);
                Point2D texLR = texPt.untransform(inLR);
                Point2D texLL = texPt.untransform(inLL);


                ul = texPt.transform(texUL);
                ur = texPt.transform(texUR);
                lr = texPt.transform(texLR);
                ll = texPt.transform(texLL);


                pt.setUlx(ul.getX());
                pt.setUly(ul.getY());
                pt.setUrx(ur.getX());
                pt.setUry(ur.getY());
                pt.setLlx(ll.getX());
                pt.setLly(ll.getY());
                pt.setLrx(lr.getX());
                pt.setLry(lr.getY());

                gc.setEffect(pt);
                gc.drawImage(testImage, texX, texY, texW, texH, 0, 0, 500, 500);
                gc.setEffect(null);

                gc.setFill(Color.DARKBLUE);
                gc.fillRect(cutoff.getX() - 1, cutoff.getY() - 1, 3, 3);

            }
        }.start();
    }
}
