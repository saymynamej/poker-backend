package ru.smn.poker.util;

public class ThreadUtil {

    public static void sleep(long seconds){
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            System.out.println("Reload");
        }
    }
}
