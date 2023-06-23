package team.zxorg.zxnoter.io;

import team.zxorg.zxnoter.io.reader.ImdReader;
import team.zxorg.zxnoter.io.reader.OsuReader;
import team.zxorg.zxnoter.map.ZXMap;

import java.io.IOException;
import java.nio.file.Path;

public class IoTest {
    public static void main(String[] args) throws IOException {
        //try {
            //ZXMap map = ImdReader.readFile(Path.of("docs/reference/Contrapasso -paradiso-/t+pazolite - Contrapasso -paradiso-_4k_hd.imd"));
            //System.out.println(map.notes);
            //map.findClosestNote(1000);
            //System.out.println(map.notes.get(19));
            //System.out.println(map.notes.size());
            //System.out.println();
            //ZXMap convertMap = map.imdConvertNoComplex(ImdInfo.ConvertMethod.BASE_SLIDE);
            //System.out.println(convertMap.notes);
            //System.out.println(Arrays.toString(((ComplexNote)map.notes.get(19)).convertNote(ImdInfo.ConvertMethod.BASE_SLIDE)));
            //System.out.println(map.imdConvertNoComplex(ImdInfo.ConvertMethod.BASE_SLIDE));
            //ZXMap map = OsuReader.readFile(Path.of("docs/reference/Dan reform jackmap mashup/1.osu"));
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
