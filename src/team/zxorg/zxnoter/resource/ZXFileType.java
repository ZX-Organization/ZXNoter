package team.zxorg.zxnoter.resource;

import team.zxorg.zxnoter.ui.main.stage.body.area.editor.image.ImageViewEditor;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.text.TextEditor;

public enum ZXFileType {
    directory("", "document.folder", Type.directory, null),

    zxm("zxm", "software.file-zxm", Type.musicMap, null),
    osu("osu", "software.file-osu", Type.musicMap, null),
    imd("imd", "software.file-imd", Type.musicMap, null),
    mc("mc", "software.file-mc", Type.musicMap, null),

    mp3("mp3", "media.music-2", Type.audio, null),
    ogg("ogg", "media.music-2", Type.audio, null),
    wav("wav", "media.music-2", Type.audio, null),
    flac("flac", "media.music-2", Type.audio, null),

    png("png", "media.image", Type.image, ImageViewEditor.class),
    jpg("jpg", "media.image", Type.image, ImageViewEditor.class),
    jpeg("jpeg", "media.image", Type.image, ImageViewEditor.class),
    svg("svg", "media.image", Type.image, null),

    mp4("mp4", "media.video", Type.video, null),
    avi("avi", "media.video", Type.video, null),
    mkv("mkv", "media.video", Type.video, null),
    mov("mov", "media.video", Type.video, null),
    wmv("wmv", "media.video", Type.video, null),
    flv("flv", "media.video", Type.video, null),
    webm("webm", "media.video", Type.video, null),
    _3gp("3gp", "media.video", Type.video, null),
    m4v("m4v", "media.video", Type.video, null),
    mpg("mpg", "media.video", Type.video, null),

    zxp("zxp", "document.file-text", Type.text, TextEditor.class),

    txt("txt", "document.file-text", Type.text, TextEditor.class),
    csv("csv", "document.file-text", Type.text, TextEditor.class),
    xml("xml", "document.file-text", Type.text, TextEditor.class),
    json("json", "document.file-text", Type.text, TextEditor.class),
    md("md", "document.file-text", Type.text, TextEditor.class),
    log("log", "document.file-text", Type.text, TextEditor.class),
    ini("ini", "document.file-text", Type.text, TextEditor.class),
    cfg("cfg", "document.file-text", Type.text, TextEditor.class),

    zip("zip", "document.file-zip", Type.zip, null),
    rar("rar", "document.file-zip", Type.zip, null),
    _7z("7z", "document.file-zip", Type.zip, null),
    tar("tar", "document.file-zip", Type.zip, null),
    gz("gz", "document.file-zip", Type.zip, null),
    bz2("bz2", "document.file-zip", Type.zip, null),

    osz("osz", "document.file-zip", Type.zip, null),
    mcz("mcz", "document.file-zip", Type.zip, null),

    unknown("", "document.file-unknow", Type.unknown, null);
    public final String nameLanguageKey;
    public final String extensionName;
    public final String iconKey;
    public final Type type;
    public final Class editor;


    ZXFileType(String extensionName, String iconKey, Type type, Class editor) {
        this.extensionName = extensionName;
        this.iconKey = iconKey;
        this.nameLanguageKey = "file-name." + extensionName;
        this.type = type;
        this.editor = editor;
    }

    public enum Type {
        directory("file-type.directory", ZXColor.ORANGE),
        musicMap("file-type.game-map", ZXColor.YELLOW),
        zip("file-type.zip", ZXColor.PURPLE),
        audio("file-type.audio", ZXColor.GREEN),
        image("file-type.image", ZXColor.BLUE),
        video("file-type.video", ZXColor.PINK),
        text("file-type.text", ZXColor.WHITE),
        unknown("file-type.unknown", ZXColor.GRAY);
        public final String typeLanguageKey;
        public final ZXColor color;

        Type(String typeLanguageKey, ZXColor color) {
            this.typeLanguageKey = typeLanguageKey;
            this.color = color;
        }
    }


}
