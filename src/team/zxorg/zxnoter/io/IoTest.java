package team.zxorg.zxnoter.io;

import team.zxorg.zxnoter.io.reader.ImdReader;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.editor.ZXFixedOrbitMapEditor;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class IoTest {
    public static void main(String[] args) throws IOException {
        //try {
            ZXMap map = new ImdReader().read(Path.of("docs/reference/xiuluo/Fracture Ray_4k_ez.imd"));


            //System.out.println(map.notes);
            //map.findClosestNote(1000);
            //System.out.println(map.notes.get(19));
            //System.out.println(map.notes.size());
            //System.out.println();
            //ZXMap convertMap = map.imdConvertNoComplex(ImdInfo.ConvertMethod.BASE_SLIDE);
            //System.out.println(convertMap.notes);
            //System.out.println(Arrays.toString(((ComplexNote)map.notes.get(19)).convertNote(ImdInfo.ConvertMethod.BASE_SLIDE)));
            //System.out.println(map.imdConvertNoComplex(ImdInfo.ConvertMethod.BASE_SLIDE));
            //ZXMap map = new OsuReader().read(Path.of("docs/reference/Dan reform jackmap mashup/1.osu"));
        /*for (int i = 0; i < 10; i++) {
            System.out.println("第"+i+"个键->"+map.notes.get(i));
        }*/
        //System.out.println(map.notes.get(4));

        ZXFixedOrbitMapEditor editor = new ZXFixedOrbitMapEditor(map);

        /*System.out.println("原map->"+map.notes);
        editor.addNote(100,1);
        editor.modifyDone();
        System.out.println("操作后map->"+map.notes);
        editor.withdraw();
        System.out.println("撤回后map->"+map.notes);
        editor.redo();
        System.out.println("重做后map->"+map.notes);*/
        System.out.println(map.getScaleNotes(5548,100,true));
        /*System.out.println("原->"+ map.findClosestNotes(5818).get(0));


*//*        editor.move(
                (ComplexNote) map.findClosestNotes(27882).get(0),3,3,true,true
        );
        editor.move(
                (ComplexNote) map.findClosestNotes(27882).get(0),1,3,true,true
        );
        editor.modifyDone();*//*
        editor.addEndOfNote((FixedOrbitNote) map.findClosestNotes(5818).get(0),1,ZXFixedOrbitMapEditor.SLIDE_NOTE);
        editor.addEndOfNote((FixedOrbitNote) map.findClosestNotes(5818).get(0),2,ZXFixedOrbitMapEditor.SLIDE_NOTE);
//        editor.addEndOfNote((FixedOrbitNote) map.findClosestNotes(400).get(0),40L,ZXFixedOrbitMapEditor.LONG_NOTE);

        //System.out.println("全部->" + map.findClosestNotes(5818));


        editor.modifyDone();
        System.out.println("全部->" + map.findClosestNotes(5818));
        System.out.println("后->"+ map.findClosestNotes(5818).get(0));*/
        /*ArrayList<String> strList = new ArrayList<>();
        strList.add(null);
        strList.add(null);
        strList.add(null);
        strList.add(null);
        strList.add(null);
        strList.add(null);
        strList.add(null);
        System.out.println(strList);*/
        //System.out.println(editor.operateStack.get(0));
        /*System.out.println("原->"+map.notes.get(4));

        editor.move((ComplexNote) map.notes.get(4),5000L,1,true,true);
        *//*editor.modifyDone();
        System.out.println("修改一次结果->"+map.notes.get(4));
        editor.move((ComplexNote) map.notes.get(4),1,1,false,false);*//*
        editor.modifyDone();
        System.out.println("最终结果->"+map.notes.get(4));*/
       /* System.out.println(
                map.getScaleNotes(1000,10000,true)
        );*/
        //editor.convertToComplexNote((FixedOrbitNote) map.notes.get(0));
/*

        System.out.println(
                new SlideNote(10617,3,-2).compareTo(
                        new LongNote(10617,1,247)
                )
        );
*/



        /*System.out.println("编辑完成第二次->"+map.notes.get(4));
        for (int i = 0; i < 10; i++) {
            System.out.println("第"+i+"个键->"+map.notes.get(i));
        }*/
            /*ZXFixedOrbitMapEditor editor = new ZXFixedOrbitMapEditor(map);
            System.out.println("第一个->"+map.notes.get(0));
            System.out.println("第二个->"+map.notes.get(1));
            editor.move((FixedOrbitNote)map.notes.get(0),1,false);
            System.out.println("完成编辑前:"+editor.shadowMap.notes);
            editor.modifyDone();
            System.out.println("完成编辑后:"+editor.shadowMap.notes);
            System.out.println("第一个->"+map.notes.get(0));
            System.out.println("第二个->"+map.notes.get(1));
            System.out.println(editor.shadowMap.notes);
            editor.move((FixedOrbitNote)map.notes.get(0),1,false);
            System.out.println(editor.shadowMap.notes);
            editor.modifyDone();
            System.out.println(editor.shadowMap.notes);
            System.out.println("第一个->"+map.notes.get(0));
            System.out.println("第二个->"+map.notes.get(1));*/
            //System.out.println( map.timingPoints);
            //System.out.println(map.findClosestTimings(30000));
            //System.out.println(map.timingPoints);
            /*System.out.println(map.unLocalizedMapInfo);
            System.out.println(ImdWriter.checkLocalizedInfos(map));*/
            //ImdWriter.writeOut(convertMap , ImdWriter.checkLocalizedInfos(convertMap) , Path.of("G:/desktop"));
            //System.out.println(map.timingPoints);
            //OsuWriter.writeOut(map,OsuWriter.checkLocalizedInfos(map),Path.of("G:/desktop"));
            //System.out.println(OsuWriter.checkLocalizedInfos(convertMap));
            //System.out.println(map.notes);
        /*} catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        /*catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }*/

    }
}
