package team.zxorg.zxnoter.map.mapInfos;

/**
 * osu词条及反本地化名,一些带有空格的词条反本地化名与本地化名一致
 */
public enum OsuInfo{
    osufileformat(ZXMInfo.osufileformat, "osu file format", "v14"),
    //general
    AudioFilename(ZXMInfo.AudioPath, "AudioFilename", ""),
    AudioLeadIn(ZXMInfo.AudioLeadIn, "AudioLeadIn", "0"),
    PreviewTime(ZXMInfo.PreviewTime, "PreviewTime", "-1"),
    Countdown(ZXMInfo.Countdown, "Countdown", "1"),
    SampleSet(ZXMInfo.SampleSet, "SampleSet", "Normal"),
    StackLeniency(ZXMInfo.StackLeniency, "StackLeniency", "0.7"),
    Mode(ZXMInfo.OsuMode, "Mode", "3"),
    LetterboxInBreaks(ZXMInfo.LetterboxInBreaks, "LetterboxInBreaks", "0"),
    UseSkinSprites(ZXMInfo.UseSkinSprites, "UseSkinSprites", "0"),
    OverlayPosition(ZXMInfo.OverlayPosition, "OverlayPosition", "NoChange"),
    SkinPreference(ZXMInfo.SkinPreference, "SkinPreference", ""),
    EpilepsyWarning(ZXMInfo.EpilepsyWarning, "EpilepsyWarning", "0"),
    CountdownOffset(ZXMInfo.CountdownOffset, "CountdownOffset", "0"),
    SpecialStyle(ZXMInfo.SpecialStyle, "SpecialStyle", "0"),
    WidescreenStoryboard(ZXMInfo.WidescreenStoryboard, "WidescreenStoryboard", "0"),
    SamplesMatchPlaybackRate(ZXMInfo.SamplesMatchPlaybackRate, "SamplesMatchPlaybackRate", "0"),
    //editor
    Bookmarks(ZXMInfo.Bookmarks, "Bookmarks", ""),
    DistanceSpacing(ZXMInfo.DistanceSpacing, "DistanceSpacing", "1"),
    BeatDivisor(ZXMInfo.BeatDivisor, "BeatDivisor", "12"),
    GridSize(ZXMInfo.GridSize, "GridSize", "4"),
    TimelineZoom(ZXMInfo.TimelineZoom, "TimelineZoom", "2.5"),
    //metadata
    Title(ZXMInfo.Title, "Title", ""),
    TitleUnicode(ZXMInfo.TitleUnicode, "TitleUnicode", ""),
    Artist(ZXMInfo.Artist, "Artist", ""),
    ArtistUnicode(ZXMInfo.ArtistUnicode, "ArtistUnicode", ""),
    Creator(ZXMInfo.MapCreator, "Creator", "zxn_"),
    Version(ZXMInfo.Version, "Version", "Insane"),
    Source(ZXMInfo.Source, "Source", ""),
    Tags(ZXMInfo.Tags, "Tags", ""),
    BeatmapID(ZXMInfo.OsuBeatmapID, "BeatmapID", "-1"),
    BeatmapSetID(ZXMInfo.OsuBeatmapSetID, "BeatmapSetID", "-1"),
    HPDrainRate(ZXMInfo.HPDrainRate, "HPDrainRate", "9"),
    CircleSize(ZXMInfo.KeyCount, "CircleSize", "4"),
    OverallDifficulty(ZXMInfo.Difficulty, "OverallDifficulty", "8.5"),
    ApproachRate(ZXMInfo.ApproachRate, "ApproachRate", "5"),
    SliderMultiplier(ZXMInfo.SliderMultiplier, "SliderMultiplier", "1.4"),
    SliderTickRate(ZXMInfo.SliderTickRate, "SliderTickRate", "1"),
    BackgroundandVideoevents(ZXMInfo.BackgroundandVideoevents, "Background and Videoevents", "0,0,\"bg.png\",0,0"),
    BreakPeriods(ZXMInfo.BreakPeriods, "Break Periods", ""),
    StoryboardLayer0Background(ZXMInfo.StoryboardLayer0Background, "Storyboard Layer 0 (Background)", ""),
    StoryboardLayer1Fail(ZXMInfo.StoryboardLayer1Fail, "Storyboard Layer 1 (Fail)", ""),
    StoryboardLayer2Pass(ZXMInfo.StoryboardLayer2Pass, "Storyboard Layer 2 (Pass)", ""),
    StoryboardLayer3Foreground(ZXMInfo.StoryboardLayer3Foreground, "Storyboard Layer 3 (Foreground)", ""),
    StoryboardLayer4Overlay(ZXMInfo.StoryboardLayer4Overlay, "Storyboard Layer 4 (Overlay)", ""),
    StoryboardSoundSamples(ZXMInfo.StoryboardSoundSamples, "Storyboard Sound Samples", ""),
    BackgroundColourTransformations(ZXMInfo.BackgroundColourTransformations, "Background Colour Transformations", ""),
    Bpm(ZXMInfo.Bpm, "", ""),
    BgPath(ZXMInfo.BgPath, "", "")
    ;
    private final ZXMInfo unLocalizedInfo;
    private final String originName;
    private final String defaultValue;

    OsuInfo(ZXMInfo unLocalizedInfo, String originName, String defaultValue) {
        this.unLocalizedInfo = unLocalizedInfo;
        this.originName = originName;
        this.defaultValue = defaultValue;
    }
    public ZXMInfo unLocalize() {
        return unLocalizedInfo;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getOriginName() {
        return originName;
    }
}
