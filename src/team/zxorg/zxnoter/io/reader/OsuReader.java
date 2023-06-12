package team.zxorg.zxnoter.io.reader;

import team.zxorg.zxnoter.map.LocalizedMapInfo;
import team.zxorg.zxnoter.map.ZXMap;

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
        byte mode = -1;
        LocalizedMapInfo localizedMapInfo = new LocalizedMapInfo();
        while ((readTemp = bfReader.readLine()) != null){
            //读取谱面信息
            if (readTemp.startsWith("[")){
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

                }
                case 0->{
                    //事件处理
                    if (readTemp.startsWith("//")){
                        String keyTemp =
                                readTemp.replaceAll("/" , "").
                                        replaceAll("\\(","").
                                        replaceAll("\\)","").
                                        replaceAll(" ","");
                        System.out.println(keyTemp);
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