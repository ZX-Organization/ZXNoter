package team.zxorg.extensionloader.core;

public enum LanguageKey {
    CONFIGURATION_LANGUAGE_CODE("configuration.languageCode"),
    SOFTWARE_NAME("software.name"),
    COMMON_UNKNOWN("common.unknown"),
    COMMON_YES("common.yes"),
    COMMON_ELLIPSIS("common.ellipsis"),
    COMMON_NO("common.no"),
    COMMON_OK("common.ok"),
    COMMON_CANCEL("common.cancel"),
    COMMON_CLOSE("common.close"),
    COMMON_LOADING("common.loading"),
    COMMON_ERROR("common.error"),
    COMMON_WARNING("common.warning"),
    COMMON_SUCCESS("common.success"),
    COMMON_INFO("common.info"),
    COMMON_CONFIRM("common.confirm"),
    COMMON_PROMPT("common.prompt"),
    COMMON_SELECT("common.select"),
    COMMON_VERSION_ALPHA("common.version.alpha"),
    COMMON_VERSION_BETA("common.version.beta"),
    COMMON_VERSION_RC("common.version.rc"),
    COMMON_VERSION_RELEASE("common.version.release"),
    MESSAGE_IMAGE_INITIALIZE("message.image.initialize"),
    MESSAGE_IMAGE_LOADING("message.image.loading"),
    MESSAGE_RESOURCE_NOT_FOUND("message.resource.notFound"),
    MESSAGE_RESOURCE_PACK_INITIALIZE("message.resourcePack.initialize"),
    MESSAGE_RESOURCE_PACK_LOST("message.resourcePack.lost"),
    MESSAGE_RESOURCE_PACK_LOADING("message.resourcePack.loading"),
    MESSAGE_RESOURCE_PACK_RELOADED("message.resourcePack.reloaded"),
    MESSAGE_RESOURCE_PACK_NOT_FOUND("message.resourcePack.notFound"),
    MESSAGE_RESOURCE_PACK_LOAD_FAILED("message.resourcePack.loadFailed"),
    MESSAGE_RESOURCE_PACK_APPLICATION("message.resourcePack.application"),
    MESSAGE_RESOURCE_PACK_NO_NAME("message.resourcePack.noName"),
    MESSAGE_RESOURCE_PACK_NO_DESCRIPTION("message.resourcePack.noDescription"),
    MESSAGE_RESOURCE_PACK_LOST_INFO("message.resourcePack.lostInfo"),
    MESSAGE_LANGUAGE_LOST("message.language.lost"),
    MESSAGE_LANGUAGE_NO_CODE("message.language.noCode"),
    MESSAGE_LOGGER_INITIALIZE("message.logger.initialize"),
    MESSAGE_LOGGER_ERROR("message.logger.error"),
    MESSAGE_VERSION_ALPHA("message.version.alpha"),
    MESSAGE_VERSION_BETA("message.version.beta"),
    MESSAGE_VERSION_RC("message.version.rc"),
    MESSAGE_VERSION_RELEASE("message.version.release"),
    MESSAGE_VERSION_TIP("message.version.tip"),
    MESSAGE_EXTENSION_LOADING("message.extension.loading"),
    MESSAGE_EXTENSION_LOADED("message.extension.loaded"),
    MESSAGE_EXTENSION_ERROR_CANT_LOAD("message.extension.error.cantLoad"),
    MESSAGE_EXTENSION_ERROR_URL("message.extension.error.url"),
    MESSAGE_EXTENSION_ERROR_DEPEND_API_NOT_COMPATIBLE("message.extension.error.dependApiNotCompatible"),
    MESSAGE_EXTENSION_ERROR_PLATFORM_NOT_SUPPORTED("message.extension.error.platformNotSupported"),
    MESSAGE_EXTENSION_ERROR_ID_CONFLICT("message.extension.error.idConflict"),
    MESSAGE_EXTENSION_ERROR_INFO_READ_FAILED("message.extension.error.infoReadFailed"),
    MESSAGE_EXTENSION_ERROR_DEPEND_EXTENSION_LOST("message.extension.error.dependExtensionLost"),
    MESSAGE_EXTENSION_ERROR_DEPEND_EXTENSION_NOT_COMPATIBLE("message.extension.error.dependExtensionNotCompatible"),
    MESSAGE_EXTENSION_ERROR_ENTRYPOINT_NOT_FOUND("message.extension.error.entrypointNotFound"),
    MESSAGE_EXTENSION_ERROR_ENTRYPOINT_NOT_IMPLEMENTED("message.extension.error.entrypointNotImplemented"),
    MESSAGE_EXTENSION_ERROR_ENTRYPOINT_INSTANCE_FAILED("message.extension.error.entrypointInstanceFailed");

    private final String key;

    LanguageKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
