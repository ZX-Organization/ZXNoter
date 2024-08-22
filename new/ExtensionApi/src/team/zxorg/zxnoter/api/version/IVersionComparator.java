package team.zxorg.zxnoter.api.version;

public interface IVersionComparator {
    IVersion getVersion();

    VersionComparisonOperator getOperator();
}
