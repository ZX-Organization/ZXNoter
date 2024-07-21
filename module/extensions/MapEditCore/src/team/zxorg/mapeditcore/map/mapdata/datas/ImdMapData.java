package team.zxorg.mapeditcore.map.mapdata.datas;

import team.zxorg.mapeditcore.map.mapdata.IBaseData;
import team.zxorg.mapeditcore.map.mapdata.ZXMapData;

/**
 * 垃圾，内容太少了
 */
public class ImdMapData implements IBaseData {
    /**
     * 谱面数据类型
     */
    String type;
    /**
     * 标题
     */
    String titleUnicode;
    /**
     * 谱面版本名
     */
    String mapVersion;
    /**
     * 音乐路径
     */
    String mscPath;
    /**
     * 谱面作者（读取外部imd大概率丢失）
     */
    String creator;
    /**
     * 表格行数
     */
    int tabRows;
    /**
     * 图长度
     */
    int mapLength;

    public ImdMapData() {
        this("unnamed","_zxn","",0,0);
    }

    public ImdMapData(String titleUnicode, String mapVersion, String mscPath, int tabRows, int mapLength) {
        this.titleUnicode = titleUnicode;
        this.mapVersion = mapVersion;
        this.mscPath = mscPath;
        this.tabRows = tabRows;
        this.mapLength = mapLength;
        type = "ImdMapData";
    }

    @Override
    public String getTitleUnicode() {
        return titleUnicode;
    }

    @Override
    public String getMapVersion() {
        return mapVersion;
    }

    @Override
    public String getMscPath() {
        return mscPath;
    }
    @Override
    public String getCreator() {
        return creator;
    }

    public int getTabRows() {
        return tabRows;
    }

    public int getMapLength() {
        return mapLength;
    }

    public void setTitleUnicode(String titleUnicode) {
        this.titleUnicode = titleUnicode;
    }

    public void setMapVersion(String mapVersion) {
        this.mapVersion = mapVersion;
    }

    public void setMscPath(String mscPath) {
        this.mscPath = mscPath;
    }

    public void setTabRows(int tabRows) {
        this.tabRows = tabRows;
    }

    public void setMapLength(int mapLength) {
        this.mapLength = mapLength;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public ZXMapData toZxMeta() {
        ZXMapData metaData = new ZXMapData();
        metaData.setTitleUnicode(titleUnicode);
        StringBuilder title = new StringBuilder();
        for (char c:titleUnicode.toCharArray()){
            if (Character.isLetterOrDigit(c)||Character.isWhitespace(c)||Character.isDefined(c))
                title.append(c);
        }
        metaData.setTitle(title.toString());
        metaData.setArtist("various artists");
        metaData.setArtistUnicode("various artists");
        metaData.setSource("rhythm master");
        metaData.setCreator(creator);
        metaData.setTags("rm,imd");
        metaData.setMscPath(mscPath);

        return metaData;
    }

    @Override
    public String toString() {
        return "ImdMapData{" +
                "titleUnicode='" + titleUnicode + '\'' +
                ", mapVersion='" + mapVersion + '\'' +
                ", mscPath='" + mscPath + '\'' +
                ", creator='" + creator + '\'' +
                ", tabRows=" + tabRows +
                ", mapLength=" + mapLength +
                '}';
    }
}
