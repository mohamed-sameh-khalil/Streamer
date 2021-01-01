package com.company;

import com.company.interfaces.VideoHandler;
import org.opencv.core.Size;
import org.opencv.videoio.VideoWriter;

import java.util.ArrayList;

import static com.company.Config.ORIGINALVIDEOEXTENSION;

public class VideoHandlerImp implements VideoHandler {
    static{ nu.pattern.OpenCV.loadLocally(); }
    @Override
    public void WriteFrames(ArrayList<String> sMats, String fileName) {

        new VideoHandlerThread(sMats, fileName).write();

    }
    private static class VideoHandlerThread extends Thread {
        ArrayList<String> sMats;
        VideoWriter videoWriter;
        String fileName;
        double fps;
        private VideoHandlerThread(ArrayList<String> sMats, String fileName){
            this.sMats = sMats;
            this.fileName = fileName;
            //TODO is this right, calculating fps based on the average of the chunk
            this.fps = Utils.calculateFPS(sMats.size(), Config.chunkTimeInMillis / 1000);
        }
        public void write(){
            start();
        }
        @Override
        public void run() {
            if(sMats.isEmpty())return;
            combineFramesToCompressedVideo();
            //TODO write the file to AWS
            // or add it to a queue to be handled???
            // Then delete the compressed file???
        }

        private void combineFramesToCompressedVideo(){
            System.out.println("FPS: " + fps);
            writeUnCompressedVideo();
            compress();
            upload();
            deleteTmpFiles();
        }
        private void writeUnCompressedVideo(){
            //TODO make a constant preset frame size instead of depending on the first frame in the sequence??
            Size sz = ImageProcessor.stringToMat(sMats.get(0)).size();
            int fourcc = VideoWriter.fourcc('M','J','P','G');
            String OriginalVideoFileName = getOriginalFileName(fileName);
            videoWriter = new VideoWriter(OriginalVideoFileName, fourcc, fps, sz);
            for(String sMat : sMats) {
                videoWriter.write(ImageProcessor.stringToMat(sMat));
            }
            videoWriter.release();
        }
        private void compress(){
            Compressor.getInstance().compress(fileName, fps);
        }

        private void upload(){
            S3Uploader.upload(fileName, Compressor.getCompressedFileName(fileName));
        }

        private void deleteTmpFiles(){
            Utils.deleteFile(getOriginalFileName(fileName));
            Utils.deleteFile(Compressor.getCompressedFileName(fileName));
        }

        public static String getOriginalFileName(String fileName){
            return fileName + ORIGINALVIDEOEXTENSION;
        }
    }
}
