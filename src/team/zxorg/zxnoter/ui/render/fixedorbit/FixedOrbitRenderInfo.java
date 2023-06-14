package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.scene.image.Image;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.render.RenderInfo;

public class FixedOrbitRenderInfo extends RenderInfo {


    /**
     * 判定线偏移百分比
     */
    public float judgedLinePosition = 0.95f;


    public enum RenderState {
        NORMAL("normal"),
        SELECTED("selected"),
        SMALL("small");
        private final String name;

        RenderState(String name) {
            this.name = name;
        }
    }




}
