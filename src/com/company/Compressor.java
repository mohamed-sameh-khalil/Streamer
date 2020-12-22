package com.company;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import static com.company.Config.*;
import static com.company.Utils.deleteFile;

public class Compressor {
    private FFmpeg ffmpeg;
    private FFprobe ffprobe;
    private FFmpegExecutor executor;
    private static Compressor instance;
    private Compressor(){
        try {
            ffmpeg = new FFmpeg(FFMPEGPATH);
            ffprobe = new FFprobe(FFPROBEPATH);
            executor = new FFmpegExecutor(ffmpeg, ffprobe);
        }
        catch (Exception e){

        }
    }
    public static Compressor getInstance(){
        if(instance != null)
            return instance;
        return instance = new Compressor();
    }
    public void compress(String fileName, double fps){
        String oldFile = fileName + ORIGINALVIDEOEXTENSION;
        String newFile = fileName + COMPRESSEDVIDEOEXTENSION;
        FFmpegBuilder builder = getBuilder(oldFile, newFile, fps);
        executor.createJob(builder).run();

        deleteFile(oldFile);
    }
    private FFmpegBuilder getBuilder(String oldFile, String newFile, double fps){
        return new FFmpegBuilder()
                .setInput(oldFile)     // Filename, or a FFmpegProbeResult
                .addOutput(newFile)   // Filename for the destination
                .setVideoCodec("libx264")     // Video using x264
                .setVideoFrameRate((int)fps, 1)
                .done();
    }
}
