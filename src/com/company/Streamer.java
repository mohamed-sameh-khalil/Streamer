package com.company;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class Streamer {
    static{ nu.pattern.OpenCV.loadLocally(); }

    private VideoCapture vc;
    private Mat lastFrame;
    private double fps;

    public Streamer(String URL, double fps){
        vc = new VideoCapture(URL);
        this.fps = fps;
    }

    public Streamer(String URL){
        this(URL, Config.fps);
    }

    public Mat getFrame(){
        vc.read(lastFrame);
        return lastFrame.clone();
    }

    public void stream(){
        Mat mat = getFrame();
        Utils.FPSWait(fps);
    }
}
