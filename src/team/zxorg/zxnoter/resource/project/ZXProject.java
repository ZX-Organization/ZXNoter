package team.zxorg.zxnoter.resource.project;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 项目类
 */
public class ZXProject {
    public ObjectProperty<Path> projectPath = new SimpleObjectProperty<>(Paths.get(""));//项目地址

    public ObjectProperty<ZXProjectInfo> projectInfo = new SimpleObjectProperty<>();//项目信息

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
    }

    /**
     * 打开项目
     */
    public void openProject(Window ownerWindow) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(GlobalResources.getLanguageContent("project.open").getValue());
        {
            File lastFile = new File(ZXConfiguration.getLast().getString("open-project"));
            if (lastFile.exists())
                directoryChooser.setInitialDirectory(lastFile);
        }
        File openFile = directoryChooser.showDialog(ownerWindow);
        if (openFile != null) {
            ZXConfiguration.getLast().put("open-project", openFile.toString());
            openProject(openFile.toPath());
        }
    }
}
