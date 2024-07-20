package team.zxorg.mapeditcore;

import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.mapeditcore.io.reader.ImdReader;
import team.zxorg.mapeditcore.io.writer.ImdWriter;
import team.zxorg.mapeditcore.io.writer.MapWriter;
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
            ImdReader reader = new ImdReader().readFile(new File("docs/reference/Contrapasso -paradiso-/t+pazolite - Contrapasso -paradiso-_4k_hd.imd"));
//            OsuReader reader = new OsuReader().readFile(new File("docs/reference/LeaF - NANO DEATH!!!!!/LeaF - NANO DEATH!!!!! (nowsmart) [DEATH].osu"));
            ZXMap map = reader.readMap();

            MapWriter writer = new ImdWriter(map);
            writer.writeFile(new File(map.metaData.getTitleUnicode() + writer.getSuffix()));
            //普通note也全丢,这不就行了吗

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}