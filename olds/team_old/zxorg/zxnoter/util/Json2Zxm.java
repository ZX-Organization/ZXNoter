package team.zxorg.zxnoter.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.io.reader.OsuReader;
import team.zxorg.zxnoter.io.writer.OsuWriter;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Json2Zxm {
    public static void main(String[] args) throws IOException, NoSuchFieldException {
        String file = "E:\\训练集\\测试\\No title";
        Path map = Path.of(file + ".osu");
        JSONArray jsonArray = JSONArray.parse(Files.readString(Path.of(file + "_rhythm.json")));
        OsuReader reader = new OsuReader(map);
        ZXMap zxMap = reader.read();
        zxMap.notes.clear();


        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject rhythm = jsonArray.getJSONObject(i);
            long time = (long) (rhythm.getDoubleValue("start_time") * 1000);
            zxMap.notes.add(new FixedOrbitNote(time, 0));
        }

        OsuWriter osuWriter = new OsuWriter();

        osuWriter.writeOut(zxMap, map.getParent().resolve(map.getFileName() + "_rhythm.osu"));
    }
}
