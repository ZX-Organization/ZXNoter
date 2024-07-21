package team.zxorg.mapeditcore.io.writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.map.mapdata.IBaseData;
import team.zxorg.mapeditcore.map.mapdata.MapDataSerializer;
import team.zxorg.mapeditcore.mapElement.IMapElement;
import team.zxorg.mapeditcore.mapElement.MapElementSerializer;

import java.io.*;

public abstract class MapWriter {
    protected File directory;
    protected ZXMap map;

    public MapWriter(ZXMap map) {
        this.map = map;
    }

    /**
     * 保存文件
     */
    public void writeFile() throws IOException {}

    /**
     * 获取保存文件后缀
     */
    public abstract String getSuffix();

    /**
     * 获取实际保存的文件目录
     */
    public abstract File getPath();

    /**
     * 获取保存路径
     */
    public File getDirectory(){
        return directory;
    };

    /**
     * 设置保存为(可以放目录，文件)
     */
    public abstract  <T>T setDirectory(File directory);

}
