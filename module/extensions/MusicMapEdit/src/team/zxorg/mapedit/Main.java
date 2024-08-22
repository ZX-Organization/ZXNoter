package team.zxorg.mapedit;

import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.zxnoter.ui.ProjectView;

public class Main implements ExtensionEntrypoint {
    @Override
    public void onLoaded(Extension extension, ExtensionManager manager) {

    }

    @Override
    public void onInitialize(Extension extension, ExtensionManager manager) {
        Logger.info("注册&初始化谱面编辑器");
        ProjectView.registerFileEditor("musicMapFileEditor", MusicMapFileEditor.class);

        //注册文件打开方式
        ProjectView.registerFileOpenMethod("zx", "musicMapFileEditor");
        ProjectView.registerFileOpenMethod("osu", "musicMapFileEditor");
        ProjectView.registerFileOpenMethod("imd", "musicMapFileEditor");
    }

    @Override
    public void onAllInitialized(Extension extension, ExtensionManager manager) {
    }
}
