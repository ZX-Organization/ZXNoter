package team.zxorg.zxnoter.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.io.reader.OsuReader;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Pytorch {
    public static void main(String[] args) throws IOException {
        Path map = Path.of("E:\\music\\1173144 beatMARIO - Night of Knights (Cranky Remix)\\beatMARIO - Night of Knights (Cranky Remix) (FAMoss) [Easy].osu");

    }

    public static JSONArray a(ZXMap zxMap) throws IOException {
        JSONArray info = new JSONArray();


        while (zxMap.notes.size() > 80) {
            zxMap.notes.remove(new Random().nextInt(zxMap.notes.size()));
        }

        BaseNote lastNote = null;
        //记录记录过的键
        Set<Long> timeSet = new HashSet<>();
        //遍历所有键
        for (BaseNote note : zxMap.notes) {
            if (note.timeStamp < 4000) {
                continue;
            }
            //检查是否意见被记录过了
            if (!timeSet.contains(note.timeStamp)) {
                timeSet.add(note.timeStamp);
                JSONObject aNote = new JSONObject();
                aNote.put("time", note.timeStamp / 1000d);
                aNote.put("label", 1);
                info.add(aNote);

                for (int i = 1; i < 6; i++) {
                    {
                        JSONObject notANote = new JSONObject();
                        notANote.put("time", (note.timeStamp - i * 20) / 1000d);
                        notANote.put("label", 0);
                        info.add(notANote);
                    }
                    {
                        JSONObject notANote = new JSONObject();
                        notANote.put("time", (note.timeStamp + i * 20) / 1000d);
                        notANote.put("label", 0);
                        info.add(notANote);
                    }
                    {
                        JSONObject notANote = new JSONObject();
                        notANote.put("time", (note.timeStamp - i * 2) / 1000d);
                        notANote.put("label", 0);
                        info.add(notANote);
                    }
                    {
                        JSONObject notANote = new JSONObject();
                        notANote.put("time", (note.timeStamp + i * 2) / 1000d);
                        notANote.put("label", 0);
                        info.add(notANote);
                    }
                }


                if (lastNote != null) {

                    JSONObject notANote = new JSONObject();
                    notANote.put("time", (lastNote.timeStamp + (note.timeStamp - lastNote.timeStamp) / 2) / 1000d);
                    notANote.put("label", 0);
                    info.add(notANote);
                }

                lastNote = note;//更新之前一个键
            }
        }
        /*while (info.size() > 2000) {
            info.remove(new Random().nextInt(info.size()));
        }*/

        return info;
        //Files.writeString(map.getParent().resolve(map.getFileName() + ".json"), info.toJSONString());
    }
}
