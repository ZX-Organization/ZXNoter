package team.zxorg.zxnoter;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import team.zxorg.zxnoter.info.ZXVersion;
import team.zxorg.zxnoter.config.ZXConfigManager;
import team.zxorg.zxnoter.config.sub.LastTimeStatesCfg;
import team.zxorg.zxnoter.config.sub.preference.MiniProjectCfg;
import team.zxorg.zxnoter.ui.main.ZXStage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static String[] commandLineArgs;


    public static void main(String[] args) {
        commandLineArgs = args;

        if (args != null && args.length > 0) {
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


    /**
     * 启动ZXNoter
     */
    public static void zxnStart() {
        ZXLogger.initialize();

        ZXLogger.info("- - - -  ZXNoter  - - - -");
        ZXLogger.info("Version: " + ZXVersion.VERSION + " Code: " + ZXVersion.VERSION.getVersionCode());
        switch (ZXVersion.VERSION.status()) {
            case RC -> {
                ZXLogger.warning("当前为 [内部测试版本] 请不要泄漏软件到外部");
            }
            case BETA -> {
                ZXLogger.warning("当前为 [提前预览版本] 如有问题请联系开发者");
            }
            case ALPHA -> {
                ZXLogger.warning("当前为 [早期开发版本] 请谨慎测试软件功能");
            }
            case STABLE -> {
                ZXLogger.info("当前为 [稳定发布版本] 请尽情使用");
            }
        }

        ZXLogger.info("ZXNoter启动");


        //屏蔽javafx歌姬初始化时的异常
        Logging.getJavaFXLogger().disableLogging();
        ZXLogger.info("初始化图形系统");

        ArrayList<ZXStage> zxStages = new ArrayList<>();

        PlatformImpl.startup(() -> {
            //再次开启javafx日志
            Logging.getJavaFXLogger().enableLogging();
            //初始化 (载入配置 使用资源)
            ZXLogger.info("初始化配置");
            ZXConfigManager.reload();

            //读取上一次状态
            LastTimeStatesCfg lastTimeStates = ZXConfigManager.configuration.lastTimeStates;

            if (lastTimeStates.openedProjects.isEmpty()) {
                //创建软件实例
                ZXStage zxStage = new ZXStage();
                zxStage.show();
                zxStages.add(zxStage);
            } else {
                for (MiniProjectCfg path : lastTimeStates.openedProjects) {
                    ZXLogger.info("载入上次打开项目: " + path);
                    ZXStage zxStage = new ZXStage();
                    zxStage.zxProjectManager.openProject(Path.of(path.path));
                    zxStage.show();
                    zxStages.add(zxStage);

                }
            }


            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                ZXLogger.info("关闭程序");
                for (ZXStage zxStage : zxStages) {
                    zxStage.zxProjectManager.closeProject();
                    zxStage.close();
                }
                // 在这里执行您需要在关机前执行的操作
            }));


           /* zxStage.setOpacity(0);


            PlatformImpl.startup(() -> {
                zxStage.show();
                //System.out.println("执行自定义窗口");
                //CustomWindow.createCustomWindow("ZXNoter");
                zxStage.setOpacity(1);
            });*/


        });


    }
}
