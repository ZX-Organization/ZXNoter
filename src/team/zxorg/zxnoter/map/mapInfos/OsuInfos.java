package team.zxorg.zxnoter.map.mapInfos;

/**
 * osu词条及反本地化名
 */
public enum OsuInfos{
    osuFileVersion("osuFileVersion"),
    AudioFilename("AudioPath"),
    AudioLeadIn("AudioLeadIn"),
    PreviewTime("PreviewTime"),
    Countdown("Countdown"),
    SampleSet("SampleSet"),
    StackLeniency("StackLeniency"),
    Mode("OsuMode"),
    LetterboxInBreaks("LetterboxInBreaks"),
    EpilepsyWarning("EpilepsyWarning"),
    SpecialStyle("SpecialStyle"),
    WidescreenStoryboard("WidescreenStoryboard"),
    DistanceSpacing("DistanceSpacing"),
    BeatDivisor("BeatDivisor"),
    GridSize("GridSize"),
    TimelineZoom("TimelineZoom"),
    Title("Title"),
    TitleUnicode("TitleUnicode"),
    Artist("Artist"),
    ArtistUnicode("ArtistUnicode"),
    Creator("MapCreator"),
    Version("Version"),
    Source("AudioSource"),
    Tags("SearchingTags"),
    BeatmapID("OsuBeatmapID"),
    BeatmapSetID("OsuBeatmapSetID"),
    HPDrainRate("HPDrainRate"),
    CircleSize("CircleSize"),
    OverallDifficulty("Difficulty"),
    ApproachRate("ApproachRate"),
    SliderMultiplier("SliderMultiplier"),
    SliderTickRate("SliderTickRate"),
    BackgroundandVideoevents("Background and Video events"),
    BreakPeriods("BreakPeriods"),
    StoryboardLayer0Background("Storyboard Layer0 (Background)"),
    StoryboardLayer1Fail("StoryboardLayer1Fail"),
    StoryboardLayer2Pass("Storyboard Layer2 (Pass)"),
    StoryboardLayer3Foreground("Storyboard Layer3 (Foreground)"),
    StoryboardSoundSamples("Storyboard Sound Samples");
    private final String unLocalizedName;

    OsuInfos(String unLocalizedName) {
        this.unLocalizedName = unLocalizedName;
    }

    public String unLocalize() {
        return unLocalizedName;
    }
}
