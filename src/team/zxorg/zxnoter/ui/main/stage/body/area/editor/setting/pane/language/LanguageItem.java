package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.language;

import javafx.scene.control.Label;
import team.zxorg.zxnoter.resource.pack.LanguageResourcePack;

public class LanguageItem {
    public LanguageResourcePack resourcePack;

    @Override
    public String toString() {
        return resourcePack.getPack().getName() + "-" + resourcePack.getLanguageContent("language-name");
    }

    public LanguageItem(LanguageResourcePack resourcePack) {
        this.resourcePack = resourcePack;
    }
}
