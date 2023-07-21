package team.zxorg.zxnoter.ui.component;

import team.zxorg.zxnoter.resource.ZXColor;

import java.nio.file.Files;
import java.nio.file.Path;

public enum ZXFileIcon {
    directory("", "document.folder", "file-name.directory", "file-type.directory", ZXColor.ORANGE),
    zxm("zxm", "software.file-zxm", "file-name.zxm", "file-type.game-map", ZXColor.YELLOW),
    osu("osu", "software.file-osu", "file-name.osu", "file-type.game-map", ZXColor.YELLOW),
    imd("imd", "software.file-imd", "file-name.imd", "file-type.game-map", ZXColor.YELLOW),
    mp3("mp3", "media.music-2", "file-name.mp3", "file-type.audio", ZXColor.GREEN),
    ogg("ogg", "media.music-2", "file-name.ogg", "file-type.audio", ZXColor.GREEN),
    wav("wav", "media.music-2", "file-name.wav", "file-type.audio", ZXColor.GREEN),
    unknown("", "document.file-unknow", "file-name.unknown", "file-type.unknown", ZXColor.GRAY);
    public final String nameLanguageKey;
    public final String typeLanguageKey;
    public final String extensionName;
    public final String iconKey;
    public final ZXColor color;

    ZXFileIcon(String extensionName, String iconKey, String nameLanguageKey, String typeLanguageKey, ZXColor color) {
        this.extensionName = extensionName;
        this.iconKey = iconKey;
        this.nameLanguageKey = nameLanguageKey;
        this.typeLanguageKey = typeLanguageKey;
        this.color = color;
    }


}
