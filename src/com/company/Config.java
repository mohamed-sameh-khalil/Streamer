package com.company;

public class Config {
    public final static double fps = 30;
    public final static double fpsDelayMillis = (1000.0 / fps);
    public final static long chunkTimeInMillis = 30 * 1000; //30 seconds for chunk of video
    public static final String SEPARATOR = "-";

    public static final String RedisServerIP = "127.0.0.1";
    public static final int RedisServerPort = 6379;
    public static final int DEFAULTEXPIRATIONSECONDS = 1;
    public static final int DISPATCHERREFRESHMINUTS = 5;

    public static final String FFMPEGPATH = "/usr/bin/ffmpeg";
    public static final String FFPROBEPATH = "/usr/bin/ffprobe";

    // Windows FFMPEG location
//    public static final String FFMPEGPATH = "C:\\FFMPEG-PATH\\ffmpeg";
//    public static final String FFPROBEPATH = "C:\\FFMPEG-PATH\\ffprobe";

//    public static final String DEFAULTCAMERAIP = "192.168.1.101";
//    public static final int DEFAULTCAMERAPORT = 8080;
//    public static final String DEFAULTCAMERAID = "1";

    public static final String ORIGINALVIDEOEXTENSION = ".avi";
    public static final String COMPRESSEDVIDEOEXTENSION = ".mp4";

    // Testing purposes, needs to be set true for production
    public static final boolean WRITEUNCOMPRESSEDVIDEO = true;
    public static final boolean WRITECOMPRESSEDVIDEO = true;
    public static final boolean UPLOAD = true;
    public static final boolean DELETETEMPFILES = true;

    public static final boolean STREAMFROMVIDEO = true;
    public static final String VIDEOPATH = "/home/darth/Downloads/DD.mp4";
}
