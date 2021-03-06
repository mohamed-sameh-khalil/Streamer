package com.company.Streaming;

import com.company.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;

public class Streamer {
    static  {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    private Mat lastFrame;
    private final double fps;
    private final RedisFrames rf;
    private final int cameraID;
    private final String cameraIP;
    private final String cameraPort;
    private final UpToDateStreamer upToDateStreamer;
    KafkaFrameWriter frameWriter = new KafkaFrameWriter();
    Timer kafkaTimer = new Timer(1000);

    public Streamer(int cameraID, String cameraIP, String cameraPort){
        this(cameraID, cameraIP, cameraPort, Config.fps);
    }

    public Streamer(int cameraID, String cameraIP, String cameraPort, double fps){
        this.cameraID = cameraID;
        this.cameraIP = cameraIP;
        this.cameraPort = cameraPort;
        this.fps = fps;

        String URL = Utils.getURL(cameraIP, cameraPort);
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
            String value = ImageProcessor.matToString(lastFrame);
            rf.setLastFrameForCamera(value, cameraIP, Integer.toString(cameraID));
            if (kafkaTimer.enoughTimePassed()){
                String timeStamp = Long.toString(System.currentTimeMillis());
                String message = Integer.toString(cameraID) + "." + timeStamp + "." + value;
                frameWriter.writeFrame(message);
                kafkaTimer.reset();
            }
        }
    }
}
