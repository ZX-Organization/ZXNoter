package team.zxorg.skin.uis.ui;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.PlainTextChange;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UISCodeArea extends CodeArea {
    private static final Pattern PATTERN = CodePattern.getPattern();
    final IntFunction<Node> numberFactory = LineNumberFactory.get(this);
    final IntFunction<Node> graphicFactory = line -> {
        Label node = (Label) numberFactory.apply(line);
        Label colorShow=new Label("■");
        colorShow.setPadding(new Insets(0,10,0,0));
        HBox hbox = new HBox(node,colorShow);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().addAll("left-bar");
        String lineText = getText(line);
        //尝试匹配颜色"	color=#4fffe1"如果有则修改字体颜色
        String colorRegex = "color=(#[0-9a-fA-F]{6})";
        Pattern colorPattern = Pattern.compile(colorRegex);
        Matcher matcher = colorPattern.matcher(lineText);
        colorShow.setVisible(false);
        if (matcher.find()) {
            colorShow.setVisible(true);
            String color = matcher.group(1);
            colorShow.setStyle("-fx-text-fill: " + color + ";");
        }
        return hbox;
    };
    private final ExecutorService executor;
    private final ExecutorService autoSaveExecutor = Executors.newSingleThreadExecutor();
    private final Path file;
    Runnable saved;

    {


        setBackground(Background.fill(Color.TRANSPARENT));
        setParagraphGraphicFactory(graphicFactory);

        getStyleClass().add("code-area");

        executor = Executors.newSingleThreadExecutor();
         multiPlainChanges().successionEnds(Duration.ofMillis(20)).retainLatestUntilLater(executor).supplyTask(this::computeHighlightingAsync).awaitLatest(multiPlainChanges()).filterMap(t -> {
            if (t.isSuccess()) {
                return Optional.of(t.get());
            } else {
                t.getFailure().printStackTrace();
                return Optional.empty();
            }
        }).subscribe(this::applyHighlighting);

        multiPlainChanges().successionEnds(Duration.ofMillis(200)).retainLatestUntilLater(autoSaveExecutor).subscribe(new Consumer<>() {
            @Override
            public void accept(List<PlainTextChange> plainTextChanges) {
                System.out.println("自动保存");
                try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                    writer.write(getText());
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                saved.run();


            }
        });


            /*textProperty().addListener((obs, oldText, newText) -> {
                codeArea.setStyleSpans(0, computeHighlighting(newText));
            });*/
    }






    public UISCodeArea(Path file, Runnable saved) throws IOException {
        super(Files.readString(file));
        this.file = file;
        this.saved = saved;


       applyHighlighting(computeHighlighting(getText()));


    }


    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass = CodePattern.getClassName(matcher);
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    public Path getFile() {
        return file;
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        setStyleSpans(0, highlighting);
    }

    private enum CodePattern {
        /**
         * 命令
         */
        COMMAND_PATTERN("^\\s*@[^\n]*", "command"),
        /**
         * 注释
         */
        COMMENT_PATTERN("^\\s*#[^\n]*", "comment"),
        /**
         * 功能组件
         */
        FUNCTIONAL_COMPONENT("^(bar|note|key|hit-fast|hit-slow|hit|press|judge|score-maxcombo|score-combo|score-score|score-acc|score-hp|bga|touch|progress|hp|musicbar|hit-fast|hit-slow|pause)\\b", "functionalComponent"),
        /**
         * 其他组件
         */
        OTHER_COMPONENT("_[^\n]*", "otherComponent"),
        ANIMATION_COMPONENT("^:[^\n]*", "animationComponent"),
        /**
         * 组件属性名
         */
        COMPONENT_PROPERTY("^\t([^\n=]+)(?==)", "componentProperty"),
        COMPONENT_PROPERTY_ERROR("^( {4})([^\n=]+)(?==)", "componentPropertyError"),
        PERCENTAGE("(\\d+(\\.\\d+)?%)", "percentage"),
        PIXEL("(\\d+(\\.\\d+)?px)", "pixel"),
        DOT("(,|\\$)", "dot"),
        SQUARE_BRACKET("(\\[|\\])", "squareBracket"),
        DELIMITER("(/)", "delimiter"),
        DELIMITER_ERROR("(\\\\)", "delimiterError"),

        ;
        private final String patternString;
        private final String className;

        CodePattern(String patternString, String className) {
            this.patternString = patternString;
            this.className = className;
        }

        public static Pattern getPattern() {
            StringBuilder sb = new StringBuilder();
            for (CodePattern pattern : CodePattern.values()) {
                sb.append("|(?<").append(pattern.className).append(">").append(pattern.patternString).append(")");
            }
            sb.deleteCharAt(0);
            return Pattern.compile(sb.toString(), Pattern.MULTILINE);
        }

        public static String getClassName(Matcher matcher) {
            for (CodePattern pattern : CodePattern.values()) {
                if (matcher.group(pattern.className) != null) return pattern.className;
            }
            return "";
        }
    }

}
