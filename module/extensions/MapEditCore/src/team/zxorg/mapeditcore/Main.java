package team.zxorg.mapeditcore;

import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.note.Note;

import java.util.ArrayList;

public class Main implements ExtensionEntrypoint {
    @Override
    public void onInitialize(Extension extension, ExtensionManager manager) {
        ZXMap map = new ZXMap();
        map.insertNote(
                new Note(
                        100,Note.calPosByOrbit(1,4)
                )
        );
        map.insertNote(
                new Note(
                        200,Note.calPosByOrbit(3,4)
                )
        );
        map.insertNote(
                new Note(
                        300,Note.calPosByOrbit(1,4)
                )
        );
        map.insertNote(
                new Note(
                        400,Note.calPosByOrbit(3,4)
                )
        );
        map.insertNote(
                new Note(
                        350,Note.calPosByOrbit(1,4)
                )
        );
        map.insertNote(
                new Note(
                        120,Note.calPosByOrbit(2,4)
                )
        );
        map.insertNote(
                new Note(
                        10,Note.calPosByOrbit(4,4)
                )
        );
        map.insertNote(
                new Note(
                        80,Note.calPosByOrbit(1,4)
                )
        );
        map.insertNote(
                new Note(
                        10,Note.calPosByOrbit(1,4)
                )
        );
        map.insertNote(
                new Note(
                        200,Note.calPosByOrbit(1,4)
                )
        );
        System.out.println(map.notes);
    }


}