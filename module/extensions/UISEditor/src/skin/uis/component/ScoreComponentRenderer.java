package skin.uis.component;

import javafx.scene.image.Image;
import skin.uis.ExpressionVector;
import skin.uis.UISComponent;
import skin.uis.UISFrame;

import java.util.ArrayList;
import java.util.List;

public class ScoreComponentRenderer extends AbstractComponentRenderer {
    //定义数字图片素材, 分别是0到9, 百分号, 小数点, 减号, 加号, 和乘号, 一共需要15张图片
    UISFrame frame;
    List<Double> frameWeight;
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
        frame = component.getFrame("frame");


        frameWeight = new ArrayList<>();
        for (Image image : frame.getFrames()) {
            frameWeight.add(image.getWidth() / image.getHeight());
        }
        fixWeight = frameWeight.getFirst();
        Image image = frame.getFrame(0);
        if (image != null) {
            texSize.setW(image.getWidth());
            texSize.setH(image.getHeight());
        }
    }

    @Override
    void reloadPosComponent() {
        pos2 = component.getExpressionVector("pos2");
        fsize = component.getExpressionVector("fsize");
    }

    @Override
    void drawComponent(double width, double height, long time) {
        String scoreStr;
        if (isACC) {
            score += 0.11;
            score %= 100;
            scoreStr = String.format("%.2f%%", score);
        } else {
            score += 1;
            score %= 1000;
            scoreStr = (int) score + "";
        }

        double aw = 0;
        double cw = fixWeight * fsize.getW();

        //计算所需宽度
        for (char c : scoreStr.toCharArray()) {
            int index = getCharIndex(c);
            //double cw = frameWeight.get(index) * fsize;
            aw += cw + pos2.getX();
        }


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
        for (char c : scoreStr.toCharArray()) {
            int index = getCharIndex(c);
            if (index != -1) {
                //double cw = frameWeight.get(index) * fsize;
                // 获取当前字符对应的图片
                Image charFrame = frame.getFrame(index);

                // 绘制字符图片
                //rr.setPos(Pos.BOTTOM_LEFT, x, y);
                double fw = 8;
                if (frameWeight.size() > index)
                    fw = frameWeight.get(index);
                //rr.setSize(Pos.BOTTOM_LEFT, );
                drawImage(charFrame, x, y, fw * fsize.getW(), fsize.getW());
                // rr.drawImage(gc, charFrame);
                // 调整下一个字符的绘制位置
            }
            x += cw + pos2.getX(); // 使用 pos2.getX() 作为字符之间的空隙
        }
    }


    private int getCharIndex(char c) {
        return "0123456789%.-+x".indexOf(Character.toLowerCase(c));
    }

}
