package team.zxorg.zxnoter.ui.main.stage.body.area.editor;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.FileItem;

import java.io.OutputStream;
import java.nio.file.Path;

public abstract class BaseFileEditor extends BaseEditor {
    protected FileItem fileItem;//文件物品
    protected BooleanProperty isEdited = new SimpleBooleanProperty();//是否已编辑

    public BaseFileEditor(FileItem fileItem) {
        this.fileItem = fileItem;

        icon.setColor(fileItem.fileType.type.color);
        icon.setIconKey(fileItem.fileType.iconKey);
        isEdited.set(false);
        setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {

            }
        });
    }

    /**
     * 保存文件
     *
     * @return 是否成功
     */
    protected abstract boolean saveFile();

}
