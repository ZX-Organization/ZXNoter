package team.zxorg.zxnoter.ui;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class CustomWindow {
    public static final JNACustomWindowDLL instanceDll;

    static {
        System.setProperty("jna.library.path", "D:\\Project\\BetterWindow\\cmake-build-release");
        instanceDll = Native.loadLibrary("libBetterWindow.dll", JNACustomWindowDLL.class);
    }

    public interface JNACustomWindowDLL extends Library {
        public int CW_CreateWindow(String title);

        public void CW_NCMouseMove(int id,char ncPos, char state);
    }


    public static int createCustomWindow(String title) {
        return instanceDll.CW_CreateWindow(title);
    }


}
