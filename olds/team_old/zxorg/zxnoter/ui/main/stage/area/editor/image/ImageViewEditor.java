package team.zxorg.zxnoter.ui.main.stage.area.editor.image;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.ui.component.ZXIcon;
import team.zxorg.zxnoter.ui.component.ZXLabel;
import team.zxorg.zxnoter.ui.component.ZXStatus;
import team.zxorg.zxnoter.ui.main.stage.area.EditorArea;
import team.zxorg.zxnoter.ui.main.stage.area.editor.base.BaseEditor;
import team.zxorg.zxnoter.ui.main.stage.side.filemanager.FileItem;

import java.nio.file.Path;
import java.util.List;

public class ImageViewEditor extends BaseEditor {
    Image image;
    ImageView imageView = new ImageView();
    ScrollPane scrollPane = new ScrollPane(imageView);

    Label sizeLabel = new Label();
    ZXStatus sizeStatus = new ZXStatus(sizeLabel);

    Label zoomLabel = new Label();
    Slider zoomSlider = new Slider();
    ZXIcon zoomMinIcon = new ZXIcon("media.landscape", ZXColor.GRAY, 12);
    ZXIcon zoomMaxIcon = new ZXIcon("media.landscape", ZXColor.GRAY, 18);
    ZXStatus zoomStatus = new ZXStatus(zoomLabel, zoomMinIcon, zoomSlider, zoomMaxIcon);

    public ImageViewEditor(Path path, EditorArea editorArea) {
        super(path, editorArea);
        isEditable.set(false);//不可编辑

        zxStatuses.addAll(List.of(zoomStatus, sizeStatus));

        zoomSlider.setValue(0.8);
        zoomLabel.setPrefWidth(40);
        zoomSlider.setPrefWidth(80);
        zoomSlider.setMax(4.0);
        zoomSlider.setMin(0.4);
        zoomSlider.setOnMouseDragged(event -> updateImageSize());


        image = new Image(path.toUri().toString());
        imageView.setImage(image);

        body.setCenter(scrollPane);

        updateImageSize();

        sizeLabel.setText((int) image.getWidth() + "×" + (int) image.getHeight());

        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            updateImageSize();
        });
        scrollPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            updateImageSize();
        });


        imageView.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isAltDown()) {

                double newZoom = zoomSlider.getValue() + (0.004 * event.getDeltaY());
                // 限制缩放范围，确保不会过度缩放或过度放大
                double minZoom = 0.4;
                double maxZoom = 4.0;
                newZoom = Math.max(minZoom, Math.min(maxZoom, newZoom));

                // 根据缩放前后的比例调整滚动条位置，使缩放中心点保持在鼠标位置
                double oldImgWidth = imageView.getFitWidth(); // 缩放前图像宽度
                double oldImgHeight = imageView.getFitHeight(); // 缩放前图像高度
                double mouseX = event.getX(); // 获取鼠标在图像上的X坐标
                double mouseY = event.getY(); // 获取鼠标在图像上的Y坐标

                zoomSlider.setValue(newZoom);
                updateImageSize();

                double newImgWidth = imageView.getFitWidth(); // 缩放后图像宽度
                double newImgHeight = imageView.getFitHeight(); // 缩放后图像高度

                // 调整滚动条位置，保持缩放中心点
                double newHValue = scrollPane.getHvalue() + ((mouseX / oldImgWidth) - (mouseX / newImgWidth)) * 2;
                double newVValue = scrollPane.getVvalue() + ((mouseY / oldImgHeight) - (mouseY / newImgHeight)) * 2;
                scrollPane.setHvalue(newHValue);
                scrollPane.setVvalue(newVValue);

                event.consume();
            }
        });

    }

    private void updateImageSize() {
        double imageScale = image.getWidth() / image.getHeight();
        double viewScale = scrollPane.getWidth() / scrollPane.getHeight();
        double fitHeight;
        double fitWidth;
        if (viewScale > imageScale) {
            // 以高度为准适应视图
            fitHeight = scrollPane.getHeight() * zoomSlider.getValue();
            fitWidth = fitHeight * imageScale;
        } else {
            // 以宽度为准适应视图
            fitWidth = scrollPane.getWidth() * zoomSlider.getValue();
            fitHeight = fitWidth / imageScale;
        }

        //-2 为了防止滚动条出现
        imageView.setFitWidth(fitWidth - 2);
        imageView.setFitHeight(fitHeight - 2);

        if (fitWidth < scrollPane.getWidth() && fitHeight < scrollPane.getHeight()) {
            scrollPane.setPadding(new Insets((scrollPane.getHeight() - fitHeight) / 2,
                    0, 0, (scrollPane.getWidth() - fitWidth) / 2));
        } else if (fitWidth < scrollPane.getWidth()) {
            scrollPane.setPadding(new Insets(0,
                    0, 0, (scrollPane.getWidth() - fitWidth) / 2));
        } else if (fitHeight < scrollPane.getHeight()) {
            scrollPane.setPadding(new Insets((scrollPane.getHeight() - fitHeight) / 2,
                    0, 0, 0));
        } else {
            scrollPane.setPadding(new Insets(0));
        }
        zoomLabel.setText((int) (zoomSlider.getValue() * 100) + "%");

    }

    @Override
    public void saveFile() {
        throw new RuntimeException("保存失败");
    }

    @Override
    protected void closed() {

    }
}
