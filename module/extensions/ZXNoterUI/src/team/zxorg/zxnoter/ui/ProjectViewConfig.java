package team.zxorg.zxnoter.ui;

import team.zxorg.extensionloader.core.ConfigData;

import java.util.LinkedHashSet;
import java.util.List;

public class ProjectViewConfig extends ConfigData {
    /**
     * 上一个打开的文件
     */
   public String previousOpenedFile;
    /**
     * 上一个打开的项目
     */
    public String previousOpenedProject;
    /**
     * 之前打开的文件
     */
    public  LinkedHashSet<String> recentlyOpenedFile;
    /**
     * 之前打开的项目
     */
    public  LinkedHashSet<String> recentlyOpenedProject;
}
