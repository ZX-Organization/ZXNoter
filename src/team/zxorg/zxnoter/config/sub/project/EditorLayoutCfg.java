package team.zxorg.zxnoter.config.sub.project;

import java.util.ArrayList;

public class EditorLayoutCfg {
    /**
     * 方向
     */
    public String orientation;
    /**
     * 属于布局 记录子布局和选项卡对象
     */
    public ArrayList<EditorLayoutCfg> subLayout;
    /**
     * 属于选项卡 记录打开的编辑器
      */
    public ArrayList<String> editors;
}
