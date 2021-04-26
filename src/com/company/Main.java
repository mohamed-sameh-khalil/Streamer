package com.company;


import com.company.Dispatcher.Dispatcher;
import org.opencv.core.Core;

public class Main {
    static  {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    public static void main(String[] args) {
        // Run dispatcher
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.startDispatcher();

//        new Thread(()->{
//            Streamer streamer = new Streamer(7, "192.168.1.100", "8080");
//            streamer.stream();
//        }).start();
//
//        new Thread(()->{
//            VideoCollector videoCollector = new VideoCollector("192.168.1.101", "5");
//            videoCollector.collect();
//        }).start();
//
//        RedisFrames rf = RedisFrames.getDefaultRedisFrames();
//        Mat mat = new Mat();
//        while(true) {
//            String sMat = rf.getLastFrameForCamera("192.168.1.100", "7");
//            mat = ImageProcessor.stringToMat(sMat);
//            HighGui.imshow("Camera ID = " + 7, mat);
//            HighGui.waitKey(10);
//        }
    }
}
