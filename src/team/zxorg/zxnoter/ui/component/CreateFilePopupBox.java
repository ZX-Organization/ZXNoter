package team.zxorg.zxnoter.ui.component;

import javafx.geometry.Pos;
import javafx.scene.Node;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.resource.ZXFileType;

public class CreateFilePopupBox extends TrackPopupVHBox {

    public CreateFilePopupBox(Node bindNode, boolean isFolder, BindAttributes... attributes) {
        super(bindNode, Pos.TOP_CENTER, 0, attributes);
        ZXTextFieldGroup createFileTextField = new ZXTextFieldGroup("side-bar.file-manager.create-file.input", ZXFileType.unknown.iconKey);
        createFileTextField.setFocusTraversable(false);
        if (isFolder) {
            createFileTextField.textField.setPromptTextKey("side-bar.file-manager.create-folder.input");
            createFileTextField.icon.color.set(ZXFileType.directory.type.color);
            createFileTextField.icon.iconKey.set(ZXFileType.directory.iconKey);
        } else {
            createFileTextField.textField.setPromptTextKey("side-bar.file-manager.create-file.input");
            createFileTextField.icon.color.set(ZXFileType.unknown.type.color);
            createFileTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                ZXFileType zxFileType = ZXIcon.getFileIcon(newValue);
                createFileTextField.icon.iconKey.set(zxFileType.iconKey);
                createFileTextField.icon.color.set(zxFileType.type.color);
            });
        }

        ZXIconButton createFileIconButton = new ZXIconButton("system.check", 22);
        createFileIconButton.setColor(ZXColor.GREEN);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(8);
        getChildren().addAll(createFileTextField, createFileIconButton);
    }
}
