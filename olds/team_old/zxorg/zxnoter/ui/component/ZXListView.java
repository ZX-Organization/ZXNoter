package team.zxorg.zxnoter.ui.component;

import com.sun.javafx.scene.control.VirtualScrollBar;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.skin.ListViewSkin;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.layout.Region;

public class ZXListView<T> extends ListView<T> {
    DoubleProperty itemWidth=new SimpleDoubleProperty();
    BooleanProperty HScrollBarVisible;
    boolean loaded = false;

    public ZXListView() {
        skinProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof ListViewSkin<?> listViewSkin) {
                if (listViewSkin.getChildren().get(0) instanceof VirtualFlow<?> virtualFlow) {
                    for (Node node : virtualFlow.getChildrenUnmodifiable()) {
                        if (node instanceof VirtualScrollBar virtualScrollBar) {
                            if (virtualScrollBar.getOrientation() == Orientation.VERTICAL) {
                                HScrollBarVisible = virtualScrollBar.visibleProperty();
                                itemWidth.bind(widthProperty().subtract(virtualScrollBar.widthProperty()).subtract(16));
                                updateAll();
                                loaded = true;
                                return;
                            }
                        }
                    }
                }
            }
        });
        getItems().addListener((ListChangeListener<T>) c -> {
            if (loaded) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (T node : c.getAddedSubList()) {
                            if (node instanceof Region region) {
                                region.maxWidthProperty().bind(itemWidth);
                            }
                        }
                    }
                }
            }
        });
    }

    private void updateAll() {
        for (T node : getItems()) {
            if (node instanceof Region region) {
                region.maxWidthProperty().bind(itemWidth);
            }
        }
    }
}
