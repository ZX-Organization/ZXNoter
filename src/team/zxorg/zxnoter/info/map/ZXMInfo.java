package team.zxorg.zxnoter.info.map;

public enum ZXMInfo{

    //音频文件相对路径
    AudioPath("osu,imd", ""),

    //背景图片相对路径
    BgPath("osu,imd", ""),

    //谱面长度(通指首键时间到尾键结束时间)
    MapLength("imd", "0"),

    //音频文件播放之前预留的空白时间
    AudioLeadIn("osu", "0"),

    //时间点总数,应与谱面编辑同步
    TimingCount("imd", "0"),

    //物件总数(在imd中代指表格行数)
    ObjectCount("imd", "0"),

    //bpm,可以是逗号分割的数组,在添加基准bpm时更新
    Bpm("osu,imd", ""),

    //标题,通常为音乐名罗马音,不可含有非ascii字符
    Title("osu,imd", ""),

    //艺术家,通常为音乐的曲师罗马音,不可含有非ascii字符
    Artist("osu", ""),

    //谱师
    MapCreator("osu", "_zxn"),

    //版本,通常指谱面的版本,由谱师自拟
    Version("osu,imd", "Insane"),

    //音乐时长
    AudioLength("", "0"),

    //按键总数,只有在定轨谱面含有,
    KeyCount("osu,imd", "4"),

    //音频选中时的预览起点时间
    PreviewTime("osu",  "-1"),

    //谱面开始前的倒数
    Countdown("osu", "1"),

    //按键默认音效组
    SampleSet("osu",  "Normal"),

    //osu文件版本
    osufileformat("osu" , "v14"),

    //物件重叠效果阈值,osu默认0.7,用途未知
    StackLeniency("osu", "0.7"),

    //osu模式,定轨模式(mania)默认3
    OsuMode("osu", "3"),

    //osu中是否开启谱面休息段使用黑边填充设置
    LetterboxInBreaks("osu",  "0"),

    //osu中是否允许故事板使用玩家皮肤元素
    UseSkinSprites("osu",  "0"),

    //设置物件皮肤覆盖层与数字层之间的关系（NoChange = 使用玩家皮肤设定， Below = 覆盖层绘制于数字之下，Above = 覆盖层绘制于数字之上）
    OverlayPosition("osu", "NoChange"),

    //推荐在游玩时使用的皮肤名称
    SkinPreference("osu", ""),

    //是否开启谱面闪烁（癫痫）警告
    EpilepsyWarning("osu",  "0"),

    //谱面第一个物件之前的倒计时的偏移值（拍子）
    CountdownOffset("osu",  "0"),

    //是否在 定轨(osu!mania) 谱面中启用 BMS 风格（N+1 键）的键位设置
    SpecialStyle("osu",  "0"),

    //是否开启故事板的宽屏显示
    WidescreenStoryboard("osu", "0"),

    //是否允许当变速类型模组开启时，改变音效的播放速率
    SamplesMatchPlaybackRate("osu", "0"),

    //书签（蓝线）的位置（毫秒）
    Bookmarks("osu", ""),

    //间距锁定倍率
    DistanceSpacing("osu",  "1"),

    //节拍细分
    BeatDivisor("osu", "12"),

    //网格大小
    GridSize("osu", "4"),

    //物件时间轴的缩放倍率
    TimelineZoom("osu",  "2.5"),

    //使用unicode编码的标题,支持所有语言
    TitleUnicode("osu", ""),

    //使用unicode编码的曲师名,支持所有语言
    ArtistUnicode("osu", ""),

    //歌曲信息与档案的来源
    Source("osu", ""),

    //易于搜索的标签
    Tags("osu", ""),

    //osuBid
    OsuBeatmapID("osu", "-1"),

    //osuSid
    OsuBeatmapSetID("osu",  "-1"),

    //掉血速度 (osu)HP 值（0-10）
    HPDrainRate("osu",  "9"),

    //难度 (osu)OD 值
    Difficulty("osu", "8.5"),

    //(osu)AR 值（0-10）
    ApproachRate("osu", "5"),

    //基础滑条速度倍率，乘以 100 后可得到该速度下每拍内滑条会经过多少 osu! 像素
    SliderMultiplier("osu", "1.4"),

    //滑条点倍率，每拍中滑条点的数量
    SliderTickRate("osu", "1"),

    //
    BackgroundandVideoevents("osu", "0,0,\"bg.png\",0,0"),

    //
    BreakPeriods("osu", ""),

    //
    StoryboardLayer0Background("osu", ""),

    //
    StoryboardLayer1Fail("osu", ""),

    //
    StoryboardLayer2Pass("osu", ""),

    //
    StoryboardLayer3Foreground("osu", ""),

    //
    StoryboardLayer4Overlay("osu", ""),

    //
    StoryboardSoundSamples("osu", ""),

    //
    BackgroundColourTransformations("osu", ""),

    ;
    private final String usages;
    private final String defaultValue;

    ZXMInfo(String usages, String defaultValue) {
        this.usages = usages;
        this.defaultValue = defaultValue;
    }

    public String getUsages() {
        return usages;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
