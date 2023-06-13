package team.zxorg.zxnoter.ui.render;

import javafx.scene.canvas.Canvas;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.ui.render.info.RenderInfo;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class MapRender {
    protected Canvas canvas;
    protected ZXMap renderZXMap;
    protected RenderInfo renderInfo;




    public MapRender(ZXMap renderZXMap) {
        this.renderZXMap = renderZXMap;
    }

    /**
     * 执行渲染操作
     */
    public void render() {

        renderHandle();
    }

    public void clearRect() {
        //清除区域
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
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
