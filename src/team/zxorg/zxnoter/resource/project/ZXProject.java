package team.zxorg.zxnoter.resource.project;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.DirectoryChooser;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXConfiguration;
import team.zxorg.zxnoter.ui.main.ZXStage;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.base.BaseFileEditor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * 项目类
 */
public class ZXProject {
    public ObjectProperty<Path> projectPath = new SimpleObjectProperty<>(Paths.get(""));//项目地址
    public ObjectProperty<ZXProjectInfo> projectInfo = new SimpleObjectProperty<>();//项目信息
    public ZXStage zxStage;

    public HashMap<Path, BaseFileEditor> fileEditorMap = new HashMap<>();

    public ZXProject(ZXStage zxStage) {
        this.zxStage = zxStage;
    }

    {
        projectPath.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                projectInfo.set(null);
            } else {
                Path zxpFile = newValue.resolve(newValue.getFileName() + ".zxp");
                if (Files.exists(zxpFile)) {
                    try (InputStream inputStream = new FileInputStream(zxpFile.toFile())) {
                        JSONObject zxProjectInfo = JSON.parseObject(inputStream);
                        if (zxProjectInfo != null) {
                            projectInfo.set(zxProjectInfo.toJavaObject(ZXProjectInfo.class));
                        } else {
                            ZXLogger.warning("项目配置文件内容为空 " + zxpFile);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    ZXLogger.warning("项目配置文件丢失 " + zxpFile);
                    projectInfo.set(new ZXProjectInfo());
                }
            }
        });

    }

    /**
     * 打开项目
     */
    public void openProject(Path path) {
        projectPath.set(path);
        ZXConfiguration.getLastTime().put("open-project", path.toString());
    }

    /**
     * 关闭项目
     */
    public void closeProject() {
        ZXLogger.info("关闭项目 " + projectPath.get());
        projectPath.set(null);
        ZXConfiguration.getLastTime().put("open-project", "");
        BaseFileEditor[] editors = new BaseFileEditor[fileEditorMap.values().size()];
        fileEditorMap.values().toArray(editors);
        for (BaseFileEditor editor : editors) {
            editor.close();
        }
    }


    /**
     * 打开项目
     */
    public void openProject() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(GlobalResources.getLanguageContent("project.open").getValue());
        {
            File lastFile = new File(ZXConfiguration.getLastTime().getString("open-project"));
            if (lastFile.exists())
                directoryChooser.setInitialDirectory(lastFile);
        }
        File openFile = directoryChooser.showDialog(zxStage);
        if (openFile != null) {
            openProject(openFile.toPath());
        }
    }

    public void openLastProject() {
        String path = ZXConfiguration.getLastTime().getString("open-project");
        if (!"".equals(path)) {
            ZXLogger.info("打开之前项目 " + path);
            File lastFile = new File(path);
            if (lastFile.exists())
                openProject(lastFile.toPath());
        }
    }


}
