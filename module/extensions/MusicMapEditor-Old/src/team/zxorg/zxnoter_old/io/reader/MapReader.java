package team.zxorg.zxnoter_old.io.reader;

import team.zxorg.zxnoter_old.map.ZXMap;

import java.io.IOException;
import java.nio.file.Path;

public interface MapReader {
    /**
     * 获取此reader支持文件扩展名
     * @return
     */
    String getSupportFileExtension();

    /**
     * 获取此reader正在读取的文件相对路径
     * @return
     */
    Path getReadPath();

    /**
     * 读取文件Path.of()
     * @param path 文件路径
     * @return
     */
    ZXMap read(Path path) throws IOException;

    /**
     * 补全zxMap所需的反本地化信息
     * @return
     */
    void completeInfo();
}
