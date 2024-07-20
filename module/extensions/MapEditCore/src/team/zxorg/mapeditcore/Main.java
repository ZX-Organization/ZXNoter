package team.zxorg.mapeditcore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.mapeditcore.io.ImdReader;
import team.zxorg.mapeditcore.io.MapReader;
import team.zxorg.mapeditcore.io.OsuReader;
import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.mapElement.note.MixNote;
import team.zxorg.mapeditcore.mapElement.note.Note;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Main implements ExtensionEntrypoint {
    @Override
    public void onInitialize(Extension extension, ExtensionManager manager){
    }

    public static void main(String[] args) {
        try{
            ImdReader reader = new ImdReader().readFile(new File("docs/reference/Contrapasso -paradiso-/t+pazolite - Contrapasso -paradiso-_4k_hd.imd"));
//            OsuReader reader = new OsuReader().readFile(new File("docs/reference/LeaF - NANO DEATH!!!!!/LeaF - NANO DEATH!!!!! (nowsmart) [DEATH].osu"));
            ZXMap map = reader.readMap();/*
            System.out.println(new Gson().toJson(map));*/
            Gson gson = new GsonBuilder().registerTypeAdapter(Note.class, new Note()).setPrettyPrinting().create();
            System.out.println(gson.toJson(map));/*
            ZXMap deSerializedMap = gson.fromJson(gson.toJson(map).toString(), ZXMap.class);*/
            //System.out.println(deSerializedMap);全丢了

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}