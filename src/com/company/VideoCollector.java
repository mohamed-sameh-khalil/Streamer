package com.company;

import com.company.interfaces.VideoHandler;

import java.util.ArrayList;

import static com.company.Config.*;

public class VideoCollector {
    public static class FrameStreamStopped extends RuntimeException {}

    private final VideoHandler videoHandler;
    private final RedisFrames rf;
    private ArrayList<String> sMats;


    private final Timer timer;
    private final String cameraIP;
    private final String cameraID;

    public VideoCollector(String cameraIP, String cameraID) {
        this.videoHandler = new VideoHandlerImp();
        this.rf = new RedisFrames();
        this.timer = new Timer();
        this.cameraID = cameraID;
        this.cameraIP = cameraIP;

    }

    public void collect() throws FrameStreamStopped {
        sMats = new ArrayList<>();
        timer.reset();
        while (true) {
            if (timer.enoughTimePassed()) {
                processBatch();
            }
            try {
                timer.executeAndWaitFPS(fps, this::addingAFrame);
            } catch (RedisFrames.FrameNotExist fne) {
                //TODO add an action to occur when multiple frames are missed
                // maybe exit the function??
            }
        }
    }

    private void processBatch() throws FrameStreamStopped {
        if (sMats.isEmpty()) throw new FrameStreamStopped();
        long duration = timer.getTimeSpentInMillis();
        timer.reset();
        String fileName = getFileName();//must get fileName after resetting the timer to get the correct time
        videoHandler.writeFrames(sMats, fileName, duration);
        clearOldBatch();
    }

    private void addingAFrame() throws RedisFrames.FrameNotExist {
        sMats.add(rf.getLastFrameForCamera(cameraIP, cameraID));
    }

    private void clearOldBatch() {
        sMats = new ArrayList<>();
    }

    private String getFileName() {
        return cameraIP + SEPARATOR + cameraID + SEPARATOR + timer.getModdedTimestamp();
    }

    public static void main(String[] args) {
        VideoCollector videoCollector = new VideoCollector(DEFAULTCAMERAIP, DEFAULTCAMERAID);
        videoCollector.collect();

    }

}
