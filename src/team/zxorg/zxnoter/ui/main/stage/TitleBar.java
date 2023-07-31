package team.zxorg.zxnoter.ui.main.stage;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.resource.project.ZXProject;
import team.zxorg.zxnoter.ui.component.ComponentFactory;
import team.zxorg.zxnoter.ui.main.ZXStage;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.SettingEditor;

public class TitleBar extends HBox {
    //菜单栏
    public MenuBar menuBar = new MenuBar();
    ZXProject zxProject;

    public TitleBar(ZXStage zxStage) {
        this.zxProject = zxStage.project;
        //标题栏
        setPrefSize(Region.USE_COMPUTED_SIZE, 30);
        setMinHeight(Region.USE_PREF_SIZE);
        getStyleClass().add("title-bar");
        ImageView zxnIcon = new ImageView(ZXResources.LOGO_X26);//ZXResources.getImage("img.zxnoter.zxnoter-x26")
        HBox.setMargin(zxnIcon, new Insets(2, 4, 2, 4));


        //菜单栏
        menuBar.setPadding(new Insets(0));
        getChildren().addAll(zxnIcon, menuBar);


        {//构建菜单
            {//文件
                Menu fileMenu = ComponentFactory.menu("title-bar.file");
                {//创建
                    Menu createMenu = ComponentFactory.menu("title-bar.file.create");
                    createMenu.setGraphic(ZXResources.getIconPane("document.folder-add", 18, ZXColor.FONT_USUALLY));

                    {//项目
                        MenuItem createProject = ComponentFactory.menuItem("title-bar.file.create.project");
                        createProject.setGraphic(ZXResources.getIconPane("document.file-ppt", 18, ZXColor.FONT_USUALLY));
                        createProject.setOnAction(event -> {
                            System.out.println("创建一个项目");
                        });
                        MenuItem createMap = ComponentFactory.menuItem("title-bar.file.create.zxmap");
                        createMap.setGraphic(ZXResources.getIconPane("software.file-zxm", 18, ZXColor.YELLOW));
                        createMap.setOnAction(event -> {
                            System.out.println("创建一个谱面");
                        });

                        createMenu.getItems()
                                .addAll(createProject,
                                        createMap);
                    }

                    //打开项目或文件
                    MenuItem openMenu = ComponentFactory.menuItem("title-bar.file.open");
                    openMenu.setGraphic(ZXResources.getIconPane("document.folder-open", 18, ZXColor.FONT_LIGHT));
                    openMenu.setOnAction(event -> {
                        zxProject.openProject(getScene().getWindow());
                    });

                    //打开最近
                    Menu recentMenu = ComponentFactory.menu("title-bar.file.recent");
                    recentMenu.setGraphic(ZXResources.getIconPane("system.time", 18, ZXColor.FONT_LIGHT));
                    {

                        //测试用 列出最近打开的项目
                        for (int i = 0; i < 6; i++) {
                            recentMenu.getItems().add(new MenuItem("项目" + i));
                        }
                    }

                    //关闭项目
                    MenuItem closeMenu = ComponentFactory.menuItem("title-bar.file.close");
                    closeMenu.setGraphic(ZXResources.getIconPane("system.close", 18, ZXColor.FONT_LIGHT));
                    closeMenu.setOnAction(event -> {
                        zxProject.closeProject();
                    });

                    //设置
                    MenuItem settingMenu = ComponentFactory.menuItem("title-bar.file.setting");
                    settingMenu.setGraphic(ZXResources.getIconPane("system.settings-3", 18, ZXColor.FONT_LIGHT));
                    settingMenu.setOnAction(event -> {

                        zxStage.editorArea.editorTabPane.createEditor(new SettingEditor());
                    });

                    //退出
                    MenuItem quitMenu = ComponentFactory.menuItem("title-bar.file.quit");
                    quitMenu.setGraphic(ZXResources.getIconPane("others.door-closed", 18, ZXColor.FONT_LIGHT));
                    quitMenu.setOnAction(event -> {
                        System.out.println("退出");
                    });

                    fileMenu.getItems().addAll(createMenu, openMenu, recentMenu, closeMenu, new SeparatorMenuItem(), settingMenu, quitMenu);
                }


                getMenus().add(fileMenu);
            }
        }


    }

    public ObservableList<Menu> getMenus() {
        return menuBar.getMenus();
    }
}