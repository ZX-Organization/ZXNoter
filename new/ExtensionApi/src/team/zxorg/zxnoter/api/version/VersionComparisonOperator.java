package team.zxorg.zxnoter.api.version;

public enum VersionComparisonOperator {
    /**
     * 大于
     */
    GREATER_THAN(">"),
    /**
     * 大于等于
     */
    GREATER_THAN_EQUAL(">="),
    /**
     * 等于
     */
    EQUAL("~"),
    /**
     * 小于等于
     */
    LESS_THAN_EQUAL("<="),
    /**
     * 小于
     */
    LESS_THAN("<");
    final String symbol;

    VersionComparisonOperator(String symbol) {
        this.symbol = symbol;
    }
}
