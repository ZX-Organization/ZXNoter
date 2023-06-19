package team.zxorg.zxnoter.map.mapInfos;

/**
 * osu词条及反本地化名,一些带有空格的词条反本地化名与本地化名一致
 */
public enum OsuInfo {
    osufileformat("osu file format", "v14"),
    AudioFilename("AudioPath", ""),
    AudioLeadIn("AudioLeadIn", "0"),
    PreviewTime("PreviewTime", "-1"),
    Countdown("Countdown", "1"),
    SampleSet("SampleSet", "Normal"),
    StackLeniency("StackLeniency", "0.7"),
    Mode("OsuMode", "3"),
    LetterboxInBreaks("LetterboxInBreaks", "0"),
    UseSkinSprites("UseSkinSprites", "0"),
    OverlayPosition("OverlayPosition", "NoChange"),
    SkinPreference("SkinPreference", ""),
    EpilepsyWarning("EpilepsyWarning", "0"),
    CountdownOffset("CountdownOffset", "0"),
    SpecialStyle("SpecialStyle", "0"),
    WidescreenStoryboard("WidescreenStoryboard", "0"),
    SamplesMatchPlaybackRate("SamplesMatchPlaybackRate", "0"),
    Bookmarks("Bookmarks", ""),
    DistanceSpacing("DistanceSpacing", "1"),
    BeatDivisor("BeatDivisor", "12"),
    GridSize("GridSize", "4"),
    TimelineZoom("TimelineZoom", "2.5"),
    Title("Title", ""),
    TitleUnicode("TitleUnicode", ""),
    Artist("Artist", ""),
    ArtistUnicode("ArtistUnicode", ""),
    Creator("MapCreator", "zxn_"),
    Version("Version", "Insane"),
    Source("AudioSource", ""),
    Tags("SearchingTags", ""),
    BeatmapID("OsuBeatmapID", "-1"),
    BeatmapSetID("OsuBeatmapSetID", "-1"),
    HPDrainRate("HPDrainRate", "9"),
    CircleSize("KeyCount", "4"),
    OverallDifficulty("Difficulty", "8.5"),
    ApproachRate("ApproachRate", "5"),
    SliderMultiplier("SliderMultiplier", "1.4"),
    SliderTickRate("SliderTickRate", "1"),
    BackgroundandVideoevents("Background and Video events", "0,0,\"bg.png\",0,0"),
    BreakPeriods("Break Periods", ""),
    StoryboardLayer0Background("Storyboard Layer 0 (Background)", ""),
    StoryboardLayer1Fail("Storyboard Layer 1 (Fail)", ""),
    StoryboardLayer2Pass("Storyboard Layer 2 (Pass)", ""),
    StoryboardLayer3Foreground("Storyboard Layer 3 (Foreground)", ""),
    StoryboardLayer4Overlay("Storyboard Layer 4 (Overlay)", ""),
    StoryboardSoundSamples("Storyboard Sound Samples", ""),
    BackgroundColourTransformations("Background Colour Transformations", ""),
    BaseBpm("BaseBpm", ""),
    BgPath("BgPath","")
    ;
    private final String unLocalizedName;
    private final String defaultValue;

    OsuInfo(String unLocalizedName, String defaultValue) {
        this.unLocalizedName = unLocalizedName;
        this.defaultValue = defaultValue;
    }
    public String unLocalize() {
        return unLocalizedName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
