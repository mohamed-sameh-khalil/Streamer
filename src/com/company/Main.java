package com.company;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

public class Main {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    public static void main(String[] args) {
        System.out.println("Welcome to OpenCV " + Core.VERSION);
//        Mat src = Imgcodecs.imread("D:\\Downloads\\useless stuff\\image001.jpg");
        VideoCapture vc = new VideoCapture("rtsp://192.168.1.11:8080/h264_ulaw.sdp");
        System.out.println("Captured the stream");
        Mat img = new Mat();
        int frames = 0;
        long start = System.currentTimeMillis();
        while(frames < 1000) {
            vc.read(img);
            frames++;
//            HighGui.imshow("Original Image", img);
//            HighGui.waitKey(1);
        }
        long end = System.currentTimeMillis();
        double time = (end - start) * 1.0 / 1000;
        System.out.println("Time: " + time + " seconds");
        double fps = frames * 1.0 / time;
        System.out.println("FPS: " + fps);
    }
}
