package team.zxorg.zxnoter.ui.main.component;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.resource_old.ZXResources;
import team.zxorg.zxnoter.ui.ComponentFactory;

public class TitleBar extends HBox {
    //菜单栏
    public MenuBar menuBar = new MenuBar();

    public TitleBar() {

        //标题栏
        setBackground(Background.fill(Color.RED));
        setPrefSize(Region.USE_COMPUTED_SIZE, 30);
        setMinHeight(Region.USE_PREF_SIZE);
        getStyleClass().add("title-bar");
        ImageView zxnIcon = new ImageView(ZXResources.getImage("img.zxnoter.zxnoter-x26"));
        HBox.setMargin(zxnIcon, new Insets(2, 4, 2, 4));


        //菜单栏
        menuBar.setPadding(new Insets(0));
        getChildren().addAll(zxnIcon, menuBar);


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


                getMenus().add(fileMenu);
            }
        }


    }

    public ObservableList<Menu> getMenus() {
        return menuBar.getMenus();
    }
}
