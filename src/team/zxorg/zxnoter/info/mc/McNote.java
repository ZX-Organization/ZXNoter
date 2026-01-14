package team.zxorg.zxnoter.info.mc;

public class McNote {
    public int[] beat;
    public int[] endbeat; // 长条结束时间

    // 关键：轨道索引 (0 ~ KeyCount-1)
    // 使用 Integer 而不是 int，因为背景音效 Note 可能没有这个字段
    public Integer column;

    public int type; // 1 通常代表 Sound Note
    public String sound;
    public int offset; // 声音偏移
}
