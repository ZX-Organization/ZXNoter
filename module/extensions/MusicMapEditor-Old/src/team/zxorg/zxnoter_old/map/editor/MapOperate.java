package team.zxorg.zxnoter_old.map.editor;

import team.zxorg.zxnoter_old.note.BaseNote;

import java.util.ArrayList;

public class MapOperate {
    /**
     * 同时操作的所有按键,应与结果同样大小
     */
    ArrayList<BaseNote> srcNotes;
    ArrayList<BaseNote> desNotes;

    /**
     * 构建操作
     */
    public MapOperate() {
        srcNotes = new ArrayList<>();
        desNotes = new ArrayList<>();
    }
    private MapOperate(MapOperate srcOperate){
        //深拷贝
        ArrayList<BaseNote> srcNotes = new ArrayList<>();
        ArrayList<BaseNote> desNotes = new ArrayList<>();
        for (BaseNote note:srcOperate.desNotes){
            if (note != null) srcNotes.add(note.clone());
            else srcNotes.add(null);
        }
        for (BaseNote note:srcOperate.srcNotes){
            if (note != null) desNotes.add(note.clone());
            else desNotes.add(null);

        }
        this.srcNotes = srcNotes;
        this.desNotes = desNotes;
    }

    /**
     * 获得相反操作
     * @return 相反操作
     */
    public MapOperate getReverseOperate(){
        return new MapOperate(this);
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

    @Override
    public String toString() {
        return "MapOperate{" +
                "srcNotes=" + srcNotes +
                ", desNotes=" + desNotes +
                '}';
    }
}
