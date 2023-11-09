package team.zxorg.zxnoter.ui.main.stage.side.filemanager;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.SeparatorMenuItem;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.resource.ZXFileType;
import team.zxorg.zxnoter.ui.component.ZXIcon;
import team.zxorg.zxnoter.ui.component.ZXMenuItem;

public class FileMenu extends ContextMenu {
    public FileMenu() {
        getItems().add(new ZXMenuItem("side-bar.file-manager.menu.create-file", new ZXIcon("document.file-add",ZXColor.GREEN, 18)));
        getItems().add(new ZXMenuItem("side-bar.file-manager.menu.create-folder", new ZXIcon("document.folder-add", ZXFileType.directory.type.color, 18)));
        getItems().add(new SeparatorMenuItem());
        getItems().add(new ZXMenuItem("side-bar.file-manager.menu.cut", new ZXIcon("design.scissors", ZXColor.FONT_USUALLY, 18)));
        getItems().add(new ZXMenuItem("side-bar.file-manager.menu.copy", new ZXIcon("document.file-copy", ZXColor.FONT_USUALLY, 18)));
        getItems().add(new ZXMenuItem("side-bar.file-manager.menu.paste", new ZXIcon("document.clipboard", ZXColor.FONT_USUALLY, 18)));
        getItems().add(new ZXMenuItem("side-bar.file-manager.menu.rename", new ZXIcon("design.edit-box", ZXColor.FONT_USUALLY, 18)));
        getItems().add(new SeparatorMenuItem());
        getItems().add(new ZXMenuItem("side-bar.file-manager.menu.open", new ZXIcon("document.folder-open", ZXColor.FONT_USUALLY, 18)));
        getItems().add(new ZXMenuItem("side-bar.file-manager.menu.copy-path", new ZXIcon("editor.link", ZXColor.FONT_USUALLY, 18)));
        getItems().add(new ZXMenuItem("side-bar.file-manager.menu.search", new ZXIcon("system.search", ZXColor.FONT_USUALLY, 18)));
        getItems().add(new SeparatorMenuItem());
        getItems().add(new ZXMenuItem("side-bar.file-manager.menu.refresh", new ZXIcon("system.refresh", ZXColor.FONT_USUALLY, 18)));
        getItems().add(new SeparatorMenuItem());
        getItems().add(new ZXMenuItem("side-bar.file-manager.menu.delete", new ZXIcon("system.delete-bin", ZXColor.RED, 18)));

    }
}
