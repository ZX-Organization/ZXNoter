package team.zxorg.zxnoter.api.resource.item;

import java.io.InputStream;
import java.nio.file.Path;

public interface IFileResourceItem extends IResourceItem {
    Path getFilePath();

    byte[] readAllBytes();

    String readAllString();

    InputStream getInputStream();
}
