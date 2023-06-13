package team.zxorg.zxnoter.io;

import team.zxorg.zxnoter.io.reader.ImdReader;
import team.zxorg.zxnoter.io.reader.OsuReader;
import team.zxorg.zxnoter.io.writer.ImdWriter;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;

public class IoTest {
    public static void main(String[] args) throws IOException {
        try {
            //ZXMap map = ImdReader.readFile(Path.of("docs/reference/xi - Blue Zenith/xi - Blue Zenith_4k_hd.imd"));
            ZXMap map = OsuReader.readFile(Path.of("docs/reference/LeaF - NANO DEATH!!!!!/LeaF - NANO DEATH!!!!! (nowsmart) [DEATH].osu"));
            /*System.out.println(map.unLocalizedMapInfo);
            System.out.println(ImdWriter.checkLocalizedInfos(map));*/
            ImdWriter.writeOut(map , ImdWriter.checkLocalizedInfos(map) , Path.of("G:/desktop"));
//            System.out.println(map.notes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }
}
