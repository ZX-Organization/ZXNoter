package team.zxorg.zxnoter.uiframe.activitypane;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import team.zxorg.zxnoter.uiframe.ZXNoter;

import java.util.LinkedHashSet;

class ShowActivityPane extends VBox {
    ActivityBarPane activityBarPane;
    ShowActivityPanePosition pos;
    private static final ActivityBarConfig config = ZXNoter.config.get(ActivityBarConfig.class);

    public ShowActivityPane(ActivityBarPane activityBarPane, ShowActivityPanePosition pos) {
        this.activityBarPane = activityBarPane;
        this.pos = pos;

        getStyleClass().addAll("activity-pane", pos.name());
        isResize.addListener((observable, oldValue, newValue) -> {
            if (newValue)
                getStyleClass().addAll("resize");
            else
                getStyleClass().remove("resize");
        });
        setOnMouseMoved(event -> {
            isResize.set(switch (pos) {
                case left -> event.getX() > getWidth() - 4 && event.getX() < getWidth();
                case right -> event.getX() > 0 && event.getX() < 4;
                case bottom -> event.getY() > 0 && event.getY() < 4;
            });
        });
        setOnMouseExited(event -> {
            if (!event.isPrimaryButtonDown()) {
                isResize.set(false);
            }
        });
        setOnMouseReleased(event -> {
            getOnMouseMoved().handle(event);
        });


        Stage stage = activityBarPane.projectView.getStage();
        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(this::refreshLimitSize);
        });
        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(this::refreshLimitSize);
        });

        setOnMouseDragged(event -> {
            if (isResize.get()) {
                /*if (event.getX() < getBoundsInParent().getWidth() / 2) {
                    //activityBarPane.borderPane.setLeft(null);
                } else */
                {
                    refreshLimitSize();
                    switch (pos) {
                        case left -> {
                            config.leftPaneWidth = Math.min(event.getX(), getMaxWidth());
                            setPrefWidth(config.leftPaneWidth);
                        }
                        case right -> {
                            config.rightPaneWidth = Math.min(getWidth() - event.getX(), getMaxWidth());
                            setPrefWidth(config.rightPaneWidth);
                        }
                        case bottom -> {
                            config.bottomPaneHeight = Math.min(getHeight() - event.getY(), getMaxHeight());
                            setPrefHeight(config.bottomPaneHeight);
                        }
                    }
                    config.needSave();
                }
            }
        });


        //refreshLimitSize();
    }

    /**
     * 获取配置尺寸
     *
     * @return 尺寸
     */
    public double getLimitSize() {
        return switch (pos) {
            case left -> config.leftPaneWidth;
            case right -> config.rightPaneWidth;
            case bottom -> config.bottomPaneHeight;
        };
    }

    /**
     * 设置显示尺寸
     *
     * @param size 尺寸
     */
    public void setSize(double size) {
        switch (pos) {
            case left, right -> setMaxWidth(size);
            case bottom -> setMaxHeight(size);
        }
    }

    /**
     * 获取当前所有活动项
     *
     * @return 所有活动项
     */
    public LinkedHashSet<String> getItems() {
        return switch (pos) {
            case left -> (config.mainBarIsLeft ? config.mainBarItems : config.secondBarItems);
            case right -> (!config.mainBarIsLeft ? config.mainBarItems : config.secondBarItems);
            case bottom -> config.bottomBarItems;
        };
    }

    void refreshLimitSize() {
        double limitW = activityBarPane.projectView.getTitleBar().getWidth();
        double limitH = activityBarPane.projectView.getStage().getHeight();
        switch (pos) {
            case left -> setMaxWidth((limitW - activityBarPane.rightActivityPane.getWidth()) * 0.4);
            case right -> setMaxWidth((limitW - activityBarPane.leftActivityPane.getWidth()) * 0.4);
            case bottom -> setMaxHeight(limitH * 0.6);
        }
    }

    BooleanProperty isResize = new SimpleBooleanProperty();

    /**
     * 设置是否显示
     *
     * @param isShow 是否显示
     */
    public void setShow(boolean isShow) {
        Node node = (isShow ? this : null);
        BorderPane borderPane = activityBarPane.borderPane;
        switch (pos) {
            case left -> borderPane.setLeft(node);
            case right -> borderPane.setRight(node);
            case bottom -> borderPane.setBottom(node);
        }
        if (node != null) {

            refreshLimitSize();
            switch (pos) {
                case left -> setPrefWidth(Math.min(config.leftPaneWidth, getMaxWidth()));
                case right -> setPrefWidth(Math.min(config.rightPaneWidth, getMaxWidth()));
                case bottom -> setPrefHeight(Math.min(config.bottomPaneHeight, getMaxHeight()));
            }
        }
    }

    /**
     * 显示活动面板
     *
     * @param activityPane 需要被显示的
     */
    public void showActivityPane(ActivityPane activityPane) {
        getChildren().clear();
        getChildren().add(activityPane);
    }
}
