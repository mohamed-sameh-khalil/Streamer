package com.company;

import com.company.interfaces.VideoHandler;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;

import static com.company.Config.*;

public class VideoCollector {
    public static class FrameStreamStopped extends RuntimeException{}

    private VideoHandler videoHandler;
    private RedisFrames rf;
    private ArrayList<String> sMats;

    private long lastModdedTimeStamp = System.currentTimeMillis();

    private final String cameraIP;
    private final String cameraID;

    public VideoCollector(String cameraIP, String cameraID){
        this.cameraID = cameraID;
        this.cameraIP = cameraIP;
        this.rf = new RedisFrames();
        this.videoHandler = new VideoHandlerImp();
    }
    public void collect() throws FrameStreamStopped{
        sMats = new ArrayList<>();
        updateTimeStamp();
        while(true){
            if(enoughTimePassed()){
                processBatch();
            }
            try {
                addingAFrame();
                Utils.FPSWait(fps);
            }
            catch (RedisFrames.FrameNotExist fne){
                //TODO add an action to occur when multiple frames are missed
                // maybe exit the function??
            }
        }
    }

    private void processBatch() throws FrameStreamStopped{
        if(sMats.isEmpty())throw new FrameStreamStopped();
        updateTimeStamp();
        videoHandler.WriteFrames(sMats,getFileName());
        clearOldBatch();
    }
    private void addingAFrame(){
        sMats.add(rf.getLastFrameForCamera(cameraIP, cameraID));
    }
    private void clearOldBatch(){
        sMats = new ArrayList<>();
    }

    private String getFileName(){
        return cameraIP + SEPARATOR + cameraID + SEPARATOR + lastModdedTimeStamp;
    }

    private boolean enoughTimePassed(){
        return System.currentTimeMillis() >= lastModdedTimeStamp + chunkTimeInMillis;
    }

    private void updateTimeStamp(){
        lastModdedTimeStamp = (System.currentTimeMillis() / chunkTimeInMillis) * chunkTimeInMillis;
    }

    public static void main(String[] args){


        RedisFrames rf = new RedisFrames("127.0.0.1", 6379, 10000);
        Mat src = Imgcodecs.imread("/home/darth/GP/darknet/predictions.jpg");
        rf.setLastFrameForCamera(src, "1", "1");
        VideoCollector videoCollector = new VideoCollector("1", "1");
        videoCollector.collect();

    }

}
