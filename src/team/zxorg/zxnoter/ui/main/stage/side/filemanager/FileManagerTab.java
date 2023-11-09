package team.zxorg.zxnoter.ui.main.stage.side.filemanager;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.ui.component.*;
import team.zxorg.zxnoter.ui.main.ZXStage;
import team.zxorg.zxnoter.ui.main.stage.side.BaseSideBarTab;
import team.zxorg.zxnoter.ui.main.stage.side.filemanager.view.BaseFileView;
import team.zxorg.zxnoter.ui.main.stage.side.filemanager.view.CreateProjectView;
import team.zxorg.zxnoter.ui.main.stage.side.filemanager.view.FileListView;
import team.zxorg.zxnoter.ui.main.stage.side.filemanager.view.FileTreeView;

public class FileManagerTab extends BaseSideBarTab {
    VBox fileView = new VBox();
    public SimpleObjectProperty<Integer> showCountProperty = new SimpleObjectProperty<>(0);
    ZXLabel showCount = new ZXLabel("side-bar.file-manager.count", showCountProperty);

    public ObjectProperty<BaseFileView> fileViewProperty = new SimpleObjectProperty<>();
    public ZXStage zxStage;

    public FileManagerTab(ZXStage zxStage) {
        super("file-manager", "document.folder-4", zxStage);
        this.zxStage = zxStage;
        ZXTextFieldGroup searchGroup = new ZXTextFieldGroup("side-bar.file-manager.search", "document.file-search");

        ZXIconButton regularButton = new ZXIconButton();
        regularButton.setColor(ZXColor.FONT_USUALLY);
        regularButton.setIconKey("system.close");
        regularButton.setSize(22);
        //regularButton.setOnAction((event) -> textField.clear());

        //searchGroup.getChildren().addAll(regularButton);
        VBox.setVgrow(fileView, Priority.ALWAYS);

        ZXIconButton viewButton = new ZXIconButton("editor.node-tree", 16, "side-bar.file-manager.view");
        viewButton.setOnAction(event -> {
            setFileViewType(-1);
        });
        /*ZXIconButton createFileButton = new ZXIconButton("document.file-add", 16, "side-bar.file-manager.create-file");
        CreateFilePopupBox createFilePopupBox = new CreateFilePopupBox(createFileButton, false, TrackTooltip.BindAttributes.CLICK_POP_UP, TrackTooltip.BindAttributes.LOSE_FOCUS_DISAPPEAR);

        ZXIconButton createFolderButton = new ZXIconButton("document.folder-add", 16, "side-bar.file-manager.create-folder");
        CreateFilePopupBox createFolderPopupBox = new CreateFilePopupBox(createFolderButton, true, TrackTooltip.BindAttributes.CLICK_POP_UP, TrackTooltip.BindAttributes.LOSE_FOCUS_DISAPPEAR);*/

        HBox.setHgrow(showCount, Priority.ALWAYS);
        showCount.setMaxWidth(Double.MAX_VALUE);
        HBox control = new HBox(showCount, viewButton);
        control.setAlignment(Pos.CENTER_LEFT);
        control.setPadding(new Insets(0, 4, 0, 4));
        control.setSpacing(4);

        body.setSpacing(6);
        body.getChildren().addAll(searchGroup, fileView, control);
        body.setPadding(new Insets(8));


        zxProject.openedPath.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                fileViewProperty.set(new CreateProjectView(this));
                showCountProperty.set(0);
            } else {

                setFileViewType(cfg.fileManager.fileViewType.equals("tree") ? 1 : 0);
            }
        });


        fileViewProperty.addListener((observable, oldValue, newValue) -> {
            viewButton.setVisible(true);
            if (newValue instanceof FileTreeView) {
                viewButton.setIconKey("editor.node-tree");
            } else if (newValue instanceof FileListView) {
                viewButton.setIconKey("editor.list-check");
            } else if (newValue instanceof CreateProjectView) {
                viewButton.setVisible(false);
            }
            fileView.getChildren().clear();
            newValue.refresh();
            fileView.getChildren().add(newValue);
        });
        setFileViewType(cfg.fileManager.fileViewType.equals("tree") ? 1 : 0);
    }


    public void setFileViewType(int fileViewType) {
        BaseFileView newFileView = fileViewProperty.get();
        if (fileViewType == -1) {
            if (newFileView instanceof FileTreeView) {
                newFileView = new FileListView(this);
            } else if (newFileView instanceof FileListView) {
                newFileView = new FileTreeView(this);
            }
        } else {
            newFileView = switch (fileViewType) {
                case 0 -> new FileTreeView(this);
                case 1 -> new FileListView(this);
                default -> null;
            };
        }
        fileViewProperty.set(newFileView);
    }


}
