package team.zxorg.zxnoter.ui.main.stage.side;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import team.zxorg.zxnoter.config.ZXProjectManager;
import team.zxorg.zxnoter.ui.main.ZXStage;
import team.zxorg.zxnoter.ui.main.stage.side.filemanager.FileManagerTab;

public class SideBar extends TabPane {
    ObservableList<String> styleClass = getStyleClass();
    double lastWeight = 0;
    double foldWeight = 0;
    double minWeight = 220;
    double maxWeight = 460;

    BooleanProperty isAdjust = new SimpleBooleanProperty(false);//调整宽度
    public BooleanProperty isFold = new SimpleBooleanProperty(false);//是否是折叠
    MouseEvent pressedMouseEvent;
    ZXStage zxStage;

    public SideBar(ZXStage zxStage) {
        this.zxStage = zxStage;
        styleClass.add("side-bar");
        setMinWidth(Region.USE_PREF_SIZE);
        setSide(Side.LEFT);
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        setTabWidth(minWeight);
        lastWeight = minWeight;

        isAdjust.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                styleClass.add("adjust-side-bar");
            } else {
                styleClass.remove("adjust-side-bar");
            }
        });

        isFold.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                foldWeight = getTabWidth();
                setTabWidth(0);
                getStyleClass().add("fold-side-bar");
            } else {
                if (foldWeight == 0)
                    foldWeight = minWeight;
                setTabWidth(foldWeight);
                getStyleClass().remove("fold-side-bar");
            }
        });

        setOnMouseMoved(event -> {
            boolean canAdjust = getWidth() - event.getX() < 6;
            isAdjust.set(canAdjust);
        });
        setOnMouseReleased(event -> {
            pressedMouseEvent = null;
            isAdjust.set(false);
        });
        setOnMousePressed(event -> {
            pressedMouseEvent = event;
            lastWeight = getTabWidth();
        });
        setOnMouseExited(event -> {
            if (!isFold.get() && pressedMouseEvent == null)
                isAdjust.set(false);
        });
        setOnMouseDragged(event -> {
            boolean canAdjust = getTabWidth() - event.getX() < 6;
            if (canAdjust || isAdjust.get()) {
                if (pressedMouseEvent != null) {
                    double w = lastWeight + event.getX() - pressedMouseEvent.getX();
                    if (w < minWeight)
                        w = w < minWeight / 2 ? 0 : minWeight;
                    w = Math.min(w, maxWeight);
                    isFold.set(w == 0);
                    if (isAdjust.get())
                        setTabWidth(w);
                }
            }
        });


        {
            FileManagerTab fileManagerTab = new FileManagerTab(zxStage);
            getTabs().add(fileManagerTab);
        }
        /*{
            new BaseSideBarTab("side-bar.menu.pack-up", "document.folder-upload", this);
        }*/
    }

    private void setTabWidth(double width) {
        setPrefWidth(width + 50);
    }

    private double getTabWidth() {
        return getPrefWidth() - 50;
    }

    public ZXProjectManager getZXProject() {
        return zxStage.zxProjectManager;
    }

}
