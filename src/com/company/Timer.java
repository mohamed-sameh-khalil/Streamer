package com.company;

import jdk.jshell.execution.Util;

public class Timer {
    private long timestamp;
    private long moddedTimestamp;
    private final long chunkTimeInMillis;

    public interface Executor{
        public void execute();
    }

    public Timer(){
        this(Config.chunkTimeInMillis);
    }
    public Timer(long chunkTimeInMillis){
        this.chunkTimeInMillis = chunkTimeInMillis;
        reset();
    }
    public long getTimeSpentInMillis(){
        return System.currentTimeMillis() - timestamp;
    }
    public boolean enoughTimePassed(){
        return System.currentTimeMillis() >= moddedTimestamp + chunkTimeInMillis;
    }
    public long getModdedTimestamp(){
        return moddedTimestamp;
    }
    public void reset(){
        resetTimeStamp();
        resetModdedTimeStamp();
    }
    public static void executeAndWaitFPS(double fps, Executor executor){
        //this a wrapper to implement accurate waits
        long start = System.currentTimeMillis();
        executor.execute();
        waitCorrect(start, fps);
    }
    private void resetTimeStamp(){
        timestamp = System.currentTimeMillis();
    }
    private void resetModdedTimeStamp() {
        moddedTimestamp = (System.currentTimeMillis() / chunkTimeInMillis) * chunkTimeInMillis;
    }
    private static void waitCorrect(long start, double fps){
        long end = System.currentTimeMillis();
        long duration = Utils.FPSToMillis(fps);
        long waitTime = start + duration - end;
        if(waitTime > 0)Utils.MilliWait(waitTime);
    }
}
