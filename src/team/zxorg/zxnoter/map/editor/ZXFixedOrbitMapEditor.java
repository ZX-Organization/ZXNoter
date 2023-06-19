package team.zxorg.zxnoter.map.editor;

import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.mapInfos.ImdInfo;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.fixedorbit.SlideNote;

import java.util.ArrayList;
import java.util.Stack;

public class ZXFixedOrbitMapEditor {
    /**
     * 实际zxMap
     */
    ZXMap srcMap;
    /**
     * 虚影map
     */
    public ZXMap shadowMap;
    /**
     * 操作队列
     */
    Stack<MapOperate> operateStack;
    Stack<MapOperate> withdrawStack;
    private ArrayList<FixedOrbitNote> shadows;
    private MapOperate tempMapOperate;

    public ZXFixedOrbitMapEditor(ZXMap map) {
        srcMap = map;
        this.shadowMap = new ZXMap(new ArrayList<>(), map.timingPoints, map.unLocalizedMapInfo);
        operateStack = new Stack<>();
        withdrawStack = new Stack<>();
        shadows =new ArrayList<>();
    }

    /**
     * 编辑按键的轨道变化(可直接编辑shadowMap中的按键)
     *
     * @param note  要编辑的按键
     * @param orbit 轨道变化值
     */
    public void move(FixedOrbitNote note, int orbit) {

        checkOperate(note);
        //克隆获得虚影按键
        FixedOrbitNote shadowNote = note.clone();
        //将虚影按键加入虚影map中
        shadowMap.insertNote(shadowNote);
        shadows.add(shadowNote);

        //对虚影按键进行编辑
        shadowMap.moveNote(shadowNote, shadowNote.orbit += orbit);
        //添加操作结果(上一次也是操作此按键时覆盖)
        tempMapOperate.desNotes.add(shadowNote);

    }

    /**
     * 编辑组合键中某一子键的轨道变化(此子键必须是长条)
     * @param note          要编辑的子键的所属组合键
     * @param orbit         轨道变化值
     * @param childIndex    子键下标
     * @param keepAfterNote 是否保持此子键之后所有按键的绝对位置
     */
    public boolean move(ComplexNote note, int orbit, int childIndex, boolean keepAfterNote) {
        checkOperate(note);
        //克隆获得虚影按键
        ComplexNote shadowNote = note.clone();
        shadows.add(shadowNote);
        int desNoteIndex = shadowMap.insertNote(shadowNote);
        //shadowNote.setRelatively(true);
        //编辑子键
        FixedOrbitNote child = shadowNote.notes.get(childIndex);
        if (child instanceof LongNote childLongNote) {
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
            //区分是否保持后方子键位置与时间戳
            if (keepAfterNote) {
                //保持(后方不需要移动)
                //检查此子键下一个按键
                if (childIndex == shadowNote.notes.size() - 1) {
                    //子键处于组合键尾部无需操作
                } else {
                    FixedOrbitNote next = shadowNote.notes.get(childIndex + 1);
                    if (next instanceof SlideNote slideNote) {
                        if (childLongNote.orbit != slideNote.orbit) {
                            //编辑的按键和下一个按键轨道错位
                            //缓存下下个物件的轨道位置
                            int nextNextOrbit = slideNote.orbit + slideNote.slideArg;
                            slideNote.orbit = childLongNote.orbit;
                            slideNote.slideArg = nextNextOrbit - slideNote.orbit;
                        }
                    }
                }
            } else {
                if (childIndex == shadowNote.notes.size() - 1){
                    tempMapOperate.desNotes.add(shadowNote);
                    return true;
                }
                //跟随
                //检查
                for (int i = childIndex+1; i < shadowNote.notes.size()-1; i++) {
                    int resOrb = shadowNote.notes.get(i).orbit + orbit;
                    if (resOrb >= Integer.parseInt(shadowMap.unLocalizedMapInfo.getInfo(ImdInfo.ImdKeyCount.unLocalize()))){
                        return false;
                    }
                }
                //未return
                for (int i = childIndex+1; i < shadowNote.notes.size()-1; i++) {
                    shadowNote.notes.get(i).orbit += orbit;
                }
            }
            tempMapOperate.desNotes.add(shadowNote);
            return true;
        }
        return false;
    }

    /**
     * 编辑按键的时间戳变化(自动排序)
     * @param note 要编辑的按键
     * @param time 时间戳变化值
     * @return 编辑后按键所处的下标位置
     */
    public int move(FixedOrbitNote note, long time) {
        checkOperate(note);
        //克隆获得虚影按键
        FixedOrbitNote shadowNote = note.clone();
        int desNoteIndex = shadowMap.insertNote(shadowNote);

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
     * @param keepAfterNote 是否保持此子键之后所有按键的绝对位置
     * @return 编辑是否成功
     */
    public boolean move(ComplexNote note, long time, int childIndex, boolean keepAfterNote) {
        //操作时间戳并排序
        shadowMap.moveNote(note, time + note.timeStamp);
        return false;
    }

    /**
     * 编辑按键参数(变化,非直接修改)
     *
     * @param parameter 参数变化值
     * @return 编辑是否成功
     */
    public boolean modifyPar(FixedOrbitNote note, int parameter) {
        return shadowMap.editNotePar(note, parameter);
    }

    /**
     * 编辑组合键中指定下标子键的参数(编辑,非直接修改)
     * @param complexNote 要编辑的组合键
     * @param childIndex 要编辑的组合键中的子键的下标
     * @param keepAfterNote 是否保持此子键之后所有按键的绝对位置
     * @return
     */
    public boolean modifyPar(ComplexNote complexNote, int childIndex, boolean keepAfterNote){
        return true;
    }

    /**
     * 完成修改,同步原map,操作栈
     */
    public void modifyDone() {
        //完成修改,同步到原zxMap中
        if (tempMapOperate == null){
            return;
        }
        //检查操作结果中是否包含组合键
        for (FixedOrbitNote tempEditNote:tempMapOperate.desNotes){
            if (tempEditNote instanceof ComplexNote complexNote){
                checkComplexNote(complexNote);
            }
        }
        //克隆结果插入原map
        for (FixedOrbitNote note:tempMapOperate.desNotes)
            srcMap.insertNote(note.clone());
        //删除原按键
        for (FixedOrbitNote note:tempMapOperate.srcNotes)
            srcMap.deleteNote(note);
        //清除此次编辑产生的所有虚影
        for (int i = 0; i < shadows.size(); i++) {
            shadowMap.deleteNote(shadows.get(i));
        }
        //添加到操作堆栈
        operateStack.add(tempMapOperate);
        tempMapOperate = null;

    }

    /**
     * 检查操作(检查是否初始化和是否已包含此按键相关操作)
     * @param srcNote 要检查的按键
     */
    private void checkOperate(FixedOrbitNote srcNote) {
        if (tempMapOperate == null) {
            //上一次无操作新建
            tempMapOperate = new MapOperate();
            //加入操作源中
            tempMapOperate.srcNotes.add(srcNote);
        }else if (!tempMapOperate.srcNotes.contains(srcNote)){
            //上一次有操作,但操作对象不包含传入按键
            //加入操作源中
            tempMapOperate.srcNotes.add(srcNote);
        }

    }
    private void checkComplexNote(ComplexNote note) {
        //检查按键是否为组合键,修复其中多余节点和0参滑键
        ArrayList<FixedOrbitNote> deleteList = new ArrayList<>();
        for (int i = 1; i < note.notes.size(); i++) {
            FixedOrbitNote child = note.notes.get(i);
            if (child instanceof SlideNote slideNote) {
                //滑子键
                if (slideNote.slideArg == 0) {
                    //将零参滑加入删除列表
                    deleteList.add(slideNote);
                }
            } else if (child instanceof LongNote thisLongNote) {
                LongNote tempLongNote = thisLongNote;
                //长子键
                //向前检查所有同轨长键
                int index = i - 1;
                while (note.notes.get(index--) instanceof LongNote temp) {
                    //检查到此长子键前一个也是长子键
                    temp.sustainedTime += tempLongNote.sustainedTime;
                    deleteList.add(tempLongNote);
                    //向前移动
                    tempLongNote = temp;
                }
            }
        }
        note.notes.removeAll(deleteList);
    }

}
