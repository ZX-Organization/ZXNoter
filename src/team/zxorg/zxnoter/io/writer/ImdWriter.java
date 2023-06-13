package team.zxorg.zxnoter.io.writer;

import team.zxorg.zxnoter.map.LocalizedMapInfo;
import team.zxorg.zxnoter.map.ZXMap;

import java.nio.file.Path;
import java.util.Set;

public class ImdWriter {
    public static void writeOut(ZXMap zxMap, Path path){
        LocalizedMapInfo localizedMapInfo = zxMap.localizedMapInfo;
        Set<String> keyset = localizedMapInfo.allInfo.keySet();
        for (String key : keyset){
            System.out.println(key);
        }
    }
}
