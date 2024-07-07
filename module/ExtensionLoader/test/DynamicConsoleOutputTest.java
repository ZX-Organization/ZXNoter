import team.zxorg.extensionloader.util.ANSICode;
import team.zxorg.extensionloader.util.DynamicConsoleOutput;

public class DynamicConsoleOutputTest {
    public static void main(String[] args) throws InterruptedException {
        DynamicConsoleOutput output = new DynamicConsoleOutput();
        output.appendString("Hello, ");
        output.update();
        Thread.sleep(1000);

        output.appendString("world!");
        output.update();
        Thread.sleep(1000);

        output.setColor(ANSICode.RED);
        output.appendString("This is a red text.");
        output.resetColor();
        output.update();
        Thread.sleep(1000);

        output.deleteLastChar();
        output.update();
    }

}
