package com.company;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class Streamer {
    static{ nu.pattern.OpenCV.loadLocally(); }

    private VideoCapture vc;
    private Mat lastFrame;
    private int fps;

    public Streamer(String URL, int fps){
        vc = new VideoCapture(URL);
        this.fps = fps;
    }

    public Streamer(String URL){
        this(URL, 60);
    }

    public Mat getFrame(){
        vc.read(lastFrame);
        return lastFrame.clone();
    }

    public void stream(){
        Mat mat = getFrame();
        wait(fps);
    }

    public void wait(int fps){
        double time = 1000 * 1.0 / fps;
        try {
            Thread.sleep((long)time);
        }
        catch (InterruptedException ie){

        }
    }
}
