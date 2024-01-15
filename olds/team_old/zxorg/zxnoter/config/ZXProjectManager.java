package team.zxorg.zxnoter.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.stage.DirectoryChooser;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.config.root.ProjectConfig;
import team.zxorg.zxnoter.config.sub.project.EditorLayoutCfg;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.ui.main.ZXStage;
import team.zxorg.zxnoter.ui.main.stage.area.EditorArea;
import team.zxorg.zxnoter.ui.main.stage.area.EditorLayout;
import team.zxorg.zxnoter.ui.main.stage.area.EditorTabPane;
import team.zxorg.zxnoter.ui.main.stage.area.editor.base.BaseEditor;
import team.zxorg.zxnoter.ui.main.stage.side.filemanager.FileItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ZXProjectManager {

    public static ProjectConfig projectConfig;
    /**
     * 已打开的项目根目录
     */
    public ObjectProperty<Path> openedPath = new SimpleObjectProperty<>(null);
    ZXStage zxStage;

    public ZXProjectManager(ZXStage zxStage) {
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
            openedPath.set(path);

            //恢复之前的编辑区
            restoreEditorsArea(projectConfig.editorArea, zxStage.editorArea);
            zxStage.editorArea.autoLayout();
            zxStage.editorArea.checkItems();

        } catch (IOException e) {
            ZXLogger.severe("载入项目配置异常");
            return;
        }

        //ZXConfiguration.getLastTime().put("open-project", path.toString());
    }

    /**
     * 恢复编辑区区域
     * @param editorLayoutCfg
     * @param editorLayout
     */
    private void restoreEditorsArea(EditorLayoutCfg editorLayoutCfg, EditorLayout editorLayout) {
        if (editorLayoutCfg.editors != null) {
            ZXLogger.info("构建TabPane " + editorLayoutCfg.editors);
            EditorTabPane editorTabPane = new EditorTabPane(zxStage.editorArea, editorLayout);
            editorLayout.getItems().add(editorTabPane);
            for (String path : editorLayoutCfg.editors) {
                Path filePath = openedPath.get().resolve(path);
                if (!Files.exists(filePath)) {
                    ZXLogger.warning("文件不存在: " + filePath);
                }
                BaseEditor editor;
                FileItem openFile = new FileItem(filePath, zxStage);
                try {
                    Constructor<BaseEditor> constructor = openFile.fileType.editor.getDeclaredConstructor(Path.class, EditorArea.class);
                    editor = constructor.newInstance(openFile.path, zxStage.editorArea);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                //editorMap.put(openFile.path, editor);
                //focusEditorTabPane.get().createEditor(editor);
                editorTabPane.createEditor(editor);
            }
        } else if (editorLayoutCfg.subLayout != null) {
            editorLayout.getItems().clear();
            for (EditorLayoutCfg subLayout : editorLayoutCfg.subLayout) {
                ZXLogger.info("构建Layout " + subLayout);
                if (subLayout.editors != null) {
                    restoreEditorsArea(subLayout, editorLayout);
                } else if (subLayout.subLayout != null) {
                    EditorLayout newEditorLayout = new EditorLayout(editorLayout);
                    newEditorLayout.setOrientation(("horizontal".equals(subLayout.orientation) ? Orientation.HORIZONTAL : Orientation.VERTICAL));
                    editorLayout.getItems().add(newEditorLayout);
                    restoreEditorsArea(subLayout, newEditorLayout);
                }
            }
        }

    }

    /**
     * 保存编辑器区域
     * @param editorLayoutCfg
     * @param editorLayout
     */
    private void buildEditorsArea(EditorLayoutCfg editorLayoutCfg, EditorLayout editorLayout) {
        editorLayoutCfg.orientation = (editorLayout.getOrientation().equals(Orientation.HORIZONTAL) ? "horizontal" : "vertical");
        editorLayoutCfg.subLayout = new ArrayList<>();
        for (Object o : editorLayout.getItems()) {
            EditorLayoutCfg subLayout = new EditorLayoutCfg();
            editorLayoutCfg.subLayout.add(subLayout);
            if (o instanceof EditorLayout subEditorLayout) {
                buildEditorsArea(subLayout, subEditorLayout);
            } else if (o instanceof EditorTabPane editorTabPane) {
                subLayout.editors = new ArrayList<>();
                for (Object tab : editorTabPane.getTabs()) {
                    if (tab instanceof BaseEditor baseEditor) {
                        subLayout.editors.add(baseEditor.getPath().toString());
                    }
                }
            }
        }
    }


    /**
     * 关闭项目
     */
    public void closeProject() {
        ZXLogger.info("关闭项目 " + openedPath.get());

        //保存编辑器区布局
        projectConfig.editorArea = new EditorLayoutCfg();
        buildEditorsArea(projectConfig.editorArea, zxStage.editorArea);


        //ZXConfiguration.getLastTime().put("open-project", "");
        BaseEditor[] editors = new BaseEditor[zxStage.editorArea.editorMap.values().size()];
        zxStage.editorArea.editorMap.values().toArray(editors);
        for (BaseEditor editor : editors) {
            editor.close();
        }
        try (OutputStream outputStream = Files.newOutputStream(openedPath.get().resolve("reference.zxp"))) {
            ZXLogger.info("保存项目配置");
            JSON.writeTo(outputStream, projectConfig, JSONWriter.Feature.PrettyFormat);
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
