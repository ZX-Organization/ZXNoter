package team.zxorg.zxnoter.ui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import team.zxorg.extensionloader.core.Configuration;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.core.Resource;
import team.zxorg.extensionloader.event.ResourceEventListener;
import team.zxorg.fxcl.component.flexview.FlexArea;
import team.zxorg.zxnoter.ui.component.StatusBar;
import team.zxorg.zxnoter.ui.component.activitypane.ActivityPane;
import team.zxorg.zxnoter.ui.component.editor.BaseFileEditor;
import team.zxorg.zxnoter.ui.component.titlebar.TitleBar;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.*;


/**
 * 项目视图窗口
 */
public class ProjectView {

    public static final ObservableList<String> globalStylesheets = FXCollections.observableArrayList();
    protected static final Map<String, String> fileOpenMethodMap = new HashMap<>();
    protected static final Map<String, List<String>> fileOpenFilterMap = new LinkedHashMap<>();
    private static final Map<String, Class<? extends BaseFileEditor>> fileEditorMap = new HashMap<>();
    private static final ProjectViewConfig config = ZXNoter.config.get(ProjectViewConfig.class);
    private static final ResourceEventListener resourceEventListener = new ResourceEventListener() {
        @Override
        public void onReload() {
            globalStylesheets.clear();
            for (Path file : Resource.listResourceFiles("style")) {
                globalStylesheets.addAll(Resource.getResourceToUrl(file).toString());
            }
        }
    };

    static {
        Resource.addEventListener(resourceEventListener);
        resourceEventListener.onReload();
    }

    public final FileChooser openFileChooser = new FileChooser();
    /**
     * 场景
     */
    public final Scene scene;
    /**
     * 标题栏
     */
    private final TitleBar titleBar;
    /**
     * 视图区域
     */
    private final FlexArea viewArea;
    /**
     * 活动栏面板
     */
    private final ActivityPane activityBar;
    /**
     * 状态栏
     */
    private final StatusBar statusBar;
    /**
     * 窗口
     */
    private final Stage stage = new Stage();
    /**
     * 根
     */
    private final VBox root;


    public ProjectView() {

        titleBar = new TitleBar(this);
        viewArea = new FlexArea() {
            {
                getStyleClass().addAll("view-area");
                /*createTabPane().addTab(new Tab());
                createSplitPane().createTabPane().addTab(new Tab());*/
            }
        };
        activityBar = new ActivityPane(this, viewArea);
        statusBar = new StatusBar(this);
        root = new VBox(titleBar, activityBar, statusBar);
        scene = new Scene(root);

        Bindings.bindContent(scene.getStylesheets(), globalStylesheets);
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setTitle("ZXNoter");


        stage.setOnCloseRequest(event -> {
            Configuration.save();
        });

        //初始化歌姬玩意

        // 添加文件过滤器
        for (var f : fileOpenFilterMap.entrySet()) {
            openFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(Language.get("common.file." + f.getKey() + ".name"), f.getValue()));
        }

    }

    /**
     * 注册文件编辑器
     *
     * @param id              编辑器id
     * @param fileEditorClass 文件编辑器类
     */
    public static void registerFileEditor(String id, Class<? extends BaseFileEditor> fileEditorClass) {
        BaseFileEditor fileEditor;
        try {
            fileEditor = fileEditorClass.getDeclaredConstructor(Path.class, ProjectView.class).newInstance(null, null);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            Logger.warning("注册失败: " + fileEditorClass.getSimpleName() + " " + e.getMessage());
            return;
        }
        fileEditorMap.put(id, fileEditorClass);
    }

    /**
     * 注册文件打开方式
     *
     * @param extensionName 文件扩展名
     * @param fileEditorId  文件编辑器id
     */
    public static void registerFileOpenMethod(String extensionName, String fileEditorId) {
        if (!fileEditorMap.containsKey(fileEditorId)) {
            Logger.warning("不存在的文件编辑器: " + extensionName + " -> " + fileEditorId);
            return;
        }
        if (fileOpenFilterMap.containsKey(extensionName)) {
            Logger.warning("注册了重复的文件打开方式: " + extensionName + " -> " + fileEditorId);
            return;
        }
        fileOpenFilterMap.computeIfAbsent("$all", (e) -> new ArrayList<>()).add("*." + extensionName);
        fileOpenFilterMap.computeIfAbsent(extensionName, (e) -> new ArrayList<>()).add("*." + extensionName);
        fileOpenMethodMap.put(extensionName, fileEditorId);
    }

    public Stage getStage() {
        return stage;
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    public void show() {
        stage.show();
    }

    /**
     * 打开文件
     *
     * @param file 文件
     */
    public void openFile(Path file) {
        Logger.info("打开文件: " + file);
        String extension = file.getFileName().toString();
        extension = extension.substring(extension.lastIndexOf(".") + 1);
        String editorId = fileOpenMethodMap.get(extension);
        if (editorId == null) {
            Logger.warning("打文件: " + file + " 时找不到可用的打开方式");
            return;
        }
        Class<? extends BaseFileEditor> fileEditorClass = fileEditorMap.get(editorId);

        BaseFileEditor fileEditor;
        try {
            fileEditor = fileEditorClass.getDeclaredConstructor(Path.class, ProjectView.class).newInstance(file, this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            Logger.warning("打开文件失败: " + fileEditorClass.getSimpleName() + " " + e.getMessage());
            return;
        }

        config.previousOpenedFile = file.toAbsolutePath().toString();
        config.recentlyOpenedFile.addFirst(config.previousOpenedFile);
        titleBar.updateRecent();
        config.needSave();
        viewArea.addTab(fileEditor);

    }

    /**
     * 打开文件选择器
     */
    public void openFileChooser() {
        if (config.previousOpenedFile != null) {
            File f = new File(config.previousOpenedFile);
            if (f.isFile())
                openFileChooser.setInitialDirectory(f.getParentFile());
        }

        File file = openFileChooser.showOpenDialog(stage.getOwner());
        if (file == null) {
            Logger.info("用户取消打开文件");
            return;
        }
        openFile(file.toPath());
    }

    /**
     * 打开项目目录
     *
     * @param projectPath 项目文件目录
     */
    public void openProject(Path projectPath) {

    }
}
