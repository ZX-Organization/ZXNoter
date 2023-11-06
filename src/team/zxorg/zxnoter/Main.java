package team.zxorg.zxnoter;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import team.zxorg.zxnoter.info.ZXVersion;
import team.zxorg.zxnoter.resource.ZXConfiguration;
import team.zxorg.zxnoter.ui.main.ZXStage;

import java.util.Random;

public class Main {
    public static String[] commandLineArgs;


    public static void main(String[] args) {
        commandLineArgs = args;

        if (args != null) {
            switch (args[0]) {
                case "-random" -> {
                    String[] something = new String[args.length - 2];
                    System.arraycopy(args, 2, something, 0, something.length);
                    int randomCounts = Integer.parseInt(args[1]);
                    randomDoSomething(randomCounts, something);
                }
                case "-version" -> {
                    System.out.println("ZXNoter " + ZXVersion.VERSION);
                }
                case "-help" -> {
                    System.out.println("ZXNoter " + ZXVersion.VERSION);
                    System.out.println("-random [randomNum] [something]... 随机做些事");
                    System.out.println("-version 显示版本号");
                    System.out.println("-help 显示帮助");
                }
                default -> {
                    zxnStart();
                }
            }
            return;
        }
        zxnStart();

    }

    /**
     * 随机做些事函数
     *
     * @param randomCounts 随机次数
     * @param something    需要随机排序的数组
     */
    public static void randomDoSomething(int randomCounts, String[] something) {
        int count = 0;
        while (true) {
            count++;
            boolean found = true;
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("< 第 ").append(count).append(" 轮 > 👉 ");
            int firstRandomValue = new Random().nextInt(something.length);
            stringBuilder.append(something[firstRandomValue]);
            for (int i = 0; i < randomCounts - 1; i++) {
                int nextRandomValue = new Random().nextInt(something.length);
                stringBuilder.append(" ").append(something[nextRandomValue]);
                if (firstRandomValue != nextRandomValue) {
                    found = false;
                }
            }


            if (found) {
                System.out.println(stringBuilder + " ✔️正确的😋");
                break;
            } else {
                System.out.println(stringBuilder + " ❌不正确😅");
            }
        }
    }


    //创建软件实例
    static ZXStage zxStage;

    /**
     * 启动ZXNoter
     */
    public static void zxnStart() {
        ZXLogger.initialize();


        ZXLogger.info("ZXNoter启动");


        //屏蔽javafx歌姬初始化时的异常
        Logging.getJavaFXLogger().disableLogging();
        ZXLogger.info("初始化图形系统");


        PlatformImpl.startup(() -> {
            //再次开启javafx日志
            Logging.getJavaFXLogger().enableLogging();
            //初始化 (载入配置 使用资源)
            ZXLogger.info("初始化配置");
            ZXConfiguration.reload();

            zxStage = new ZXStage();

            ZXLogger.info("显示ZXN-UI窗口");
            zxStage.setOpacity(0);


            PlatformImpl.startup(() -> {
                zxStage.show();
                //System.out.println("执行自定义窗口");
                //CustomWindow.createCustomWindow("ZXNoter");
                zxStage.setOpacity(1);
            });
        });


    }
}
