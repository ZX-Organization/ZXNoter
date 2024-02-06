package team.zxorg.mapeditcore;

import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.map.mapio.ImdReader;
import team.zxorg.mapeditcore.map.mapio.MapReader;
import team.zxorg.mapeditcore.map.mapio.OsuReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Main implements ExtensionEntrypoint {
    @Override
    public void onInitialize(Extension extension, ExtensionManager manager){

        ZXMap map = null;
        try{
            //ImdReader reader = new ImdReader(Path.of(new File("docs/reference/Corruption/Corruption_4k_ez.imd").toURI()));
            OsuReader reader = new OsuReader(Path.of(new File("docs/reference/Memme -  (dawn_future) [Hard].osu").toURI()));
            Logger.info(reader.toString());
            map = reader.readMap();
        }catch (IOException e){
            e.printStackTrace();
        }

        if (map != null) {
            Logger.info("\n"+map.notes);
        }
    }


}