package team.zxorg.zxnoter.resource.pack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.type.ResourceType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class BaseResourcePack {
    /**
     * 资源子id
     */
    private final String resourceId;
    /**
     * 资源类型
     */
    private final ResourceType type;
    /**
     * 资源所属包
     */
    private final ResourcePack pack;
    /**
     * 资源信息
     */
    private final JSONObject info;
    /**
     * 资源相对目录
     */
    private final Path path;

    @Override
    public String toString() {
        return "资源[类型:" + type + " id:" + resourceId + " 所属资源包:" + pack + " 所在路径:" + path + "]";
    }

    public String getResourceFullId() {
        return (getPackId() + "." + getType().name() + "." + getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseResourcePack pack) {
            return getResourceFullId().equals(pack.getResourceFullId());
        }
        return false;
    }

    public BaseResourcePack(ResourcePack pack, ResourceType type, Path jsonPath) {
        this.type = type;
        this.pack = pack;
        this.path = jsonPath.getParent();

        try (InputStream inputStream = Files.newInputStream(jsonPath)) {
            // 读取语言资源信息
            info = JSON.parseObject(inputStream);
            resourceId = info.getString("id");
        } catch (IOException e) {
            throw new RuntimeException("LanguageResource: 语言载入失败");
        }
    }

    /**
     * 获取资源名称 (只有在资源包彻底被载入完成后才能获取)
     *
     * @return 资源名
     */
    public String getName() {
        return pack.getLanguageContent("resource-pack." + pack.getId() + "." + type.name() + "." + resourceId + ".name");
    }

    /**
     * 获取资源描述 (只有在资源包彻底被载入完成后才能获取)
     *
     * @return 资源描述
     */
    public String getDescribe() {
        return pack.getLanguageContent("resource-pack." + pack.getId() + "." + type.name() + "." + resourceId + ".describe");
    }

    /**
     * 获取资源包id
     *
     * @return 资源包id
     */
    public String getPackId() {
        return pack.getId();
    }

    /**
     * 获取资源id
     *
     * @return 资源id
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * 获取资源类型
     *
     * @return 资源类型
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * 获取所属资源包
     *
     * @return 资源包
     */
    public ResourcePack getPack() {
        return pack;
    }

    /**
     * 获取全部信息
     *
     * @return JSONObject信息
     */
    public JSONObject getInfo() {
        return info;
    }

    /**
     * 获取资源根目录
     *
     * @return 资源根目录
     */
    public Path getPath() {
        return path;
    }

    /**
     * 重载资源
     */
    public abstract void reload();
}
