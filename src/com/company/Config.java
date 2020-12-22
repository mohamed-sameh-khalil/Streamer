package com.company;

public class Config {
    public final static double fps = 24;
    public final static double fpsDelayMillis = (1000.0 / fps);
    public final static long chunkTimeInMillis = 10 * 1000; //30 seconds for chunk of video
    public static final String SEPARATOR = "-";

    public static final String RedisServerIP = "127.0.0.1";
    public static final int RedisServerPort = 6379;
    public static final String VIDEOEXTENSION = ".mp4";
}
