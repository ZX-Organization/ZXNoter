package team.zxorg.mapeditcore.io.reader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import team.zxorg.extensionloader.gson.GsonManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class McReader extends MapReader{
    @Override
    public McReader readFile(File file) throws IOException {
        this.file = file;
        JsonElement json = JsonParser.parseReader(new FileReader(file));
        System.out.println(json);
        return this;
    }

    @Override
    public <T> T readMeta() throws IOException {
        return null;
    }

    @Override
    public String getSuffix() {
        return ".mc";
    }
}
