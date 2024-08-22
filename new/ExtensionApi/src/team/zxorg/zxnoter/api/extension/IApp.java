package team.zxorg.zxnoter.api.extension;

import team.zxorg.zxnoter.api.language.ILanguageManager;
import team.zxorg.zxnoter.api.resource.IResourceManager;

public interface IApp {
    IExtensionManager getExtensionManager();

    IResourceManager getResourceManager();

    ILanguageManager getLanguageManager();
}
