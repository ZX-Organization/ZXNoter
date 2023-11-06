package team.zxorg.zxnoter.ui;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.UUID;

public class TestCustomWindow extends Stage {
    int cwId;

    public TestCustomWindow() {
        VBox body = new VBox();

        body.setBackground(Background.fill(Color.RED));
        Scene scene = new Scene(body);
        setScene(scene);


        UUID uuid = UUID.randomUUID();

        setOpacity(0);
        setTitle(uuid.toString());

        PlatformImpl.startup(() -> {
            show();
            cwId = CustomWindow.createCustomWindow(uuid.toString());
            setOpacity(1);
        });

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_RIGHT);
        header.setBackground(Background.fill(Color.BLUE));

        header.setOnMouseMoved(event -> {
            CustomWindow.instanceDll.CW_NCMouseMove(cwId, (char) 2, (char) 0);
            CustomWindow.instanceDll.CW_NCMouseMove(cwId, (char) 2, (char) 1);
        });
        header.setOnMousePressed(event -> {
            CustomWindow.instanceDll.CW_NCMouseMove(cwId, (char) 2, (char) 2);
        });

        header.setOnMouseReleased(event -> {
            CustomWindow.instanceDll.CW_NCMouseMove(cwId, (char) 2, (char) 3);
        });

        body.getChildren().addAll(header,new TextField());

        //header.setOnMouseMoved(event -> CustomWindow.instanceDll.CW_MouseMove(cwId,false));


        Button miniButton = new Button("最小化");
        Button maxButton = new Button("窗口化");
        maxButton.setOnMouseMoved(event -> {
            System.out.println("移动");
            CustomWindow.instanceDll.CW_NCMouseMove(cwId, (char) 9, (char) 0);
            CustomWindow.instanceDll.CW_NCMouseMove(cwId, (char) 9, (char) 1);
        });
        maxButton.setOnMousePressed(event -> {
            //CustomWindow.instanceDll.CW_NCMouseMove(cwId, (char) 9, (char) 2);
        });
        maxButton.setOnMouseReleased(event -> {
           // CustomWindow.instanceDll.CW_NCMouseMove(cwId, (char) 9, (char) 3);
        });
        maxButton.setOnMouseClicked(event -> {
            CustomWindow.instanceDll.CW_NCMouseMove(cwId, (char) 9, (char) 4);
        });
        Button closeButton = new Button("关闭");


        header.getChildren().addAll(miniButton, maxButton, closeButton);
    }

    public static void main(String[] args) {
        //屏蔽javafx歌姬初始化时的异常
        Logging.getJavaFXLogger().disableLogging();


        PlatformImpl.startup(() -> {
            //再次开启javafx日志
            Logging.getJavaFXLogger().enableLogging();

            //TestCustomWindow zxStage = new TestCustomWindow();


            {
                Stage test = new Stage();
                test.setTitle("Test Custom Window 1");
                test.setWidth(400);
                test.setHeight(300);
                VBox body = new VBox();
                body.setBackground(Background.fill(Color.RED));
                Scene scene = new Scene(body);
                test.setScene(scene);
                test.show();

            }
            {
                Stage test = new Stage();
                test.setTitle("Test Custom Window 2");
                test.setWidth(400);
                test.setHeight(300);
                VBox body = new VBox();
                body.setBackground(Background.fill(Color.GOLD));
                Scene scene = new Scene(body);
                test.setScene(scene);
                test.show();
            }
            //CustomWindow.createCustomWindow("ZXNoter");
        });



    }
}
