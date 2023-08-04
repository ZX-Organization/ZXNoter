package team.zxorg.zxnoter.ui.main.stage.body.area.editor.base;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.UserPreference;
import team.zxorg.zxnoter.resource.project.ZXProject;
import team.zxorg.zxnoter.ui.main.stage.body.area.EditorTabPane;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.FileItem;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class BaseFileEditor extends BaseTab {
    protected FileItem fileItem;//文件物品
    protected BooleanProperty isEdited = new SimpleBooleanProperty();//是否编辑过
    protected BooleanProperty isEditable = new SimpleBooleanProperty();//是否能编辑

    public FileItem getFileItem() {
        return fileItem;
    }

    public BaseFileEditor(FileItem fileItem, ZXProject zxProject) {
        super(zxProject);
        this.fileItem = fileItem;

        icon.setColor(fileItem.fileType.type.color);
        icon.setIconKey(fileItem.fileType.iconKey);
        isEdited.set(false);

        setText(String.valueOf(fileItem.path.getFileName()));
    }

    @Override
    protected boolean closeRequest() {
        ZXLogger.info("关闭编辑器 " + this);
        if (isEdited.get())//编辑过才能保存
            if (UserPreference.isCloseAutoSave.get()) {//自动保存
                save();
            } else {//弹出保存提示框
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.getDialogPane().getStylesheets().addAll(zxProject.zxStage.getScene().getStylesheets());
                alert.setTitle(GlobalResources.getLanguageContent("alert.save-file.title").get() + "(" + fileItem.path.getFileName() + ")");
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
                    zxProject.fileEditorMap.remove(fileItem.path);
                    return true;
                } else {
                    // 取消操作
                    ZXLogger.info("取消操作");
                    return false;
                }
            }
        if (isEdited.get()) {//如果没保存成功，阻止关闭
            return false;
        } else {
            closed();//关闭
            zxProject.fileEditorMap.remove(fileItem.path);
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
            alert.getDialogPane().getStylesheets().addAll(zxProject.zxStage.getScene().getStylesheets());
            alert.setTitle(GlobalResources.getLanguageContent("alert.save-file.error.title").get() + "(" + fileItem.path.getFileName() + ")");
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
