package com.company;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.net.URL;
import java.net.URLConnection;

public class Streamer {
    static{ nu.pattern.OpenCV.loadLocally(); }

    private final VideoCapture vc;
    private final Mat lastFrame;
    private final double fps;
    private final RedisFrames rf;
    private final String cameraIP;
    private final int cameraPort;


    public Streamer(String cameraIP, int cameraPort){
        this(cameraIP, cameraPort, Config.fps);
    }

    public Streamer(String cameraIP, int cameraPort, double fps){
        String URL = getURL(cameraIP,cameraPort);
        vc = new VideoCapture(URL);
        lastFrame = new Mat();
        this.fps = fps;
        this.cameraIP = cameraIP;
        this.cameraPort = cameraPort;
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
            // TODO is time being wasted because we recreate the lambda every time even though its the same thing
            //  or is java smart enough?
            Timer.executeAndWaitFPS(fps, ()->{
                readFrame();
                System.out.println(lastFrame.size());
                handleFrame();
            });
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
