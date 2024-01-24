package team.zxorg.fxcl;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.core.Resource;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.fxcl.component.Icon;
import team.zxorg.fxcl.component.LangButton;
import team.zxorg.fxcl.component.LangLabel;

public class Test implements ExtensionEntrypoint {

    @Override
    public void onInitialize(Extension extension, ExtensionManager manager) {

        Logger.warning("测试FX组件库");

        //屏蔽javafx歌姬初始化时的异常
        Logging.getJavaFXLogger().disableLogging();
        Logger.info("初始化FX图形系统");

        PlatformImpl.startup(() -> {
            //再次开启javafx日志
            Logging.getJavaFXLogger().enableLogging();
            StringProperty sp = new SimpleStringProperty("HELLO");


            Icon icon = new Icon("software.logo", 128);

            VBox root = new VBox(icon, new LangButton(new Icon("business.inbox-archive", 64, Color.WHITE), "software.name"), new LangLabel("message.language.noCode", sp));
            sp.setValue("nnnn");
            root.setSpacing(20);
            root.setAlignment(Pos.CENTER);
            root.setBackground(Background.fill(Color.BLACK));

            Scene scene = new Scene(root);

            scene.getStylesheets().addAll(Resource.getResourceToUrl("style/dark.css").toString());
            scene.getStylesheets().addAll(Resource.getResourceToUrl("style/style.css").toString());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();

        });

    }
}
