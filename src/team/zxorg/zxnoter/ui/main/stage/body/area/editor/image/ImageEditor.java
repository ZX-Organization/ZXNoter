package team.zxorg.zxnoter.ui.main.stage.body.area.editor.image;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.BaseEditor;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.BaseFileEditor;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.FileItem;

import java.io.OutputStream;
import java.nio.file.Path;

public class ImageEditor extends BaseFileEditor {
    public ImageEditor(FileItem fileItem) {
        super(fileItem);
        setText(String.valueOf(fileItem.path.getFileName()));
        ImageView imageView = new ImageView(new Image(fileItem.path.toUri().toString()));
        setContent(imageView);

    }

    @Override
    public boolean saveFile() {
        return false;
    }
}
