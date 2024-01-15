package team.zxorg.zxnoter.util;

import com.alibaba.fastjson2.JSONArray;
import team.zxorg.zxnoter.info.map.ZXMInfo;
import team.zxorg.zxnoter.io.reader.OsuReader;
import team.zxorg.zxnoter.map.ZXMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class AAAA {
    static int index = 0;

    public static void main(String[] args) throws IOException {
        Path mapsPath = Path.of("E:\\music");
        Path musicOutPath = Path.of("E:\\训练集");

        Files.list(mapsPath).forEach(path -> {
            try {
                Files.find(path, Integer.MAX_VALUE, (p, a) -> p.toString().contains("[Normal]")).forEach(path1 -> {
                    OsuReader osuReader = new OsuReader(path1);
                    ZXMap zxMap = null;
                    try {
                        zxMap = osuReader.read();
                    } catch (Exception e) {
                        return;
                    }

                    Path audio = path.resolve(zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.AudioPath));
                    if (Files.exists(audio)) {//分析所有谱面
                        System.out.println("解析: " + zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.Title));
                        try {
                            Path audioPath = musicOutPath.resolve(zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.Title) + ".mp3");
                            if (!Files.exists(audioPath))
                                Files.copy(audio, audioPath);
                            Path jsonPath = musicOutPath.resolve(zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.Title) + ".json");
                            JSONArray json = Pytorch.a(zxMap);
                            if (Files.exists(jsonPath))
                                Files.delete(jsonPath);
                            Files.writeString(jsonPath, json.toJSONString());
                        } catch (Exception e) {
                        }
                    }
                    index++;

                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }
}
