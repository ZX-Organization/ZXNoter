package team.zxorg.zxnoter.io;

import team.zxorg.zxnoter.io.reader.ImdReader;
import team.zxorg.zxnoter.io.reader.OsuReader;
import team.zxorg.zxnoter.io.writer.ImdWriter;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;

import java.io.IOException;
import java.nio.file.Path;

public class IoTest {
    public static void main(String[] args) {
        try {
            ZXMap map = ImdReader.readFile(Path.of("docs/reference/xi - Blue Zenith/xi - Blue Zenith_4k_hd.imd"));
            //ZXMap map = OsuReader.readFile(Path.of("docs/reference/LeaF - NANO DEATH!!!!!/LeaF - NANO DEATH!!!!! (nowsmart) [DEATH].osu"));
            //ImdWriter.writeOut(map , Path.of("G:\\desktop"));
            System.out.println(map.notes.get(19));
            map.modifyComplexNote(
                    (ComplexNote)map.notes.get(19),
                    ((ComplexNote)map.notes.get(19)).notes.get(0),
                    100
            );

            map.modifyComplexNote(
                    (ComplexNote)map.notes.get(19),
                    ((ComplexNote)map.notes.get(19)).notes.get(1),
                    -2
            );
            System.out.println(map.notes.get(19));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
