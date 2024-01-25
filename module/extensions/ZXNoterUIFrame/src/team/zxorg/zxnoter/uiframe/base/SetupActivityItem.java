package team.zxorg.zxnoter.uiframe.base;

import team.zxorg.zxnoter.uiframe.ZXNoterManager;
import team.zxorg.zxnoter.uiframe.component.ActivityItem;

public class SetupActivityItem extends ActivityItem {
    public SetupActivityItem() {
        init(ZXNoterManager.extension, "setup", "system.settings-4");
    }
}
