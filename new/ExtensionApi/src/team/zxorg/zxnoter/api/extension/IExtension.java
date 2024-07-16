package team.zxorg.zxnoter.api.extension;

import java.util.List;

public interface IExtension {
    /**
     * 获取扩展id
     *
     * @return 扩展id
     */
    String getId();

    /**
     * 获取依赖列表
     * @return 依赖的扩展列表
     */
    List<IExtension> getDependencies();

}
