package team.zxorg.zxnoter.resource;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.DirectoryChooser;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.ui.main.ZXStage;
import team.zxorg.zxnoter.ui.main.stage.area.editor.base.BaseEditor;

import java.io.File;
import java.nio.file.Path;

public class ZXProject {
    /**
     * 项目文件夹目录
     */
    public static ObjectProperty<Path> projectsPath = new SimpleObjectProperty<>(null);
    /**
     * 已打开的项目根目录
     */
    public ObjectProperty<Path> openedPath = new SimpleObjectProperty<>(null);
    ZXStage zxStage;

    public ZXProject(ZXStage zxStage) {
        this.zxStage = zxStage;
    }

    /**
     * 打开项目
     */
    public void openProject(Path path) {
        openedPath.set(path);
        //ZXConfiguration.getLastTime().put("open-project", path.toString());
    }

    /**
     * 关闭项目
     */
    public void closeProject() {
        ZXLogger.info("关闭项目 " + openedPath.get());
        openedPath.set(null);
        //ZXConfiguration.getLastTime().put("open-project", "");
        BaseEditor[] editors = new BaseEditor[zxStage.editorArea.editorMap.values().size()];
        zxStage.editorArea.editorMap.values().toArray(editors);
        for (BaseEditor editor : editors) {
            editor.close();
        }
    }

    /**
     * 打开项目
     */
    public void openProject() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(GlobalResources.getLanguageContent("project.open").getValue());
        /*{
            File lastFile = new File(ZXConfiguration.getLastTime().getString("open-project"));
            if (lastFile.exists())
                directoryChooser.setInitialDirectory(lastFile);
        }*/
        File openFile = directoryChooser.showDialog(zxStage);
        if (openFile != null) {
            openProject(openFile.toPath());
        }
    }
}
