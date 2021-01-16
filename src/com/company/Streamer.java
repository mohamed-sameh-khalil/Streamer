package com.company;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.net.URL;
import java.net.URLConnection;

public class Streamer {
    static{ nu.pattern.OpenCV.loadLocally(); }

    private VideoCapture vc;
    private Mat lastFrame;
    private double fps;
    private RedisFrames rf;
    private String cameraIP;
    private int cameraPort;


    public Streamer(String cameraIP, int cameraPort){
        this(cameraIP, cameraPort, Config.fps);
    }

    public Streamer(String cameraIP, int cameraPort, double fps){
        String URL = getURL(cameraIP,cameraPort);
        vc = new VideoCapture(URL);

        lastFrame = new Mat();
        this.fps = fps;
        this.cameraIP = cameraIP;
        rf = RedisFrames.getDefaultRedisFrames();
    }

    private String getURL(String cameraIP, int cameraPort){
        //TODO move this function to utils
        return "rtsp://" + cameraIP + ":" + cameraPort + "/h264_ulaw.sdp";
    }

    private void readFrame(){
        vc.read(lastFrame);
    }

    public void stream(){
        while(true) {
            readFrame();
            System.out.println(lastFrame.size());
            handleFrame();
            //Utils.FPSWait(fps);
        }
    }

    private void handleFrame(){
        //TODO create a new thread to write frames to redis/kafka?
        // currently just write it in the same thread
        rf.setLastFrameForCamera(lastFrame, cameraIP, Config.DEFAULTCAMERAID);
    }

    public static void main(String[] args){
        Streamer streamer = new Streamer(Config.DEFAULTCAMERAIP, Config.DEFAULTCAMERAPORT);
        streamer.stream();
    }
}
