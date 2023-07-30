package team.zxorg.zxnoter.ui.main.stage.body.area.editor.image;

import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.BaseEditor;

import java.io.OutputStream;

public class ImageEditor extends BaseEditor {
    public ImageEditor() {
        icon.setColor(ZXColor.FONT_USUALLY);
        icon.setIconKey("media.image-edit");
    }

    @Override
    public boolean saveFile(OutputStream outputStream) {
        return false;
    }
}
