package team.zxorg.mapeditcore.note;

import team.zxorg.extensionloader.core.Logger;

public class SlideNote extends Note{
    /**
     * 滑动方向角度,0~360
     */
    private double direction;

    /**
     * 获取滑键方向
     * @return 方向
     */
    public double getDirection() {
        return direction;
    }

    /**
     * 设置滑键方向
     * @param direction 方向
     * @return 结果
     */
    public double setDirection(double direction) {
        if (direction<=360 &&direction>=0)
            return this.direction = direction;
        else {
            Logger.info("使用非法方向->" + direction + '\n' + "物件:" +this);
            return this.direction;
        }
    }
}
