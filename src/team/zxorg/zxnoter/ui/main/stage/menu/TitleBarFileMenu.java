package team.zxorg.zxnoter.ui.main.stage.menu;

import javafx.scene.control.SeparatorMenuItem;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.resource.project.ZXProject;
import team.zxorg.zxnoter.ui.component.ComponentFactory;
import team.zxorg.zxnoter.ui.component.ZXIcon;
import team.zxorg.zxnoter.ui.component.ZXMenu;
import team.zxorg.zxnoter.ui.component.ZXMenuItem;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.SettingViewEditor;


public class TitleBarFileMenu extends ZXMenu {
    public ZXProject zxProject;

    public TitleBarFileMenu(ZXProject zxProject) {
        super("title-bar.file");
        this.zxProject = zxProject;
        {//构建菜单
            {//文件
                {//创建

                    ZXMenuItem createProject = new ZXMenuItem("menu.file.create-project", new ZXIcon("document.file-ppt", ZXColor.FONT_USUALLY, 18));
                    createProject.setOnAction(event -> {
                        System.out.println("创建一个项目");
                    });


                    //打开项目或文件
                    javafx.scene.control.MenuItem openMenu = ComponentFactory.menuItem("title-bar.file.open");
                    openMenu.setGraphic(ZXResources.getIconPane("document.folder-open", 18, ZXColor.FONT_LIGHT));
                    openMenu.setOnAction(event -> {
                        zxProject.openProject();
                    });

                    //打开最近
                    javafx.scene.control.Menu recentMenu = ComponentFactory.menu("title-bar.file.recent");
                    recentMenu.setGraphic(ZXResources.getIconPane("system.time", 18, ZXColor.FONT_LIGHT));
                    {

                        //测试用 列出最近打开的项目
                        for (int i = 0; i < 6; i++) {
                            recentMenu.getItems().add(new javafx.scene.control.MenuItem("项目" + i));
                        }
                    }

                    //关闭项目
                    javafx.scene.control.MenuItem closeMenu = ComponentFactory.menuItem("title-bar.file.close");
                    closeMenu.setGraphic(ZXResources.getIconPane("system.close", 18, ZXColor.FONT_LIGHT));
                    closeMenu.setOnAction(event -> {
                        zxProject.closeProject();
                    });

                    //设置
                    javafx.scene.control.MenuItem settingMenu = ComponentFactory.menuItem("title-bar.file.setting");
                    settingMenu.setGraphic(ZXResources.getIconPane("system.settings-3", 18, ZXColor.FONT_LIGHT));
                    settingMenu.setOnAction(event -> {
                        zxProject.
                                zxStage.editorArea.rootEditorTabPane.createEditor(new SettingViewEditor(zxProject));
                    });

                    //退出
                    javafx.scene.control.MenuItem quitMenu = ComponentFactory.menuItem("title-bar.file.quit");
                    quitMenu.setGraphic(ZXResources.getIconPane("others.door-closed", 18, ZXColor.FONT_LIGHT));
                    quitMenu.setOnAction(event -> {
                        System.out.println("退出");
                    });

                    getItems().addAll(new CreatFileMenu(), createProject, openMenu, recentMenu, closeMenu, new SeparatorMenuItem(), settingMenu, quitMenu);
                }


            }
        }
    }
}
