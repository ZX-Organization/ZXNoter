package team.zxorg.zxnoter.info.map;

/**
 * osu词条及反本地化名,一些带有空格的词条反本地化名与本地化名一致
 * @author xiang2333
 */
public enum OsuInfo{
    osufileformat(ZXMInfo.osufileformat, "osu file format"),
    //general
    AudioFilename(ZXMInfo.AudioPath, "AudioFilename"),
    AudioLeadIn(ZXMInfo.AudioLeadIn, "AudioLeadIn"),
    PreviewTime(ZXMInfo.PreviewTime, "PreviewTime"),
    Countdown(ZXMInfo.Countdown, "Countdown"),
    SampleSet(ZXMInfo.SampleSet, "SampleSet"),
    StackLeniency(ZXMInfo.StackLeniency, "StackLeniency"),
    Mode(ZXMInfo.OsuMode, "Mode"),
    LetterboxInBreaks(ZXMInfo.LetterboxInBreaks, "LetterboxInBreaks"),
    UseSkinSprites(ZXMInfo.UseSkinSprites, "UseSkinSprites"),
    OverlayPosition(ZXMInfo.OverlayPosition, "OverlayPosition"),
    SkinPreference(ZXMInfo.SkinPreference, "SkinPreference"),
    EpilepsyWarning(ZXMInfo.EpilepsyWarning, "EpilepsyWarning"),
    CountdownOffset(ZXMInfo.CountdownOffset, "CountdownOffset"),
    SpecialStyle(ZXMInfo.SpecialStyle, "SpecialStyle"),
    WidescreenStoryboard(ZXMInfo.WidescreenStoryboard, "WidescreenStoryboard"),
    SamplesMatchPlaybackRate(ZXMInfo.SamplesMatchPlaybackRate, "SamplesMatchPlaybackRate"),
    //editor
    Bookmarks(ZXMInfo.Bookmarks, "Bookmarks"),
    DistanceSpacing(ZXMInfo.DistanceSpacing, "DistanceSpacing"),
    BeatDivisor(ZXMInfo.BeatDivisor, "BeatDivisor"),
    GridSize(ZXMInfo.GridSize, "GridSize"),
    TimelineZoom(ZXMInfo.TimelineZoom, "TimelineZoom"),
    //metadata
    Title(ZXMInfo.Title, "Title"),
    TitleUnicode(ZXMInfo.TitleUnicode, "TitleUnicode"),
    Artist(ZXMInfo.Artist, "Artist"),
    ArtistUnicode(ZXMInfo.ArtistUnicode, "ArtistUnicode"),
    Creator(ZXMInfo.MapCreator, "Creator"),
    Version(ZXMInfo.Version, "Version"),
    Source(ZXMInfo.Source, "Source"),
    Tags(ZXMInfo.Tags, "Tags"),
    BeatmapID(ZXMInfo.OsuBeatmapID, "BeatmapID"),
    BeatmapSetID(ZXMInfo.OsuBeatmapSetID, "BeatmapSetID"),
    HPDrainRate(ZXMInfo.HPDrainRate, "HPDrainRate"),
    CircleSize(ZXMInfo.KeyCount, "CircleSize"),
    OverallDifficulty(ZXMInfo.Difficulty, "OverallDifficulty"),
    ApproachRate(ZXMInfo.ApproachRate, "ApproachRate"),
    SliderMultiplier(ZXMInfo.SliderMultiplier, "SliderMultiplier"),
    SliderTickRate(ZXMInfo.SliderTickRate, "SliderTickRate"),
    BackgroundandVideoevents(ZXMInfo.BackgroundandVideoevents, "Background and Videoevents"),
    BreakPeriods(ZXMInfo.BreakPeriods, "Break Periods"),
    StoryboardLayer0Background(ZXMInfo.StoryboardLayer0Background, "Storyboard Layer 0 (Background)"),
    StoryboardLayer1Fail(ZXMInfo.StoryboardLayer1Fail, "Storyboard Layer 1 (Fail)"),
    StoryboardLayer2Pass(ZXMInfo.StoryboardLayer2Pass, "Storyboard Layer 2 (Pass)"),
    StoryboardLayer3Foreground(ZXMInfo.StoryboardLayer3Foreground, "Storyboard Layer 3 (Foreground)"),
    StoryboardLayer4Overlay(ZXMInfo.StoryboardLayer4Overlay, "Storyboard Layer 4 (Overlay)"),
    StoryboardSoundSamples(ZXMInfo.StoryboardSoundSamples, "Storyboard Sound Samples"),
    BackgroundColourTransformations(ZXMInfo.BackgroundColourTransformations, "Background Colour Transformations"),
    Bpm(ZXMInfo.Bpm, ""),
    BgPath(ZXMInfo.BgPath, "")

    ;
    private final ZXMInfo unLocalizedInfo;
    private final String originName;

    OsuInfo(ZXMInfo unLocalizedInfo, String originName) {
        this.unLocalizedInfo = unLocalizedInfo;
        this.originName = originName;
    }
    public ZXMInfo unLocalize() {
        return unLocalizedInfo;
    }

    public String getOriginName() {
        return originName;
    }
}
