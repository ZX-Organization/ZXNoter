package team.zxorg.zxnoter.map.editor;

import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.fixedorbit.SlideNote;

import java.awt.geom.Rectangle2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class ZxFixedOrbitMapEditor {
    /**
     * 实际zxMap
     */
    ZXMap srcMap;
    /**
     * 虚影map
     */
    ZXMap shadowMap;
    /**
     * 操作队列
     */
    Stack<MapOperate> operateStack;
    Stack<MapOperate> withdrawStack;
    private MapOperate tempMapOperate;
    private FixedOrbitNote tempEditNote;
    private FixedOrbitNote tempShadowNote;

    public ZxFixedOrbitMapEditor(ZXMap map) {
        srcMap = map;
        this.shadowMap = new ZXMap(new ArrayList<>(), map.timingPoints, map.unLocalizedMapInfo);
        operateStack = new Stack<>();
        withdrawStack = new Stack<>();
    }

    /**
     * 编辑按键的轨道变化(可直接编辑shadowMap中的按键)
     *
     * @param note  要编辑的按键
     * @param orbit 轨道变化值
     */
    public void move(FixedOrbitNote note, int orbit) {
        if (tempMapOperate == null || !tempMapOperate.srcNote.equals(note)) {
            //上一次操作的不是此按键,新建
            tempMapOperate = new MapOperate(note);
            //获得原按键引用
            tempEditNote = note;
            //克隆获得虚影按键
            tempShadowNote = note.clone();
        }
        //将虚影按键加入虚影map中
        int desNoteIndex = shadowMap.insertNote(tempShadowNote);
        //对虚影按键进行编辑
        shadowMap.moveNote(tempShadowNote, tempShadowNote.orbit += orbit);
        //设置操作结果(上一次也是操作此按键时覆盖)
        tempMapOperate.desNote = (FixedOrbitNote) shadowMap.notes.get(desNoteIndex);
    }

    /**
     * 编辑组合键中某一子键的轨道变化(此子键必须是长条)
     *
     * @param note          要编辑的子键的所属组合键
     * @param orbit         轨道变化值
     * @param childIndex    子键下标
     * @param keepAfterNote 是否保持此子键之后的子键属性
     */
    public void move(ComplexNote note, int orbit, int childIndex, boolean keepAfterNote) {
        if (tempMapOperate == null || !tempMapOperate.srcNote.equals(note)) {
            //上一次操作的不是此按键,新建
            tempMapOperate = new MapOperate(note);
            tempEditNote = note;
        }
        //克隆获得虚影按键
        ComplexNote shadowNote = note.clone();
        int desNoteIndex = shadowMap.insertNote(shadowNote);
        //shadowNote.setRelatively(true);
        //编辑子键
        FixedOrbitNote child = shadowNote.notes.get(childIndex);
        if (child instanceof LongNote childLongNote) {
            //区分是否保持后方子键位置与时间戳
            if (keepAfterNote) {
                //保持
                //直接编辑子键
                childLongNote.orbit += orbit;
                //检查组合键是否有断裂(从此子键上一个子键检查到此子键的下一个子键)
                //检查此子键前一个按键
                if (childIndex == 0) {
                    //子键处于组合键头部(头部按键只能编辑长键[拉出])
                    //头部添加一个滑键
                    shadowNote.notes.add(new SlideNote(shadowNote.timeStamp, shadowNote.orbit, childLongNote.orbit - shadowNote.orbit));
                    //排序
                    shadowNote.notes.sort(FixedOrbitNote::compareTo);
                } else {
                    FixedOrbitNote previous = shadowNote.notes.get(childIndex - 1);
                    if (previous instanceof SlideNote slideNote) {
                        //编辑的(child)是长条,前一个(previous)一定是滑键
                        if (slideNote.orbit + slideNote.slideArg != child.orbit) {
                            //修正前一个滑键参数
                            slideNote.slideArg = child.orbit - slideNote.orbit;
                        }
                    }
                }
                //检查此子键下一个按键
                if (childIndex == shadowNote.notes.size() - 1){
                    //子键处于组合键尾部
                }else {
                    FixedOrbitNote next = shadowNote.notes.get(childIndex + 1);
                    if (next instanceof SlideNote slideNote){
                        if (childLongNote.orbit != slideNote.orbit){
                            //编辑的按键和下一个按键轨道错位
                            //缓存下下个物件的轨道位置
                            int nextNextOrbit = slideNote.orbit + slideNote.slideArg;
                            slideNote.orbit = childLongNote.orbit;
                            slideNote.slideArg = nextNextOrbit-slideNote.orbit;
                        }
                    }
                }
            } else {
                //跟随

            }
        }
    }

    /**
     * 编辑按键的时间戳变化(自动排序)
     *
     * @param note 要编辑的按键
     * @param time 时间戳变化值
     * @return 编辑后按键所处的下标位置
     */
    public int move(FixedOrbitNote note, long time) {
        FixedOrbitNote shadowNote = note.clone();
        shadowMap.insertNote(shadowNote);
        //操作时间戳并排序
        return shadowMap.moveNote(note, time + note.timeStamp);
    }

    /**
     * 编辑组合键中某一子键的时间戳变化(自动适配)
     *
     * @param note          要编辑的子键的所属组合键
     * @param time          要编辑的子键的时间戳变化
     * @param childIndex    要编辑的子键索引
     * @param keepAfterNote 是否保持此子键之后的子键属性
     * @return 编辑是否成功
     */
    public boolean move(ComplexNote note, long time, int childIndex, boolean keepAfterNote) {
        //操作时间戳并排序
        shadowMap.moveNote(note, time + note.timeStamp);
        return false;
    }

    /**
     * 编辑按键参数(编辑,非直接修改)
     *
     * @param parameter 参数变化值
     * @return 编辑是否成功
     */
    public boolean modifyPar(FixedOrbitNote note, int parameter) {
        return shadowMap.editNotePar(note, parameter);
    }

    /**
     * 完成修改,同步原map,操作栈
     */
    public void modifyDone() {
        //完成修改,同步到原zxMap中
        //检查按键是否为组合键,修复其中多余节点和0参滑键
        if (tempEditNote instanceof ComplexNote complexNote){
            ArrayList<FixedOrbitNote> deleteList = new ArrayList<>();
            for (int i = 1; i < complexNote.notes.size(); i++) {
                FixedOrbitNote child = complexNote.notes.get(i);
                if (child instanceof SlideNote slideNote){
                    //滑子键
                    if (slideNote.slideArg == 0){
                        //将零参滑加入删除列表
                        deleteList.add(slideNote);
                    }
                }else if (child instanceof LongNote thisLongNote){
                    //长子键
                    if (complexNote.notes.get(i-1) instanceof LongNote previousLongNote){
                        //检查到此长子键前一个也是长子键
                        previousLongNote.sustainedTime+= thisLongNote.sustainedTime;
                        deleteList.add(thisLongNote);
                        /*if ()*/
                    }
                }
            }
            complexNote.notes.removeAll(deleteList);
        }
        //克隆结果插入原map
        srcMap.insertNote(tempMapOperate.desNote.clone());
        //删除原按键
        srcMap.deleteNote(tempEditNote);
        //添加到操作堆栈
        operateStack.add(tempMapOperate);

    }

}
