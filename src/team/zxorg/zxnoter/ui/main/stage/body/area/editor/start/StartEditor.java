package team.zxorg.zxnoter.ui.main.stage.body.area.editor.start;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.component.ZXLabel;
import team.zxorg.zxnoter.ui.component.ZXTextFieldGroup;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.BaseEditor;

public class StartEditor extends BaseEditor {

    public StartEditor() {
        icon.setColor(ZXColor.FONT_USUALLY);
        icon.setIconKey("software.logo");
        textProperty().bind(GlobalResources.getLanguageContent("editor.start-editor.tab-title"));


        ImageView logo = new ImageView(ZXResources.LOGO);
        logo.setFitWidth(160);
        logo.setFitHeight(160);

        ZXLabel titleLabel = new ZXLabel("editor.start-editor.title", ZXColor.FONT_USUALLY);
        titleLabel.setFont(Font.font(28));
        ZXLabel startLabel = new ZXLabel("editor.start-editor.fast-start", ZXColor.FONT_USUALLY);
        startLabel.setFont(Font.font(16));

        HBox tools = new HBox();
        tools.getChildren().add(new Button("打开之前项目"));
        tools.getChildren().add(new Button("创建新项目"));
        tools.getChildren().add(new Button("创建新谱面"));
        tools.setAlignment(Pos.CENTER);
        tools.setSpacing(6);
        VBox body = new VBox(logo, titleLabel, startLabel, tools);
        body.setSpacing(12);
        body.setAlignment(Pos.CENTER);
        setContent(body);


        setClosable(false);
    }
}
