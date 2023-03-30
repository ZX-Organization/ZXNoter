package com.github.zxorganization.sound.audiomixer;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        AudioMixer audioMixer = new AudioMixer(48000, 1024);
        int handle = audioMixer.addAudio(new File("Hikaru Station.wav"));


        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            AudioChannel audioChannel = audioMixer.createChannel(handle);
            audioChannel.setVolume(0.2f);
            audioChannel.setPlaySpeed(true, 1 + 0.12f * i);
            audioChannel.play();
            audioChannel.setEndBehavior(AudioChannel.EndBehavior.CLOSE);
        }


    }
}
