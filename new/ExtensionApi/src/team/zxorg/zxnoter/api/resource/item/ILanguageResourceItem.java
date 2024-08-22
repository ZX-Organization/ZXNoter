package team.zxorg.zxnoter.api.resource.item;


import team.zxorg.zxnoter.api.language.ILanguageBundle;

public interface ILanguageResourceItem extends IResourceItem{
    String format(Object... args);

    /**
     * 获取来源包
     *
     * @return 语言包
     */
    ILanguageBundle getLanguagePack();
}
