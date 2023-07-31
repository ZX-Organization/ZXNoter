package team.zxorg.zxnoter;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import team.zxorg.zxnoter.resource.ZXConfiguration;
import team.zxorg.zxnoter.ui.main.ZXStage;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class Main {
    public static String[] commandLineArgs;


    public static void main(String[] args) {
        ZXLogger.info("ZXNoter启动");
        commandLineArgs = args;

        if (args != null) {
            if ("-random".equals(args[0])) {
                String[] something = new String[args.length - 2];
                System.arraycopy(args, 2, something, 0, something.length);
                int randomCounts = Integer.parseInt(args[1]);
                int count = 0;
                while (true) {
                    count++;
                    boolean found = true;

                    System.out.print("< 第 " + count + " 轮 > 👇");
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    int firstRandomValue = new Random().nextInt(something.length);
                    System.out.print(something[firstRandomValue]);
                    for (int i = 0; i < randomCounts - 1; i++) {
                        int nextRandomValue = new Random().nextInt(something.length);
                        System.out.print(" " + something[nextRandomValue]);
                        if (firstRandomValue != nextRandomValue) {
                            found = false;
                        }
                    }


                    if (found) {
                        System.out.println(" ✔️正确😋");
                        break;
                    } else {
                        System.out.println(" ❌不正确😅");
                    }
                }


                //ZXLogger.info("马上干什么: " + new Random().nextInt(4) + " " + new Random().nextInt(2));
                return;
            }
        }
        {
            ZXLogger.info("初始化图形系统");


            ZXLogger.info("系统语言代码 " + Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());


            //屏蔽javafx歌姬初始化时的异常
            Logging.getJavaFXLogger().disableLogging();

            PlatformImpl.startup(() -> {
                //再次开启javafx日志
                Logging.getJavaFXLogger().enableLogging();
                //初始化 (载入配置 使用资源)
                ZXLogger.info("初始化配置");
                ZXConfiguration.reload();

                //创建软件实例
                ZXStage zxStage = new ZXStage();
                ZXLogger.info("显示ZXN-UI窗口");
                zxStage.show();

                //创建软件实例
                ZXStage zxStage2 = new ZXStage();
                ZXLogger.info("显示ZXN-UI窗口");
                zxStage2.show();
            });


        }

    }
}
