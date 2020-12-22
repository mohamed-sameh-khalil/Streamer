package com.company;

public class Config {
    public final static double fps = 24;
    public final static double fpsDelayMillis = (1000.0 / fps);
    public final static long chunkTimeInMillis = 30 * 1000; //30 seconds for chunk of video
    public static final String SEPARATOR = "-";

    public static final String RedisServerIP = "127.0.0.1";
    public static final String FFMPEGPATH = "/usr/bin/ffmpeg";
    public static final String FFPROBEPATH = "/usr/bin/ffprobe";
    public static final int RedisServerPort = 6379;
    public static final String ORIGINALVIDEOEXTENSION = ".avi";
    public static final String COMPRESSEDVIDEOEXTENSION = ".mp4";
}
