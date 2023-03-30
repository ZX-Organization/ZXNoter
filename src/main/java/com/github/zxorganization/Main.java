package com.github.zxorganization;

import com.github.zxorganization.ui.ZXNoterApp;

public class Main {
    public static void main(String[] args) {
        System.out.println(Main.class.getResource(""));
        ZXNoterApp zxNoterApp = new ZXNoterApp();
        zxNoterApp.runApp(args);
    }
}
