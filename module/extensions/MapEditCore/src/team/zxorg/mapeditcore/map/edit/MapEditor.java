package team.zxorg.mapeditcore.map.edit;

import team.zxorg.mapeditcore.map.MapUtil;
import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.mapElement.IMapElement;
import team.zxorg.mapeditcore.mapElement.note.Note;
import team.zxorg.mapeditcore.mapElement.timing.Timing;

import java.util.ArrayList;
import java.util.Stack;

public class MapEditor {

    ZXMap map;
    Stack<MapOperate> operates;
    Stack<MapOperate> deOperates;

    public MapEditor(ZXMap map) {
        this.map = map;
        operates = new Stack<>();
        deOperates = new Stack<>();
    }

    /**
     * 有序地插入元素
     */
    public void insertElement(IMapElement ele) {
        if (ele instanceof Note)
            addElement(ele, map.notes);
        if (ele instanceof Timing)
            addElement(ele, map.timings);
        operates.add(new MapOperate(null, ele));
    }

    /**
     * 删除元素
     */
    public void deleteNote(IMapElement ele) {
        map.notes.remove(ele);
        operates.add(new MapOperate(ele, null));
    }

    /**
     * 撤销
     */
    public void undo() {
        if (operates.size() > 0) {
            deOperates.add(operates.pop().deOperate(map));
        }else {
            throw new RuntimeException("操作栈已空");
        }
    }

    /**
     * 重做
     */
    public void redo(){
        if (deOperates.size() > 0) {
            operates.add(deOperates.pop().deOperate(map));
        }else {
            throw new RuntimeException("撤回栈已空");
        }
    }

    /**
     * 添加元素
     */
    private void addElement(IMapElement element, ArrayList<IMapElement> list) {
        int time = element.getTime();
        if (list.size() > 0) {
            if (list.size() > 1) {
                //表中有两个及以上
                list.add(MapUtil.binarySearchMapElement(list, time), element);
            } else {
                //表中只有一个
                if (list.get(0).getTime() <= time)
                    list.add(element);
            }
        } else
            list.add(element);
    }
}
