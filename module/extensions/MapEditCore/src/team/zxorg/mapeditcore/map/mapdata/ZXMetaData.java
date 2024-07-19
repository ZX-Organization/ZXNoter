package team.zxorg.mapeditcore.map.mapdata;

public class ZXMetaData implements IBaseData{
    /**
     * 歌曲标题
     */
    String title;
    /**
     * 歌曲标题unicode(通常为罗马音)
     */
    String titleUnicode;
    /**
     * 艺术家
     */
    String artist;
    /**
     * 艺术家unicode(通常为罗马音)
     */
    String artistUnicode;
    /**
     * 图作者
     */
    String creator;
    /**
     * 难度名(版本名)
     */
    String mapVersion;
    /**
     * 歌曲信息与档案的来源
     */
    String source;
    /**
     * 空格分隔的 String数组,易于搜索的标签
     */
    String tags;
    String mscPath;

    public ZXMetaData() {
        setTitle("");
        setTitleUnicode("");
        setArtist("");
        setArtistUnicode("");
        setCreator("");
        setMapVersion("");
        setSource("");
        setTags("");
    }

    public ZXMetaData(String title, String titleUnicode, String artist, String artistUnicode, String creator, String mapVersion, String source, String tags) {
        this.title = title;
        this.titleUnicode = titleUnicode;
        this.artist = artist;
        this.artistUnicode = artistUnicode;
        this.creator = creator;
        this.mapVersion = mapVersion;
        this.source = source;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleUnicode() {
        return titleUnicode;
    }

    public void setTitleUnicode(String titleUnicode) {
        this.titleUnicode = titleUnicode;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtistUnicode() {
        return artistUnicode;
    }

    public void setArtistUnicode(String artistUnicode) {
        this.artistUnicode = artistUnicode;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getMapVersion() {
        return mapVersion;
    }

    @Override
    public String getMscPath() {
        return mscPath;
    }

    public void setMapVersion(String mapVersion) {
        this.mapVersion = mapVersion;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
