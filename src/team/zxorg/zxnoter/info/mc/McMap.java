package team.zxorg.zxnoter.info.mc;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Malody Chart JSON 结构映射类
 */
public class McMap {
    public McMeta meta;
    public List<McTime> time;
    public List<McEffect> effect;
    public List<McNote> note;
    public Object extra; // 通常为 null，或者保留不做处理
}

