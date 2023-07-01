package team.zxorg.zxnoter.ui.main;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.resource_old.ZXResources;
import team.zxorg.zxnoter.ui.ComponentFactory;
import team.zxorg.zxnoter.ui.main.component.EditorArea;
import team.zxorg.zxnoter.ui.main.component.SideBar;
import team.zxorg.zxnoter.ui.main.component.StatusBar;
import team.zxorg.zxnoter.ui.main.component.TitleBar;

public class MainWindow {

    public TitleBar titleBar = new TitleBar();//标题栏
    public SideBar sideBar = new SideBar();//侧边栏
    public EditorArea editorArea = new EditorArea();//编辑器区域
    public StatusBar statusBar = new StatusBar();//状态栏
    HBox body = new HBox(sideBar, editorArea);
    VBox main = new VBox(titleBar, body, statusBar);

    public Scene init(String theme) {
        Scene scene = new Scene(main);
        //应用样式
        scene.getStylesheets().add(ZXResources.getPath("css.root").toUri().toString());
        scene.getStylesheets().add(ZXResources.getPath("css.theme." + theme).toUri().toString());


        {//构建菜单
            {//文件
                Menu fileMenu = ComponentFactory.menu("title-bar.menu.file");
                {//创建
                    Menu createMenu = ComponentFactory.menu("title-bar.menu.file.create");
                    createMenu.setGraphic(ZXResources.getSvgPane("svg.icons.document.folder-add-line", 18, "gray"));

                    {//项目
                        MenuItem createProject = ComponentFactory.menuItem("title-bar.menu.file.create.project");
                        createProject.setGraphic(ZXResources.getSvgPane("svg.icons.document.file-ppt-line", 18, "purple"));
                        createProject.setOnAction(event -> {
                            System.out.println("创建一个项目");
                        });
                        MenuItem createMap = ComponentFactory.menuItem("title-bar.menu.file.create.zxmap");
                        createMap.setGraphic(ZXResources.getSvgPane("svg.icons.zxnoter.file-notemap-line", 18, "yellow"));
                        createMap.setOnAction(event -> {
                            System.out.println("创建一个谱面");
                        });

                        createMenu.getItems()
                                .addAll(createProject,
                                        createMap);
                    }

                    //打开项目或文件
                    MenuItem openMenu = ComponentFactory.menuItem("title-bar.menu.file.open");
                    openMenu.setGraphic(ZXResources.getSvgPane("svg.icons.document.folder-open-line", 18, "gray"));
                    openMenu.setOnAction(event -> {
                        System.out.println("打开一个项目或文件");
                    });

                    //打开最近
                    Menu recentMenu = ComponentFactory.menu("title-bar.menu.file.recent");
                    recentMenu.setGraphic(ZXResources.getSvgPane("svg.icons.system.time-line", 18, "gray"));
                    {

                        //测试用 列出最近打开的项目
                        for (int i = 0; i < 6; i++) {
                            recentMenu.getItems().add(new MenuItem("项目" + i));
                        }
                    }

                    //关闭项目
                    MenuItem closeMenu = ComponentFactory.menuItem("title-bar.menu.file.close");
                    closeMenu.setGraphic(ZXResources.getSvgPane("svg.icons.system.close-line", 18, "gray"));
                    closeMenu.setOnAction(event -> {
                        System.out.println("关闭项目");
                    });

                    //设置
                    MenuItem settingMenu = ComponentFactory.menuItem("title-bar.menu.file.setting");
                    settingMenu.setGraphic(ZXResources.getSvgPane("svg.icons.system.settings-3-line", 18, "gray"));
                    settingMenu.setOnAction(event -> {
                        System.out.println("设置");
                    });

                    //退出
                    MenuItem quitMenu = ComponentFactory.menuItem("title-bar.menu.file.quit");
                    quitMenu.setGraphic(ZXResources.getSvgPane("svg.icons.others.door-closed-line", 18, "gray"));
                    quitMenu.setOnAction(event -> {
                        System.out.println("退出");
                    });


                    fileMenu.getItems().addAll(createMenu, openMenu, recentMenu, closeMenu, new SeparatorMenuItem(), settingMenu, quitMenu);
                }


                titleBar.menuBar.getMenus().add(fileMenu);
            }
        }

        return scene;
    }
}
