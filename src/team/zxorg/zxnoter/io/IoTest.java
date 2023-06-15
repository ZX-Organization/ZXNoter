package team.zxorg.zxnoter.io;

import team.zxorg.zxnoter.io.reader.ImdReader;
import team.zxorg.zxnoter.io.reader.OsuReader;
import team.zxorg.zxnoter.io.writer.ImdWriter;
import team.zxorg.zxnoter.io.writer.OsuWriter;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.mapInfos.ImdInfo;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.Arrays;

public class IoTest {
    public static void main(String[] args) {
        try {
            ZXMap map = ImdReader.readFile(Path.of("docs/reference/Contrapasso -paradiso-/t+pazolite - Contrapasso -paradiso-_4k_hd.imd"));
            //System.out.println(map.notes.get(19));
            //System.out.println(map.notes.size());
            System.out.println();
            ZXMap convertMap = map.imdConvertNoComplex(ImdInfo.ConvertMethod.BASE_SLIDE);
            //System.out.println(convertMap.notes);
            //System.out.println(Arrays.toString(((ComplexNote)map.notes.get(19)).convertNote(ImdInfo.ConvertMethod.BASE_SLIDE)));
            //System.out.println(map.imdConvertNoComplex(ImdInfo.ConvertMethod.BASE_SLIDE));
            //ZXMap map = OsuReader.readFile(Path.of("docs/reference/LeaF - NANO DEATH!!!!!/LeaF - NANO DEATH!!!!! (nowsmart) [DEATH].osu"));
            //System.out.println(map.timingPoints);
            /*System.out.println(map.unLocalizedMapInfo);
            System.out.println(ImdWriter.checkLocalizedInfos(map));*/
            ImdWriter.writeOut(convertMap , ImdWriter.checkLocalizedInfos(convertMap) , Path.of("G:/desktop"));
            //System.out.println(map.timingPoints);
            //OsuWriter.writeOut(map,OsuWriter.checkLocalizedInfos(map),Path.of("G:/desktop"));
            //System.out.println(map.notes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }
}
