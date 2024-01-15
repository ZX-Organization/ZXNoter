package team.zxorg.ui;

public class TimeUtils {
    /**
     * 将时间转为格式化时间字符串
     * @param timeInMillis 时间ms
     * @return 格式化时间
     */
    public static String formatTime(long timeInMillis) {
        long hours = timeInMillis / (60 * 60 * 1000);
        long minutes = (timeInMillis % (60 * 60 * 1000)) / (60 * 1000);
        long seconds = (timeInMillis % (60 * 1000)) / 1000;
        long milliseconds = timeInMillis % 1000;

        StringBuilder formattedTime = new StringBuilder();
        if (hours > 0) {
            formattedTime.append(hours).append(":");
        }
        if (minutes > 0 || hours > 0) {
            formattedTime.append(String.format("%02d", minutes)).append(":");
        }
        formattedTime.append(String.format("%02d", seconds)).append(".");
        formattedTime.append(String.format("%03d", milliseconds));
        return formattedTime.toString();
    }

    /**
     * 字符串解析到时间
     *
     * @param timeString 格式化时间字符串 h:mm:ss.ttt
     * @return 时间ms
     */
    public static long parseTime(String timeString) {
        //检查timeString的分隔符号 在前面补全
        String[] parts = timeString.split(":");
        if (parts.length == 1)
            parts = ("0:0:" + timeString).split(":");
        if (parts.length == 2)
            parts = ("0:" + timeString).split(":");

        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        // 分割秒和毫秒部分
        String[] secondsAndMilliseconds = parts[2].split("\\.");
        if (secondsAndMilliseconds.length != 2) {
            secondsAndMilliseconds = (parts[2] + ".0").split("\\.");
        }

        int seconds = Integer.parseInt(secondsAndMilliseconds[0]);
        int milliseconds = Integer.parseInt(secondsAndMilliseconds[1]);

        // 检查毫秒部分的长度，如果小于等于2则不做补0处理
        milliseconds *= Math.pow(10, 3 - secondsAndMilliseconds[1].length());

        return ((long) hours * 60 * 60 * 1000) + ((long) minutes * 60 * 1000) + (seconds * 1000L) + milliseconds;
    }
}
