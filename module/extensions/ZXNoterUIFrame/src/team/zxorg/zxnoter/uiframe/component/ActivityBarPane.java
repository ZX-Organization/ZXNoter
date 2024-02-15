package team.zxorg.zxnoter.uiframe.component;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.uiframe.ProjectView;
import team.zxorg.zxnoter.uiframe.ZXNoter;
import team.zxorg.zxnoter.uiframe.config.ActivityBarConfig;

public class ActivityBarPane extends HBox {

    ActivityBarConfig config = ZXNoter.config.get(ActivityBarConfig.class);
    private final ProjectView projectView;
    BorderPane activityBorderPane = new BorderPane();
    /**
     * 主活动栏
     */
    ActivityBar mainActivityBar;
    /**
     * 底部活动栏
     */
    ActivityBar mainBottomActivityBar;
    /**
     * 次活动栏
     */
    ActivityBar secondActivityBar;


    public ActivityBarPane(ProjectView projectView, Node contentNode) {
        VBox.setVgrow(this, Priority.ALWAYS);
        getStyleClass().add("activity-border-pane");
        this.projectView = projectView;
        mainActivityBar = new ActivityBar();
        mainBottomActivityBar = new ActivityBar();
        secondActivityBar = new ActivityBar();
        mainActivityBar.getStyleClass().addAll("left");
        mainBottomActivityBar.getStyleClass().addAll("left");
        secondActivityBar.getStyleClass().addAll("right");

        activityBorderPane.setCenter(contentNode);
        HBox.setHgrow(activityBorderPane, Priority.ALWAYS);


        VBox mainBar = new VBox(mainActivityBar, mainBottomActivityBar);
        VBox.setVgrow(mainBottomActivityBar, Priority.ALWAYS);

        getChildren().addAll(mainBar, activityBorderPane, secondActivityBar);

        config.save();

        //实例化所有侧边栏
        /*for (Map.Entry<String, Class<? extends ActivitySideBar>> entry : sideBarClassMap.entrySet()) {
            ActivitySideBar sideBar = newInstance(entry.getValue());
            sideBar.bind(projectView, new ActivityItem(sideBar.getId()));
            sideBarMap.put(entry.getKey(), sideBar);
            System.out.println("实例化 " + sideBar);
        }*/


    }

    /*public static class ActivityBarConfig {
        List<String> topItems;
        List<String> bottomItems;
        String openedItem;

        public ActivityBarConfig() {
            topItems = new ArrayList<>();
            bottomItems = new ArrayList<>();
        }
    }*/


    private static class ActivityBar extends VBox {
        public ActivityBar() {
            getStyleClass().addAll("activity-bar");
        }
    }
}
