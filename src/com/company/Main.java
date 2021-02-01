package com.company;


import java.io.File;

import static org.opencv.imgcodecs.Imgcodecs.imread;

public class Main {
    static{ nu.pattern.OpenCV.loadLocally(); }

    public static void main(String[] args) {
        RedisFrames rf = RedisFrames.getDefaultRedisFrames();
        Mat mat = new Mat();
        while(true) {
            String sMat = rf.getLastFrameForCamera(Config.DEFAULTCAMERAIP, Config.DEFAULTCAMERAID);
            mat = ImageProcessor.stringToMat(sMat);
            HighGui.imshow("Test", mat);
            HighGui.waitKey(10);
        }
    }
}
