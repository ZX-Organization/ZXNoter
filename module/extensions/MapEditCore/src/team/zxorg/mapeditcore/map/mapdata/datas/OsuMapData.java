package team.zxorg.mapeditcore.map.mapdata.datas;

import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.map.mapdata.ZXMapData;

import java.util.LinkedHashMap;

public class OsuMapData extends ZXMapData {
    /**
     * 常规信息
     */
    private final LinkedHashMap<String, String> generalInfo;
    /**
     * 谱面id
     */
    int beatMapId;
    /**
     * 谱面组id
     */
    int beatMapSetId;
    /**
     * 编辑器信息
     */
    private final LinkedHashMap<String, String> editorInfo;
    /**
     * 难度信息
     */
    private final LinkedHashMap<String, String> difficultInfo;
    /**
     * 颜色信息
     */
    private final LinkedHashMap<String,String> colourInfo;
    public static String[] eventDefaultInfo = {
            "Background and Video events", "Break Periods", "Storyboard Layer 0 (Background)", "Storyboard Layer 1 (Fail)",
            "Storyboard Layer 2 (Pass)", "Storyboard Layer 3 (Foreground)", "Storyboard Sound Samples"
    };
    /**
     * 事件信息
     */
    private final LinkedHashMap<String, String> eventInfo;

    public OsuMapData() {
        //常规信息
        generalInfo = new LinkedHashMap<>();
        String[] generalDefaultInfo = {
                "AudioFilename", "AudioLeadIn", "PreviewTime", "Countdown", "SampleSet", "StackLeniency",
                "Mode", "LetterboxInBreaks", "EpilepsyWarning", "SpecialStyle", "WidescreenStoryboard",
        };
        for (String s : generalDefaultInfo) generalInfo.put(s, "");
        //谱面id
        beatMapId = -1;
        beatMapSetId = -1;
        //编辑器信息
        editorInfo = new LinkedHashMap<>();
        String[] editorDefaultInfo = {"DistanceSpacing", "BeatDivisor", "GridSize", "TimelineZoom"};
        for (String s : editorDefaultInfo) editorInfo.put(s, "");
        //难度信息
        difficultInfo = new LinkedHashMap<>();
        String[] difficultDefaultInfo = {"HPDrainRate", "CircleSize", "OverallDifficulty", "ApproachRate", "SliderMultiplier", "SliderTickRate"};
        for (String s : difficultDefaultInfo) difficultInfo.put(s, "");
        //颜色信息
        colourInfo= new LinkedHashMap<>();
        //一般为空

        //事件信息
        eventInfo = new LinkedHashMap<>();
        for (String s : eventDefaultInfo) eventInfo.put(s, "");
        type = "OsuMapData";
    }

    public OsuMapData(ZXMapData zxMapData , ZXMap map) {
        this();
        generalInfo.put("AudioFilename",zxMapData.getMscPath());
        generalInfo.put("AudioLeadIn"," 0");
        generalInfo.put("PreviewTime"," 0");
        generalInfo.put("Countdown"," 0");
        generalInfo.put("SampleSet"," Soft");
        generalInfo.put("StackLeniency"," 0.7");
        generalInfo.put("Mode"," 3");
        generalInfo.put("LetterboxInBreaks"," 0");
        generalInfo.put("EpilepsyWarning"," 1");
        generalInfo.put("SpecialStyle"," 0");
        generalInfo.put("WidescreenStoryboard"," 1");

        editorInfo.put("DistanceSpacing"," 1");
        editorInfo.put("BeatDivisor"," 8");
        editorInfo.put("GridSize"," 4");
        editorInfo.put("TimelineZoom"," 2.5");

        setTitle(zxMapData.getTitle());
        setTitleUnicode(zxMapData.getTitleUnicode());
        setArtist(zxMapData.getArtist());
        setArtistUnicode(zxMapData.getArtistUnicode());
        setCreator(zxMapData.getCreator());
        setMapVersion(zxMapData.getMapVersion());
        setSource(zxMapData.getSource());
        setTags(zxMapData.getTags());
        setBeatMapId(-1);
        setBeatMapSetId(-1);

        difficultInfo.put("HPDrainRate"," 8");
        difficultInfo.put("CircleSize", " "+ map.orbitCount);
        difficultInfo.put("OverallDifficulty", " 8");
        difficultInfo.put("ApproachRate", " 5");
        difficultInfo.put("SliderMultiplier", " 1.4");
        difficultInfo.put("SliderTickRate", " 1");

        eventInfo.put("Background and Video events","\"0,0," + zxMapData.getMscPath().replace(".mp3","")+".png" + ",0,0\"");

    }

    public int getBeatMapId() {
        return beatMapId;
    }

    public void setBeatMapId(int beatMapId) {
        this.beatMapId = beatMapId;
    }

    public int getBeatMapSetId() {
        return beatMapSetId;
    }

    public void setBeatMapSetId(int beatMapSetId) {
        this.beatMapSetId = beatMapSetId;
    }

    public LinkedHashMap<String, String> getGeneralInfo() {
        return generalInfo;
    }

    public LinkedHashMap<String, String> getEditorInfo() {
        return editorInfo;
    }

    public LinkedHashMap<String, String> getDifficultInfo() {
        return difficultInfo;
    }

    public LinkedHashMap<String, String> getColourInfo() {
        return colourInfo;
    }

    public LinkedHashMap<String, String> getEventInfo() {
        return eventInfo;
    }

    @Override
    public String toString() {
        return "OsuMapData{" +
                "title=" + getTitle() +
                ", titleUnicode=" + getTitleUnicode() +
                ", artist=" + getArtist() +
                ", artistUnicode=" + getArtistUnicode() +
                ", creator=" + getCreator() +
                ", mapVersion=" + getMapVersion() +
                ", source=" + getSource() +
                ", tags=" + getTags() +
                ", mscPath=" + getMscPath() +
                ", beatMapId=" + beatMapId +
                ", beatMapSetId=" + beatMapSetId +
                ", generalInfo=" + generalInfo +
                ", editorInfo=" + editorInfo +
                ", difficultInfo=" + difficultInfo +
                ", colourInfo=" + colourInfo +
                ", eventInfo=" + eventInfo +
                '}';
    }
}
