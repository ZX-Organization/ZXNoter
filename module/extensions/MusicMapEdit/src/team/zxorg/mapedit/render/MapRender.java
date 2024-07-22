package team.zxorg.mapedit.render;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import team.zxorg.mapedit.render.basis.AbstractRender;
import team.zxorg.mapeditcore.map.ZXMap;

public class MapRender extends AbstractRender {
    ZXMap zxMap;


    public MapRender(ZXMap zxMap) {
        this.zxMap = zxMap;


    }


    @Override
    protected void draw(GraphicsContext gc, double cw, double ch, long t) {
        setWidth(HPos.CENTER, 100);
        setHeight(VPos.CENTER, 100);
        setPos(100, 100);

        //addShadow(Color.RED,1);
        addColorAdjust((System.currentTimeMillis() % 2000 - 1000) / 1000.);
        drawImage(RenderTexture.NOTE_END);
    }
}
