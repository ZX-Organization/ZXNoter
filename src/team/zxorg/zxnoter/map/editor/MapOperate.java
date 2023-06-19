package team.zxorg.zxnoter.map.editor;

import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;

public class MapOperate {
    FixedOrbitNote srcNote;
    FixedOrbitNote desNote;

    /**
     * 构建操作
     * @param srcNote 原Note
     */
    public MapOperate(FixedOrbitNote srcNote) {
        this.srcNote = srcNote;
    }
    private MapOperate(MapOperate withdrawOperate){
        srcNote = withdrawOperate.desNote.clone();
        desNote = withdrawOperate.srcNote.clone();
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
            if (srcNote.equals(mapOperate.srcNote)){
                return true;
            }
                return false;
        } else
            return false;
    }
}
