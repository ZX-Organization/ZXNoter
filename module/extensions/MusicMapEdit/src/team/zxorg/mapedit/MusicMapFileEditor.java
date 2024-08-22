package team.zxorg.mapedit;

import javafx.geometry.Orientation;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import team.zxorg.mapedit.render.basis.RendererCanvas;
import team.zxorg.mapedit.render.object.MapRender;
import team.zxorg.mapeditcore.io.reader.ImdReader;
import team.zxorg.mapeditcore.io.reader.MapReader;
import team.zxorg.mapeditcore.io.reader.OsuReader;
import team.zxorg.mapeditcore.io.reader.ZXReader;
import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.zxnoter.ui.ProjectView;
import team.zxorg.zxnoter.ui.component.editor.BaseFileEditor;

import java.io.IOException;
import java.nio.file.Path;

public class MusicMapFileEditor extends BaseFileEditor {
    private ZXMap map;
    private final HBox leftBox;
    private final HBox rightBox;
    private final Slider progressSlider;

    private final RendererCanvas canvas;
    private final HBox canvasBox;
    private final HBox body;

    public MusicMapFileEditor(Path file, ProjectView view) {
        super(file, view);
        getStyleClass().add("music-map-editor");

        canvas = new RendererCanvas() {
            @Override
           protected long getTime() {
                return (long) (progressSlider.getValue() * 1000);
            }
        };
        HBox.setHgrow(canvas, Priority.ALWAYS);
        canvasBox = new HBox(canvas);


        leftBox = new HBox();
        progressSlider = new Slider();
        progressSlider.setOrientation(Orientation.VERTICAL);
        progressSlider.setMax(10);//暂时10s
        rightBox = new HBox(progressSlider);
        body = new HBox(leftBox, canvasBox, rightBox);
        setContent(body);

        canvasBox.setMinWidth(400);
        HBox.setHgrow(canvasBox, Priority.ALWAYS);

    }

    @Override
    public void create(Path path) {
        map = new ZXMap();
    }

    @Override
    public void open(Path path) {
        String extension = path.getFileName().toString();
        extension = extension.substring(extension.lastIndexOf(".") + 1);
        setText(path.getFileName().toString());
        MapReader reader = switch (extension) {
            case "zx" -> new ZXReader();
            case "imd" -> new ImdReader();
            case "osu" -> new OsuReader();
            default -> throw new IllegalStateException("不支持的谱面格式: " + path);
        };

        try {
            reader.readFile(path.toFile());
            map = reader.readMap();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("读取谱面时发生异常: " + path);
        }

        canvas.addRender("map", new MapRender(map));
        canvas.start();
    }

    @Override
    public void save() {

    }

    @Override
    public void close() {

    }
}
