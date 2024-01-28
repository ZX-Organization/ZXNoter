package team.zxorg.zxnoter.uiframe.base;

import team.zxorg.zxnoter.uiframe.ZXNoterManager;
import team.zxorg.zxnoter.uiframe.component.ActivityItem;

public class FileManagerActivityItem extends ActivityItem {
    public FileManagerActivityItem() {
        init(ZXNoterManager.extension, "fileManager");
    }
}
