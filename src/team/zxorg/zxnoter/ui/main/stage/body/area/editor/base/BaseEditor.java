package team.zxorg.zxnoter.ui.main.stage.body.area.editor.base;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.UserPreference;
import team.zxorg.zxnoter.ui.component.ZXLabel;
import team.zxorg.zxnoter.ui.main.stage.body.EditorArea;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.FileItem;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;

/**
 * 和文件绑定的编辑器
 */
public abstract class BaseEditor extends BaseTab {
    protected Path path;//编辑器路径
    protected BooleanProperty isEdited = new SimpleBooleanProperty();//是否编辑过
    protected BooleanProperty isEditable = new SimpleBooleanProperty();//是否能编辑

    public Path getPath() {
        return path;
    }


    public BaseEditor(Path path, EditorArea editorArea) {
        super(editorArea);
        this.path = path;
    }

    public BaseEditor(FileItem fileItem, EditorArea editorArea) {
        super(editorArea);
        path = fileItem.path;
        icon.setColor(fileItem.fileType.type.color);
        icon.setIconKey(fileItem.fileType.iconKey);
        isEdited.set(false);
        ObjectProperty<String> fileName = new SimpleObjectProperty<>();
        ZXLabel title = new ZXLabel("editor.title", fileName);
        textProperty().bind(title.textProperty());
        fileName.set(String.valueOf(fileItem.path.getFileName()));
        isEdited.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                title.setLangKey("editor.title.edited");
            } else {
                title.setLangKey("editor.title");
            }
        });
    }


    @Override
    protected boolean closeRequest() {
        ZXLogger.info("关闭编辑器 " + this);
        if (isEdited.get())//编辑过才能保存
            if (UserPreference.isCloseAutoSave.get()) {//自动保存
                save();
            } else {//弹出保存提示框
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.getDialogPane().getStylesheets().addAll(editorArea.getScene().getStylesheets());
                alert.setTitle(GlobalResources.getLanguageContent("alert.save-file.title").get() + "(" + path.getFileName() + ")");
                alert.setHeaderText(null);
                alert.contentTextProperty().bind(GlobalResources.getLanguageContent("alert.save-file.content"));

                // 设置按钮选项
                ButtonType saveButton = new ButtonType(GlobalResources.getLanguageContent("alert.save-file.save").get());
                ButtonType discardButton = new ButtonType(GlobalResources.getLanguageContent("alert.save-file.discard").get());
                ButtonType cancelButton = new ButtonType(GlobalResources.getLanguageContent("alert.save-file.cancel").get());

                // 将按钮选项添加到对话框中
                alert.getButtonTypes().setAll(saveButton, discardButton, cancelButton);
                // 显示对话框并等待用户响应
                ButtonType result = alert.showAndWait().orElse(cancelButton);

                // 根据用户的选择执行相应的操作
                if (result == saveButton) {
                    ZXLogger.info("保存文件");
                    save();
                } else if (result == discardButton) {
                    // 不保存文件的操作
                    ZXLogger.info("不保存文件");
                    //zxStage.fileEditorMap.remove(fileItem.path);
                    return true;
                } else {
                    // 取消操作
                    ZXLogger.info("取消操作");
                    return false;
                }
            }
        if (isEdited.get()) {//如果没保存成功，阻止关闭
            ZXLogger.info("保存失败");
            return false;
        } else {
            return true;
        }
    }


    public void save() {
        // 保存文件的操作
        try {
            saveFile();
            ZXLogger.info("保存文件成功");
            isEdited.set(false);
        } catch (RuntimeException e) {
            ZXLogger.warning("保存文件失败");

            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().getStylesheets().addAll(editorArea.getScene().getStylesheets());
            alert.setTitle(GlobalResources.getLanguageContent("alert.save-file.error.title").get() + "(" + path.getFileName() + ")");
            alert.setHeaderText(GlobalResources.getLanguageContent("alert.save-file.error.content").get());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            alert.setContentText(null);

            // 创建一个TextArea来显示堆栈信息
            TextArea textArea = new TextArea(sw.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            alert.getDialogPane().setExpandableContent(textArea);


            ButtonType discardButton = new ButtonType(GlobalResources.getLanguageContent("alert.save-file.error.discard").get());
            ButtonType cancelButton = new ButtonType(GlobalResources.getLanguageContent("alert.save-file.error.cancel").get());
            // 将按钮选项添加到对话框中
            alert.getButtonTypes().setAll(discardButton, cancelButton);
            // 显示对话框并等待用户响应
            ButtonType result = alert.showAndWait().orElse(cancelButton);
            if (result == discardButton) {
                isEdited.set(false);
            }
        }
    }

    /**
     * 保存文件
     *
     * @return 是否成功
     */
    protected abstract void saveFile();

}
