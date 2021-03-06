package com.company;

import java.io.File;

import static com.company.Config.STREAMFROMVIDEO;
import static com.company.Config.VIDEOPATH;

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

    public static String getURL(String cameraIP, String cameraPort){
        if(STREAMFROMVIDEO){
            return VIDEOPATH;
        }
        else{
            return "rtsp://" + cameraIP + ":" + cameraPort + "/h264_ulaw.sdp";
        }
    }

}
