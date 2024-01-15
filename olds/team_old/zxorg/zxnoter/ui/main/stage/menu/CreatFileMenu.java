package team.zxorg.zxnoter.ui.main.stage.menu;

import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.component.ComponentFactory;
import team.zxorg.zxnoter.ui.component.ZXIcon;
import team.zxorg.zxnoter.ui.component.ZXMenu;
import team.zxorg.zxnoter.ui.component.ZXMenuItem;

public class CreatFileMenu extends ZXMenu {
    public CreatFileMenu() {
        super("menu.file.create-file",new ZXIcon("document.file-add",ZXColor.FONT_LIGHT,18));

        ZXMenuItem createMap = new ZXMenuItem("title-bar.file.create.zxmap",new ZXIcon("software.file-zxm",ZXColor.YELLOW,18));
        createMap.setOnAction(event -> {
            System.out.println("创建一个谱面");
        });

        getItems().addAll(createMap);
    }
}
