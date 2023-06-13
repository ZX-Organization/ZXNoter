package team.zxorg.zxnoter.map.mapInfos;

/**
 * osu词条及反本地化名,一些带有空格的词条反本地化名与本地化名一致
 */
public enum OsuInfos{
    osufileformat("osu file format"),
    AudioFilename("AudioPath"),
    AudioLeadIn("AudioLeadIn"),
    PreviewTime("PreviewTime"),
    Countdown("Countdown"),
    SampleSet("SampleSet"),
    StackLeniency("StackLeniency"),
    Mode("OsuMode"),
    LetterboxInBreaks("LetterboxInBreaks"),
    UseSkinSprites("UseSkinSprites"),
    OverlayPosition("OverlayPosition"),
    SkinPreference("SkinPreference"),
    EpilepsyWarning("EpilepsyWarning"),
    CountdownOffset("CountdownOffset"),
    SpecialStyle("SpecialStyle"),
    WidescreenStoryboard("WidescreenStoryboard"),
    SamplesMatchPlaybackRate("SamplesMatchPlaybackRate"),
    Bookmarks("Bookmarks"),
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
    BreakPeriods("Break Periods"),
    StoryboardLayer0Background("Storyboard Layer 0 (Background)"),
    StoryboardLayer1Fail("Storyboard Layer 1 (Fail)"),
    StoryboardLayer2Pass("Storyboard Layer 2 (Pass)"),
    StoryboardLayer3Foreground("Storyboard Layer 3 (Foreground)"),
    StoryboardSoundSamples("Storyboard Sound Samples"),
    BaseBpm("BaseBpm"),
    ;
    private final String unLocalizedName;


    OsuInfos(String unLocalizedName) {
        this.unLocalizedName = unLocalizedName;
    }
    public String unLocalize() {
        return unLocalizedName;
    }
}
