package team.zxorg.zxnoter.api.extension;

import java.util.List;

public interface IExtensionManager {
    /**
     * 获取扩展列表
     *
     * @return 扩展列表
     */
    List<IExtension> getExtensions();

    /**
     * 获取扩展
     *
     * @param id 扩展id
     * @return 扩展
     */
    IExtension getExtension(String id);
}
