package team.zxorg.zxnoter.ui.component;

import javafx.geometry.Pos;
import javafx.scene.Node;
import team.zxorg.zxnoter.resource.ZXColor;

public class CreateFilePopupBox extends TrackPopupVHBox {

    public CreateFilePopupBox(Node bindNode, boolean isFolder, BindAttributes... attributes) {
        super(bindNode, Pos.TOP_CENTER, 0, attributes);
        ZXTextFieldGroup createFileTextField = new ZXTextFieldGroup("side-bar.file-manager.create-file.input", ZXFileIcon.unknown.iconKey);
        createFileTextField.setFocusTraversable(false);
        if (isFolder) {
            createFileTextField.textField.setPromptTextKey("side-bar.file-manager.create-folder.input");
            createFileTextField.icon.color.set(ZXFileIcon.directory.color);
            createFileTextField.icon.iconKey.set(ZXFileIcon.directory.iconKey);
        } else {
            createFileTextField.textField.setPromptTextKey("side-bar.file-manager.create-file.input");
            createFileTextField.icon.color.set(ZXFileIcon.unknown.color);
            createFileTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                ZXFileIcon zxFileIcon = ZXIcon.getFileIcon(newValue);
                createFileTextField.icon.iconKey.set(zxFileIcon.iconKey);
                createFileTextField.icon.color.set(zxFileIcon.color);
            });
        }

        ZXIconButton createFileIconButton = new ZXIconButton("system.check", 22);
        createFileIconButton.setColor(ZXColor.GREEN);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(8);
        getChildren().addAll(createFileTextField, createFileIconButton);
    }
}
