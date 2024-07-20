package team.zxorg.mapeditcore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.mapeditcore.io.ImdReader;
import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.mapElement.note.Note;

import java.io.File;
import java.io.IOException;

import static team.zxorg.mapeditcore.mapElement.MapElementSerializer.eleGson;
//import static team.zxorg.mapeditcore.mapElement.note.NoteSerializer.noteGson;

public class Main implements ExtensionEntrypoint {
    @Override
    public void onInitialize(Extension extension, ExtensionManager manager){
    }

    public static void main(String[] args) {
        try{
            ImdReader reader = new ImdReader().readFile(new File("docs/reference/Contrapasso -paradiso-/t+pazolite - Contrapasso -paradiso-_4k_hd.imd"));
//            OsuReader reader = new OsuReader().readFile(new File("docs/reference/LeaF - NANO DEATH!!!!!/LeaF - NANO DEATH!!!!! (nowsmart) [DEATH].osu"));
            ZXMap map = reader.readMap();

            eleGson.toJson(map);

            String json = eleGson.toJson(map);
            /*System.out.println(json);*/
            //System.out.println(noteGson.toJson(map));/*

            ZXMap deSerializedMap = eleGson.fromJson(json, ZXMap.class);
            System.out.println(deSerializedMap);
            //普通note也全丢,这不就行了吗

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}