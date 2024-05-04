package team.zxorg.ui.main.stage.side.filemanager.view;

import javafx.geometry.Pos;
import javafx.scene.text.Font;
import team.zxorg.ui.main.stage.side.filemanager.FileManagerTab;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.ui.component.ZXButton;
import team.zxorg.ui.component.ZXLabel;

public class CreateProjectView extends BaseFileView {


    public CreateProjectView(FileManagerTab fileManagerTab) {
        super(fileManagerTab);

        ZXLabel titleLabel = new ZXLabel("side-bar.file-manager.not-open.title", ZXColor.FONT_USUALLY);
        titleLabel.setFont(Font.font(16));

        ZXButton openButton = new ZXButton("side-bar.file-manager.not-open.open");
        openButton.setOnAction((event -> {
            //zxProject.openProject();
        }));

        ZXButton createButton = new ZXButton("side-bar.file-manager.not-open.create");
        getChildren().addAll(titleLabel, openButton, createButton);
        setAlignment(Pos.CENTER);
        setSpacing(8);
    }

    @Override
    public void refresh() {

    }
}
