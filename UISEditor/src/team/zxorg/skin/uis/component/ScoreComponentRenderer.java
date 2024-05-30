package team.zxorg.skin.uis.component;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import team.zxorg.skin.uis.ExpressionVector;
import team.zxorg.skin.uis.UISComponent;
import team.zxorg.skin.uis.UISFrame;
import team.zxorg.zxncore.ZXLogger;

public class ScoreComponentRenderer extends AbstractComponentRenderer {
    //定义数字图片素材, 分别是0到9, 百分号, 小数点, 减号, 加号, 和乘号, 一共需要15张图片
    UISFrame frame;
    //字体的高度 宽度需要结合图片计算比例缩放
    ExpressionVector fsize;
    //固定子字宽 跟随第一帧
    double fixWeight;
    //x值表示每个字符之间的空隙间距
    ExpressionVector pos2;
    //当前的分数
    double score = 0;
    boolean isACC;

    public ScoreComponentRenderer(UISComponent component) {
        super(component);
        isACC = component.getName().equals("score-acc");
    }

    @Override
    void reloadResComponent() {
        //System.out.println(name + " reloadRes");
        frame = component.getFrame("frame");
        if (frame != null) {
            Image image = frame.getFrame(0);
            if (image != null) {
                fixWeight = image.getWidth() / image.getHeight();
                texSize.setW(image.getWidth());
                texSize.setH(image.getHeight());
            }
        }
    }

    @Override
    void reloadPosComponent() {
        //System.out.println(name + " reloadPos");
        score = 0;
        pos2 = component.getExpressionVector("pos2");
        fsize = component.getExpressionVector("fsize");


        //reloadStyle();
    }

    @Override
    void drawComponent(double width, double height, long time) {


        transform();
        String scoreStr;
        if (isACC) {
            score = Math.abs(time) / 200 % 100;
            /*score += 0.11;
            score %= 100;*/
            scoreStr = String.format("%.2f%%", score);
        } else {
            score = Math.abs(time) % 10000;
            /*score += 1;
            score %= 1000;*/
            scoreStr = (int) score + "";
        }

        //计算所需宽度
        double cw = fixWeight * fsize.getW();
        double aw = cw * scoreStr.length() + pos2.getX();


        // 设置初始绘制位置
        double x = pos.getX() - switch (anchor.getHpos()) {
            case LEFT -> 0;
            case CENTER -> aw / 2;
            case RIGHT -> aw;
        };
        double y = pos.getY() - switch (anchor.getVpos()) {
            case TOP -> 0;
            case CENTER, BASELINE -> fsize.getW() / 2;
            case BOTTOM -> fsize.getW();
        };
        // 遍历字符串中的每个字符
        char[] sc = scoreStr.toCharArray();
        for (int i = 0; i < sc.length; i++) {
            int index = getCharIndex(sc[i]);
            if (index != -1) {
                //double cw = frameWeight.get(index) * fsize;
                // 获取当前字符对应的图片
                Image charFrame = frame.getFrame(index);
                // 绘制字符图片
                //rr.setPos(Pos.BOTTOM_LEFT, x, y);
                /*if (frameWeight.size() > index)
                    fw = frameWeight.get(index);*/
                //rr.setSize(Pos.BOTTOM_LEFT, );
                if (charFrame == null) {
                    ZXLogger.warning("???: {" + sc[i] + "}");

                    continue;
                }
                //System.out.println("  " + x + "  " + y + "  " + cw + "  " + fsize.getW());
                gc.drawImage(charFrame, x, y, cw, fsize.getW());
                gc.setStroke(Color.GREEN);
                gc.strokeRect(x, y, cw, fsize.getW());

                // rr.drawImage(gc, charFrame);
                // 调整下一个字符的绘制位置
            }
            if (sc.length - 1 != i)
                x += cw + pos2.getX(); // 使用 pos2.getX() 作为字符之间的空隙
        }
    }


    private int getCharIndex(char c) {
        return "0123456789%.-+x".indexOf(Character.toLowerCase(c));
    }

}
