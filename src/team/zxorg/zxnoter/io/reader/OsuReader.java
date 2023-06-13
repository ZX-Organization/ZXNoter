package team.zxorg.zxnoter.io.reader;

import team.zxorg.zxnoter.map.LocalizedMapInfo;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.mapInfos.OsuInfos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

/**
 * osu读取器(osu!mania)
 */
public class OsuReader {
    public static ZXMap readFile(Path path) throws IOException {
        BufferedReader bfReader = new BufferedReader(new FileReader(path.toFile()));
        String readTemp;
        //读取模式[General]等
        byte mode = -1;
        //是否进入事件值读取模式
        boolean eventValueMode = false;
        //事件缓存
        String eventNameTemp = "",eventValueTemp = "";

        LocalizedMapInfo localizedMapInfo = new LocalizedMapInfo();
        readTemp = bfReader.readLine();

        localizedMapInfo.addInfo(
                OsuInfos.valueOf(readTemp.substring(0 , readTemp.lastIndexOf("v")).replaceAll(" ","")).unLocalize(),
                readTemp.substring(readTemp.lastIndexOf("v"))
        );
        while ((readTemp = bfReader.readLine()) != null){
            //读取谱面信息
            if (readTemp.startsWith("[")){
                //处理事件属性末尾未定义情况
                if (eventValueMode){
                    localizedMapInfo.addInfo(eventNameTemp , "");
                }
                //设置读取模式
                if ("[Events]".equals(readTemp)){
                    mode = 0;
                    continue;
                }
                if ("[TimingPoints]".equals(readTemp)){
                    mode = 1;
                    continue;
                }
                if ("[HitObjects]".equals(readTemp)){
                    mode = 2;
                    continue;
                }
                mode = -1;
                continue;
            }
            //按照设置的读取模式读取信息
            if ("".equals(readTemp)){
                //跳过空行
                continue;
            }
            switch (mode){
                case -1->{
                    //带冒号属性
                    localizedMapInfo.addInfo(
                            readTemp.substring(0 , readTemp.indexOf(":")) ,
                            readTemp.substring(readTemp.lastIndexOf(":") + 1)
                    );
                }
                case 0->{
                    //事件处理
                    String key = "";
                    if (readTemp.startsWith("//")){
                        key = readTemp.replaceAll("/", "").replaceAll("\\(", "").
                                replaceAll("\\)", "").
                                replaceAll(" ", "");
                    }
                    if (eventValueMode){
                        //事件值读取模式
                        if (readTemp.startsWith("//")){
                            //值读取模式又一次读到事件名
                            localizedMapInfo.addInfo(eventNameTemp , "");
                            eventNameTemp = OsuInfos.valueOf(key).unLocalize();
                        }else {
                            //值读取
                            localizedMapInfo.addInfo(eventNameTemp , readTemp);
                            eventValueMode = false;
                        }
                    }else {
                        if (readTemp.startsWith("//")){
                            eventValueMode = true;
                            eventNameTemp = OsuInfos.valueOf(key).unLocalize();
                        }
                    }
                    continue;
                }
                case 1->{
                    //时间点处理

                    continue;
                }
                case 2->{
                    //物件处理
                    readTemp.length();
                    continue;
                }
            }

        }
        System.out.println(localizedMapInfo);
        ZXMap zxMap = new ZXMap();

        return zxMap;
    }
    private static void modeSelect(String read){

    }

    public static void main(String[] args) {
        try {
            readFile(Path.of("docs/reference/LeaF - NANO DEATH!!!!!/LeaF - NANO DEATH!!!!! (nowsmart) [DEATH].osu"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}