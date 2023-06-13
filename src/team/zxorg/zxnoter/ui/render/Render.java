package team.zxorg.zxnoter.ui.render;

import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.ui.render.info.RenderInfo;

import java.util.ArrayList;

public abstract class Render {
    protected ZXMap renderZXMap;
    protected RenderInfo renderInfo;

    /**
     * 记录的渲染物件列表
     */
    protected ArrayList<BaseNote> renderNotes = new ArrayList<>();


    public Render(ZXMap renderZXMap) {
        this.renderZXMap = renderZXMap;
    }

    /**
     * 执行渲染操作
     */
    public void render() {

        renderHandle();
    }


    /**
     * 渲染处理
     */
    abstract void renderHandle();

    /**
     * 是否在渲染范围内
     *
     * @param note 物件
     * @return 是否
     */
    abstract boolean isInRenderRange(BaseNote note);
}
