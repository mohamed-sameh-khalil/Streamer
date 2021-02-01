package com.company.Streaming;

import com.company.Config;
import com.company.RedisFrames;
import com.company.Timer;
import com.company.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

public class Streamer {
    static{ nu.pattern.OpenCV.loadLocally(); }

    private Mat lastFrame;
    private final double fps;
    private final RedisFrames rf;
    private final String cameraIP;
    private final int cameraPort;
    private final UpToDateStreamer upToDateStreamer;


    public Streamer(String cameraIP, int cameraPort){
        this(cameraIP, cameraPort, Config.fps);
    }

    public Streamer(String cameraIP, int cameraPort, double fps){
        this.cameraIP = cameraIP;
        this.cameraPort = cameraPort;
        this.fps = fps;

        String URL = Utils.getURL(cameraIP,cameraPort);
        this.upToDateStreamer = new UpToDateStreamer(URL);

        rf = RedisFrames.getDefaultRedisFrames();
    }

    private void readUpToDateFrame(){
        lastFrame = upToDateStreamer.getLastFrame();
    }

    public void stream(){
        System.out.println("Started Streamer");
        while(true)
            // TODO is time being wasted because we recreate the lambda every time even though its the same thing
            //  or is java smart enough?
            Timer.executeAndWaitFPS(fps, ()->{
                readUpToDateFrame();
                handleFrame();
            });
    }

    private void handleFrame(){
        //TODO create a new thread to write frames to redis/kafka?
        // currently just write it in the same thread
        if(lastFrame != null && !lastFrame.empty()) {
            rf.setLastFrameForCamera(lastFrame, cameraIP, Config.DEFAULTCAMERAID);
        }
    }

    public static void main(String[] args){
        Streamer streamer = new Streamer(Config.DEFAULTCAMERAIP, Config.DEFAULTCAMERAPORT);
        streamer.stream();
    }
}
