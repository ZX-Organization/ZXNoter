package team.zxorg.zxnoter_old;

import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.zxnoter.ui.ProjectView;
import team.zxorg.zxnoter_old.ui.ZXNApp;

import java.util.ArrayList;

public class Main implements ExtensionEntrypoint {
    @Override
    public void onLoaded(Extension extension, ExtensionManager manager) {
    }

    @Override
    public void onInitialize(Extension extension, ExtensionManager manager) {
    }

    @Override
    public void onAllInitialized(Extension extension, ExtensionManager manager) {
        //ZXNApp.run();
        //main(new String[]{});



    }

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
                } else if (!quotation.isEmpty()) {
                    if (!arg.endsWith(quotationMark)) {
                        quotation.append(" ").append(arg);
                    } else {
                        quotation.append(" ").append(arg, 0, arg.length() - 1);
                    }
                } else {
                    Main.args.add(arg);
                }
            }
            if (!quotation.isEmpty()) {
                Main.args.add(quotation.toString());
            }
        }

        /**
         * 测试UI
         */
        ZXNApp.run();

    }
}
