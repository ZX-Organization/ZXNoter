package team.zxorg.audiosystem;

import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;

import javax.sound.sampled.AudioSystem;

public class Entrypoint implements ExtensionEntrypoint {
    @Override
    public void onLoaded(Extension extension, ExtensionManager manager) {

    }

    @Override
    public void onInitialize(Extension extension, ExtensionManager manager) {
    }

    @Override
    public void onAllInitialized(Extension extension, ExtensionManager manager) {
        /*try {
            Main.main(new String[]{});
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }*/
    }
}
