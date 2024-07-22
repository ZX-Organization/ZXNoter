package team.zxorg.mapeditcore;

import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.mapeditcore.io.reader.ImdReader;
import team.zxorg.mapeditcore.io.reader.McReader;
import team.zxorg.mapeditcore.io.reader.OsuReader;
import team.zxorg.mapeditcore.io.writer.ImdWriter;
import team.zxorg.mapeditcore.io.writer.MapWriter;
import team.zxorg.mapeditcore.io.writer.OsuWriter;
import team.zxorg.mapeditcore.io.writer.ZXMapWriter;
import team.zxorg.mapeditcore.map.ZXMap;

import java.io.File;
import java.io.IOException;

import static team.zxorg.mapeditcore.io.writer.ZXMapWriter.ioGson;


public class Main implements ExtensionEntrypoint {
    @Override
    public void onInitialize(Extension extension, ExtensionManager manager){
    }

    public static void main(String[] args) {
        try{
//            ImdReader reader = new ImdReader().readFile(new File("docs/reference/Contrapasso -paradiso-/t+pazolite - Contrapasso -paradiso-_4k_hd.imd"));
//            ImdReader reader = new ImdReader().readFile(new File("t+pazolite - Contrapasso -paradiso-.imd"));
//            OsuReader reader = new OsuReader().readFile(new File("docs/reference/LeaF - NANO DEATH!!!!!/LeaF - NANO DEATH!!!!! (nowsmart) [DEATH].osu"));
            McReader reader = new McReader().readFile(new File("docs/reference/0/1556074008.mc"));
            ZXMap map = reader.readMap();

            ImdWriter writer = new ImdWriter(map);
            //writer.setDirectory(new File("/Users/2333xiang/IdeaProjects/ZXNoter")).writeFile();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}