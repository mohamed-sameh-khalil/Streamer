package com.company;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import static org.opencv.imgcodecs.Imgcodecs.imread;

public class Main {
    static{ nu.pattern.OpenCV.loadLocally(); }
    public static void main(String[] args) {
        System.out.println("Welcome to OpenCV " + Core.VERSION);
//        Mat src = Imgcodecs.imread("D:\\Downloads\\useless stuff\\image001.jpg");
        VideoCapture vc = new VideoCapture("22.mp4");
        System.out.println("Captured the stream");
        Mat img = new Mat();
        int frames = 0;
        KafkaFrameWriter KFWriter = new KafkaFrameWriter();
        long start = System.currentTimeMillis();
        while(frames < 100) {
            vc.read(img);
            frames++;
            KFWriter.writeFrame(img);
            //HighGui.imshow("Original Image", img);
            //HighGui.waitKey(1);
        }
        KFWriter.closeProducer();
        long end = System.currentTimeMillis();
        double time = (end - start) * 1.0 / 1000;
        System.out.println("Time: " + time + " seconds");
        double fps = frames * 1.0 / time;
        System.out.println("FPS: " + fps);
    }
}
