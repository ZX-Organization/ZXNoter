package team.zxorg.zxnoter;

import team.zxorg.zxnoter.ui.ZXNApp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static ArrayList<String> args = new ArrayList<>();
    public static String quotationMark = "\"";//引号标识符 (不同系统不一样 真的太裤辣！)

    public static void main(String[] args) {

        String osName = System.getProperty("os.name");
        if (osName.contains("linux"))
            quotationMark = "'";


        {
            StringBuilder quotation = new StringBuilder();
            for (String arg : args) {
                if (arg.startsWith(quotationMark)) {
                    quotation.append(arg.substring(1));
                } else if (quotation.length() > 0) {
                    if (!arg.endsWith(quotationMark)) {
                        quotation.append(" ").append(arg);
                    } else {
                        quotation.append(" ").append(arg, 0, arg.length() - 1);
                    }
                } else {
                    Main.args.add(arg);
                }
            }
            if (quotation.length() > 0) {
                Main.args.add(quotation.toString());
            }
        }

        /**
         * 测试UI
         */
        ZXNApp.run();


    }
}
