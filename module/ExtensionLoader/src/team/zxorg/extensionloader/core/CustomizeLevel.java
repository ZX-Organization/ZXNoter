package team.zxorg.extensionloader.core;

import java.util.logging.Level;
public class CustomizeLevel extends Level {
    public static final CustomizeLevel DEBUG = new CustomizeLevel("DEBUG", 200);
    protected CustomizeLevel(String name,int value) {
        super(name,value);
    }
}
