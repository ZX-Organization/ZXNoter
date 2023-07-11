package team.zxorg.zxnoter.ui.main.one.two.three;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.component.TrackTooltip;

public class SideBar extends TabPane {
    ObservableList<String> styleClass = getStyleClass();
    double lastWeight = 0;
    double foldWeight = 0;
    double minWeight = 160;
    double maxWeight = 400;

    BooleanProperty isAdjust = new SimpleBooleanProperty(false);//调整宽度
    BooleanProperty isFold = new SimpleBooleanProperty(false);//是否是折叠
    MouseEvent pressedMouseEvent;

    public SideBar() {
        styleClass.add("side-bar");
        setMinWidth(50);
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
            Pane pane = new Pane();
            createTab("show.hello", "document.folder-4", pane);
        }
        {
            Pane pane = new Pane();
            createTab("show.hello", "document.folder-upload", pane);
        }
    }

    public void setTabWidth(double width) {
        setPrefWidth(width + 50);
    }

    public double getTabWidth() {
        return getWidth() - 50;
    }

    public void createTab(String tipLanguageKey, String iconKey, Pane content) {
        Tab tab = new Tab();
        tab.setClosable(false);
        Pane iconPane = ZXResources.getIconPane(iconKey, 30);
        iconPane.setOnMousePressed(event -> {
            if (getSelectionModel().getSelectedItem().equals(tab)) {
                isFold.set(!isFold.get());
                event.consume();
            } else {
                isFold.set(false);
            }
        });

        TrackTooltip trackTooltip = new TrackTooltip(iconPane, Pos.BOTTOM_CENTER, 0, TrackTooltip.BindAttributes.AUTO_POP_UP);
        trackTooltip.setPos(Pos.CENTER_RIGHT, false,14);
        trackTooltip.setText(ZXResources.getLanguageContent(tipLanguageKey));

        //ComponentFactory.getTooltip(tipLanguageKey, iconPane, 12);
        tab.setGraphic(iconPane);
        tab.setContent(content);
        getTabs().add(tab);
    }
}
