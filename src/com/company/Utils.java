package com.company;

import java.io.File;

public class Utils {
    public static void FPSWait(double fps){
        double time = 1000 * 1.0 / fps;
        MilliWait((long)time);
    }
    public static long FPSToMillis(double fps){
        return (long)(1000 / fps);
    }
    public static void MilliWait(long time){
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException ie){

        }
    }

    public static double calculateFPS(int frames, double time){
        return frames / time;
    }

    public static void deleteFile(String fileName){
        new File(fileName).delete();
    }

    public static String getURL(String cameraIP, int cameraPort){
        return "rtsp://" + cameraIP + ":" + cameraPort + "/h264_ulaw.sdp";
    }

}
