package team.zxorg.zxnoter.ui.main.stage.body.side.filemanager;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.resource.UserPreference;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.ui.component.*;
import team.zxorg.zxnoter.ui.main.stage.body.SideBar;
import team.zxorg.zxnoter.ui.main.stage.body.side.BaseSideBarTab;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.view.BaseFileView;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.view.CreateProjectView;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.view.FileListView;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.view.FileTreeView;

import java.nio.file.Path;

public class FileManagerTab extends BaseSideBarTab {
    VBox fileView = new VBox();
    public SimpleObjectProperty<Integer> showCountProperty = new SimpleObjectProperty<>(0);
    ZXLabel showCount = new ZXLabel("side-bar.file-manager.count", showCountProperty);

    public ObjectProperty<BaseFileView> fileViewProperty = new SimpleObjectProperty<>();
    public SideBar tabPane;

    public FileManagerTab(SideBar tabPane) {
        super("file-manager", "document.folder-4", tabPane);
        this.tabPane = tabPane;
        ZXTextFieldGroup searchGroup = new ZXTextFieldGroup("side-bar.file-manager.search", "document.file-search");

        ZXIconButton regularButton = new ZXIconButton();
        regularButton.setColor(ZXColor.FONT_USUALLY);
        regularButton.setIconKey("system.close");
        regularButton.setSize(22);
        //regularButton.setOnAction((event) -> textField.clear());

        searchGroup.getChildren().addAll(regularButton);
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


        tabPane.zxProject.projectPath.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                fileViewProperty.set(new CreateProjectView(this));
                showCountProperty.set(0);
            } else {
                setFileViewType(config.getIntValue("view"));
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
        setFileViewType(config.getIntValue("view"));
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
