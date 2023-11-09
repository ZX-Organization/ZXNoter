package team.zxorg.zxnoter.config.configuration.sub.preference.res;

import team.zxorg.zxnoter.resource.ResourceType;

import java.util.ArrayList;

/**
 * 使用的资源包 记录资源包的标识路径如 'zxnoter.icon.line'
 */
public class ResourcesPackCfg {
    /**
     * 语言资源包
     */
    public ArrayList<String> languages;
    /**
     * 音效资源包
     */
    public ArrayList<String> sounds;
    /**
     * 图标资源包
     */
    public ArrayList<String> icons;
    /**
     * 样式资源包
     */
    public ArrayList<String> styles;
    /**
     * 布局资源包
     */
    public ArrayList<String> layouts;
    /**
     * 主题资源包
     */
    public ArrayList<String> themes;
    public ArrayList<String> getTypeResourcePacks(ResourceType resourceType){
        return switch (resourceType){
            case theme -> themes;
            case icon -> icons;
            case language -> languages;
            case layout -> layouts;
            case sound -> sounds;
            case style -> styles;
        };
    }
}
