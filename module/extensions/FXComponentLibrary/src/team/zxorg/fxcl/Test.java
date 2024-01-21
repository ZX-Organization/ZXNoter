package team.zxorg.fxcl;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import team.zxorg.extensionloader.core.Configuration;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.fxcl.component.LangLabel;
import team.zxorg.fxcl.resource.IconManager;

public class Test implements ExtensionEntrypoint {
    public String cnmd = "WDNMD";

    @Override
    public void onInitialize(Extension extension, ExtensionManager manager) {
        Configuration<Test> config = new Configuration<>("Test");
        config.save();
        System.out.println(config.getConfig().cnmd);

        Logger.warning("测试FX组件库");

        //屏蔽javafx歌姬初始化时的异常
        Logging.getJavaFXLogger().disableLogging();
        Logger.info("初始化FX图形系统");

        PlatformImpl.startup(() -> {
            //再次开启javafx日志
            Logging.getJavaFXLogger().enableLogging();
            StringProperty sp = new SimpleStringProperty("HELLO");


            IconManager.loadIconPack(extension.getClass().getClassLoader(), "assets/fxComponentLibrary/baseResourcePack/icon/line/software/");

            Pane svgIcon = new Pane();
            svgIcon.setMinSize(80, 80);
            svgIcon.setMaxSize(80, 80);
            SVGPath svgPath = new SVGPath();
            //svgPath.setContent(ResourceUtils.readSvg(extension.getResourceAsString("baseResourcePack/icon/line/software/logo.svg")));
            svgIcon.setShape(svgPath);
            svgIcon.setBackground(Background.fill(Color.GREEN));

            VBox root = new VBox(svgIcon, new LangLabel("message.language.noCode", sp));
            sp.setValue("nnnn");
            root.setSpacing(20);
            root.setAlignment(Pos.CENTER);
            root.setBackground(Background.fill(Color.BLACK));

            Scene scene = new Scene(root);

            scene.getStylesheets().addAll(extension.getResource("baseResourcePack/color/style.css").toString());
            scene.getStylesheets().addAll(extension.getResource("baseResourcePack/color/dark.css").toString());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();


        });

    }
}
