package team.zxorg.zxnoter.map.editor;

import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;

import java.awt.geom.Rectangle2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class ZxFixedOrbitMapEditor {
    ZXMap shadowMap;
    ArrayDeque<MapOperate> operateQueue;
    public ZxFixedOrbitMapEditor(ZXMap map,int rollBackSize) {
        this.shadowMap = new ZXMap(new ArrayList<>(),map.timingPoints,map.unLocalizedMapInfo);
        operateQueue = new ArrayDeque<>(rollBackSize);
    }

    /**
     * 编辑按键的轨道变化(可直接编辑shadowMap中的按键)
     * @param note 要编辑的按键
     * @param orbit 轨道变化值
     */
    public void move(FixedOrbitNote note,int orbit){
        if (shadowMap.notes.contains(note)){
            shadowMap.moveNote(note,note.orbit+orbit);
        }else {
            FixedOrbitNote shadowNote = note.clone();
            shadowMap.insertNote(shadowNote);
            shadowMap.moveNote(shadowNote,shadowNote.orbit+orbit);
        }
    }

    /**
     * 编辑组合键中某一子键的轨道变化(自动适配)
     * @param note 要编辑的子键的所属组合键
     * @param orbit 轨道变化值
     * @param childIndex 时间戳变化值
     * @param keepAfterNote 是否保持此子键之后的子键属性
     */
    public void move(FixedOrbitNote note,int orbit,int childIndex,boolean keepAfterNote){
        if (keepAfterNote){
            //保持后面的位置
            if (childIndex == 0){
                //仅移动首子键的轨道位置(此子键必定为长键)
                if (note instanceof LongNote longNote){

                }
            }
        }
        note.setOrbit(note.orbit+orbit);
    }

    /**
     * 编辑按键的时间戳变化(自动排序)
     * @param note 要编辑的按键
     * @param time 时间戳变化值
     * @return 编辑后按键所处的下标位置
     */
    public int move(FixedOrbitNote note,long time){
        FixedOrbitNote shadowNote = note.clone();
        shadowMap.insertNote(shadowNote);
        //操作时间戳并排序
        return shadowMap.moveNote(note,time+note.timeStamp);
    }

    /**
     * 编辑组合键中某一子键的时间戳变化(自动适配)
     * @param note 要编辑的子键的所属组合键
     * @param time 要编辑的子键的时间戳变化
     * @param childIndex 要编辑的子键索引
     * @param keepAfterNote 是否保持此子键之后的子键属性
     * @return 编辑是否成功
     */
    public boolean move(ComplexNote note,long time,int childIndex,boolean keepAfterNote){
        //操作时间戳并排序
        shadowMap.moveNote(note,time+note.timeStamp);
        return false;
    }

    /**
     * 编辑按键参数(编辑,非直接修改)
     * @param parameter 参数变化值
     * @return 编辑是否成功
     */
    public boolean modifyPar(FixedOrbitNote note,int parameter){
        return shadowMap.editNotePar(note,parameter);
    }
    public void modifyDone(){

    }

}
