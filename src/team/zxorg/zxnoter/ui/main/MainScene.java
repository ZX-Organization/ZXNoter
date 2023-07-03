package team.zxorg.zxnoter.ui.main;

import javafx.scene.Scene;
import team.zxorg.zxnoter.resource_old.ZXResources;
import team.zxorg.zxnoter.ui.main.component.MainVBox;

public class MainScene {


    MainVBox mainVBox = new MainVBox();//身体

    public Scene init(String theme) {
        Scene scene = new Scene(mainVBox);
        //应用样式
        //scene.getStylesheets().add(ZXResources.getPath("css.root").toUri().toString());
        //scene.getStylesheets().add(ZXResources.getPath("css.theme." + theme).toUri().toString());

        return scene;
    }
}
