package team.zxorg.extensionloader.core;

import team.zxorg.extensionloader.gson.GsonManager;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源包对象
 */
public class ResourcePack {

    private static class ResourcePackInfo {
        /**
         * 资源包的名称
         */
        private Language name;
        /**
         * 资源包的版本
         */

        private Version version;
        /**
         * 资源包的描述
         */
        private Language description;
        /**
         * 扩展标签
         */
        List<Language> tags;
        /**
         * 制作者
         */
        List<Language> author;
        /**
         * 资源包图标
         */
        Path icon;

        public ResourcePackInfo() {
            name = new Language("message.resource.noName");
            description = new Language("message.resource.noDescription");
            tags = new ArrayList<>();
            author = new ArrayList<>();
            icon = Path.of("");
            version = new Version("0.0.0");
        }
    }

    private ResourcePackInfo info;

    /**
     * 获取资源包作者
     *
     * @return 作者
     */
    public List<Language> getAuthor() {
        return info.author;
    }

    /**
     * 获取资源包标签
     *
     * @return 标签
     */
    public List<Language> getTags() {
        return info.tags;
    }

    /**
     * 获取资源包版本
     *
     * @return 版本
     */
    public Version getVersion() {
        return info.version;
    }

    /**
     * 获取资源包描述
     *
     * @return 描述
     */
    public Language getDescription() {
        return info.description;
    }

    /**
     * 获取资源包名称
     *
     * @return 名称
     */
    public Language getName() {
        return info.name;
    }

    /**
     * 获取资源包图标
     *
     * @return 图标路径，需要配合getPath()去读取
     */
    public Path getIcon() {
        return info.icon;
    }

    private FileSystem zipFileSystem;

    /**
     * 获取资源包路径
     *
     * @return 资源包路径
     */
    public Path getPath() {
        return path;
    }

    /**
     * 资源包路径 (在zip文件内)
     */
    private final Path path;

    public ResourcePack(Path path) {
        if (!Files.exists(path))
            throw new IllegalStateException(Language.get("message.resource.notFound", path));
        if (!Files.isDirectory(path)) {
            try {
                zipFileSystem = createZipFileSystem(path);
                path = zipFileSystem.getPath("/");
            } catch (IOException e) {
                throw new RuntimeException(Language.get("message.resource.loadFailed", path, e));
            }
        }
        this.path = path;
        // 读取资源包的配置文件，获取资源包的名称、版本和描述等信息
        info = GsonManager.fromJson(path.resolve("pack.json5"), ResourcePackInfo.class);
        if (info == null) {
            throw new RuntimeException(Language.get("message.resource.lostInfo", path));
        }
    }

    /**
     * 关闭资源包
     */
    public void close() {
        if (zipFileSystem != null)
            try {
                zipFileSystem.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    /**
     * 通过zip文件创建文件系统
     *
     * @param zipFilePath zip文件路径
     * @return 文件系统
     */
    private static FileSystem createZipFileSystem(Path zipFilePath) throws IOException {
        URI uri = URI.create("jar:file:" + zipFilePath.toUri().getPath());
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        return FileSystems.newFileSystem(uri, env);
    }

}