package team.zxorg.zxnoter.resource;

public enum ZXFileType {
    directory("", "document.folder", Type.directory),
    zxm("zxm", "software.file-zxm", Type.musicMap),
    osu("osu", "software.file-osu", Type.musicMap),
    imd("imd", "software.file-imd", Type.musicMap),
    mc("mc", "software.file-mc", Type.musicMap),
    mp3("mp3", "media.music-2", Type.audio),
    ogg("ogg", "media.music-2", Type.audio),
    wav("wav", "media.music-2", Type.audio),
    png("png", "media.image", Type.image),
    jpg("jpg", "media.image", Type.image),
    jpeg("jpeg", "media.image", Type.image),

    mp4("mp4", "media.video", Type.video),
    avi("avi", "media.video", Type.video),
    mkv("mkv", "media.video", Type.video),
    mov("mov", "media.video", Type.video),
    wmv("wmv", "media.video", Type.video),
    flv("flv", "media.video", Type.video),
    webm("webm", "media.video", Type.video),
    _3gp("3gp", "media.video", Type.video),
    m4v("m4v", "media.video", Type.video),
    mpg("mpg", "media.video", Type.video),

    txt("txt", "document.file-text", Type.text),
    zxp("zxp", "document.file-text", Type.text),
    csv("csv", "document.file-text", Type.text),
    xml("xml", "document.file-text", Type.text),
    json("json", "document.file-text", Type.text),
    md("md", "document.file-text", Type.text),
    log("log", "document.file-text", Type.text),
    ini("ini", "document.file-text", Type.text),
    cfg("cfg", "document.file-text", Type.text),

    zip("zip", "document.file-zip", Type.zip),
    rar("rar", "document.file-zip", Type.zip),
    _7z("7z", "document.file-zip", Type.zip),
    tar("tar", "document.file-zip", Type.zip),
    gz("gz", "document.file-zip", Type.zip),
    bz2("bz2", "document.file-zip", Type.zip),

    osz("osz", "document.file-zip", Type.zip),
    mcz("mcz", "document.file-zip", Type.zip),

    svg("svg", "media.image", Type.image),
    unknown("", "document.file-unknow",  Type.unknown);
    public final String nameLanguageKey;
    public final String extensionName;
    public final String iconKey;
    public final Type type;


    ZXFileType(String extensionName, String iconKey, Type type) {
        this.extensionName = extensionName;
        this.iconKey = iconKey;
        this.nameLanguageKey = "file-name." + extensionName;
        this.type = type;
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
