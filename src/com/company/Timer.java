package com.company;

import jdk.jshell.execution.Util;

public class Timer {
    private long timestamp;
    private long moddedTimestamp;
    private long delayTimestamp; //this is used to calculate correct wait time
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
    public void executeAndWaitFPS(double fps, Executor executor){
        //this a wrapper to implement accurate waits
        prepareWaitTimer();
        executor.execute();
        waitCorrect(fps);
    }
    private void resetTimeStamp(){
        timestamp = System.currentTimeMillis();
    }
    private void resetModdedTimeStamp() {
        moddedTimestamp = (System.currentTimeMillis() / chunkTimeInMillis) * chunkTimeInMillis;
    }
    private void prepareWaitTimer(){
        resetDelayTimeStamp();
    }
    private void waitCorrect(double fps){
        long now = System.currentTimeMillis();
        long duration = Utils.FPSToMillis(fps);
        long waitTime = delayTimestamp + duration - now;
        if(waitTime > 0)Utils.MilliWait(waitTime);
    }
    private void resetDelayTimeStamp(){
        delayTimestamp = System.currentTimeMillis();
    }
}
