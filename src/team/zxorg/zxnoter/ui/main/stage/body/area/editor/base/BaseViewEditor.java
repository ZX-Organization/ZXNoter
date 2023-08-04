package team.zxorg.zxnoter.ui.main.stage.body.area.editor.base;

import team.zxorg.zxnoter.resource.project.ZXProject;

public abstract class BaseViewEditor extends BaseTab {
    public BaseViewEditor(ZXProject zxProject) {
        super(zxProject);
    }

    @Override
    protected boolean closeRequest() {
        return true;
    }
}
