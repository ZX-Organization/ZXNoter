package team.zxorg.zxnoter.map.editor;

import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;

import java.util.ArrayList;

public class MapOperate {
    /**
     * 同时操作的所有按键,应与结果同样大小
     */
    ArrayList<FixedOrbitNote> srcNotes;
    ArrayList<BaseNote> desNotes;

    /**
     * 构建操作
     */
    public MapOperate() {
        srcNotes = new ArrayList<>();
        desNotes = new ArrayList<>();
    }
    private MapOperate(MapOperate withdrawOperate){
        //深拷贝
        ArrayList<BaseNote> srcNotes = new ArrayList<>();
        ArrayList<BaseNote> desNotes = new ArrayList<>();
        for (BaseNote note:withdrawOperate.desNotes){
            srcNotes.add(note.clone());
        }
        for (FixedOrbitNote note:withdrawOperate.srcNotes){
            desNotes.add(note.clone());
        }
    }

    /**
     * 获得重做操作
     * @param withdrawOperate 撤销操作
     * @return 重做操作
     */
    public MapOperate getRedoOperate(MapOperate withdrawOperate){
        return new MapOperate(withdrawOperate);
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof MapOperate mapOperate) {
            if (srcNotes.size() == mapOperate.srcNotes.size()){
                //大小相同
                for (int i = 0; i < srcNotes.size(); i++) {
                    //判断每一个是否都相同
                    if (!mapOperate.srcNotes.get(i).equals(srcNotes.get(i))){
                        return false;
                    }
                }
            }else {
                return false;
            }
                return true;
        } else
            return false;
    }
}
