package team.zxorg.zxnoter.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.DirectoryChooser;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.config.project.ProjectConfig;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.ui.main.ZXStage;
import team.zxorg.zxnoter.ui.main.stage.area.editor.base.BaseEditor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ZXProject {

    public static ProjectConfig projectConfig;
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
        ZXLogger.info("打开项目: " + path);
        try (InputStream projectConfigInputStream = Files.newInputStream(path.resolve("reference.zxp"))) {
            ZXLogger.info("载入项目配置");
            JSONObject root = JSON.parseObject(projectConfigInputStream);
            projectConfig = root.toJavaObject(ProjectConfig.class);
        } catch (IOException e) {
            ZXLogger.severe("载入项目配置");
            throw new RuntimeException(e);
        }
        openedPath.set(path);
        //ZXConfiguration.getLastTime().put("open-project", path.toString());
    }

    /**
     * 关闭项目
     */
    public void closeProject() {
        ZXLogger.info("关闭项目 " + openedPath.get());
        //ZXConfiguration.getLastTime().put("open-project", "");
        BaseEditor[] editors = new BaseEditor[zxStage.editorArea.editorMap.values().size()];
        zxStage.editorArea.editorMap.values().toArray(editors);
        for (BaseEditor editor : editors) {
            editor.close();
        }

        try (OutputStream outputStream = Files.newOutputStream(openedPath.get().resolve("reference.zxp"))) {
            ZXLogger.info("保存项目配置");
            JSON.writeTo(outputStream, projectConfig);
        } catch (IOException e) {
            ZXLogger.severe("保存项目配置失败");
            throw new RuntimeException(e);
        }
        openedPath.set(null);

    }

    /**
     * 打开项目 通过文件选择器
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
