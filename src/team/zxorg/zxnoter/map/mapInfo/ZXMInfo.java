package team.zxorg.zxnoter.map.mapInfo;

public enum ZXMInfo{

    //音频文件相对路径
    AudioPath("osu,imd"),

    //背景图片相对路径
    BgPath("osu,imd"),

    //谱面长度(通指首键时间到尾键结束时间)
    MapLength("imd"),

    //音频文件播放之前预留的空白时间
    AudioLeadIn("osu"),

    //时间点总数,应与谱面编辑同步
    TimingCount("imd"),

    //物件总数(在imd中代指表格行数)
    ObjectCount("imd"),

    //bpm,可以是逗号分割的数组,在添加基准bpm时更新
    Bpm("osu,imd"),

    //标题,通常为音乐名罗马音,不可含有非ascii字符
    Title("osu,imd"),

    //艺术家,通常为音乐的曲师罗马音,不可含有非ascii字符
    Artist("osu"),

    //谱师
    MapCreator("osu"),

    //版本,通常指谱面的版本,由谱师自拟
    Version("osu,imd"),

    //按键总数,只有在定轨谱面含有,
    KeyCount("osu,imd"),

    //音频选中时的预览起点时间
    PreviewTime("osu"),

    //谱面开始前的倒数
    Countdown("osu"),

    //按键默认音效组
    SampleSet("osu"),

    //osu文件版本
    osufileformat("osu"),

    //物件重叠效果阈值,osu默认0.7,用途未知
    StackLeniency("osu"),

    //osu模式,定轨模式(mania)默认3
    OsuMode("osu"),

    //osu中是否开启谱面休息段使用黑边填充设置
    LetterboxInBreaks("osu"),

    //osu中是否允许故事板使用玩家皮肤元素
    UseSkinSprites("osu"),

    //设置物件皮肤覆盖层与数字层之间的关系（NoChange = 使用玩家皮肤设定， Below = 覆盖层绘制于数字之下，Above = 覆盖层绘制于数字之上）
    OverlayPosition("osu"),

    //推荐在游玩时使用的皮肤名称
    SkinPreference("osu"),

    //是否开启谱面闪烁（癫痫）警告
    EpilepsyWarning("osu"),

    //谱面第一个物件之前的倒计时的偏移值（拍子）
    CountdownOffset("osu"),

    //是否在 定轨(osu!mania) 谱面中启用 BMS 风格（N+1 键）的键位设置
    SpecialStyle("osu"),

    //是否开启故事板的宽屏显示
    WidescreenStoryboard("osu"),

    //是否允许当变速类型模组开启时，改变音效的播放速率
    SamplesMatchPlaybackRate("osu"),

    //书签（蓝线）的位置（毫秒）
    Bookmarks("osu"),

    //间距锁定倍率
    DistanceSpacing("osu"),

    //节拍细分
    BeatDivisor("osu"),

    //网格大小
    GridSize("osu"),

    //物件时间轴的缩放倍率
    TimelineZoom("osu"),

    //使用unicode编码的标题,支持所有语言
    TitleUnicode("osu"),

    //使用unicode编码的曲师名,支持所有语言
    ArtistUnicode("osu"),

    //歌曲信息与档案的来源
    Source("osu"),

    //易于搜索的标签
    Tags("osu"),

    //osuBid
    OsuBeatmapID("osu"),

    //osuSid
    OsuBeatmapSetID("osu"),

    //掉血速度 (osu)HP 值（0-10）
    HPDrainRate("osu"),

    //难度 (osu)OD 值
    Difficulty("osu"),

    //(osu)AR 值（0-10）
    ApproachRate("osu"),

    //基础滑条速度倍率，乘以 100 后可得到该速度下每拍内滑条会经过多少 osu! 像素
    SliderMultiplier("osu"),

    //滑条点倍率，每拍中滑条点的数量
    SliderTickRate("osu"),

    //
    BackgroundandVideoevents("osu"),

    //
    BreakPeriods("osu"),

    //
    StoryboardLayer0Background("osu"),

    //
    StoryboardLayer1Fail("osu"),

    //
    StoryboardLayer2Pass("osu"),

    //
    StoryboardLayer3Foreground("osu"),

    //
    StoryboardLayer4Overlay("osu"),

    //
    StoryboardSoundSamples("osu"),

    //
    BackgroundColourTransformations("osu"),

    ;
    private final String usages;

    ZXMInfo(String usages) {
        this.usages = usages;
    }

    public String getUsages() {
        return usages;
    }
}
