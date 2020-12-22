package com.company;

import com.company.interfaces.VideoHandler;
import org.opencv.core.Size;
import org.opencv.videoio.VideoWriter;

import java.util.ArrayList;

import static com.company.Config.COMPRESSEDVIDEOEXTENSION;
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

        public void combineFramesToCompressedVideo(){
            System.out.println("FPS: " + fps);

            writeOriginalVideo();
            System.out.println("wrote the file before compression");
            compress();

            System.out.println("Done");
//            System.exit(0);
        }
        public void writeOriginalVideo(){
            //TODO make a constant preset frame size instead of depending on the first frame in the sequence??
            Size sz = ImageProcessor.stringToMat(sMats.get(0)).size();
            int fourcc = VideoWriter.fourcc('M','J','P','G');
            String OriginalVideoFileName = processFileName(fileName);
            videoWriter = new VideoWriter(OriginalVideoFileName, fourcc, fps, sz);
            for(String sMat : sMats) {
                videoWriter.write(ImageProcessor.stringToMat(sMat));
            }
            videoWriter.release();
        }
        public void compress(){
            Compressor.getInstance().compress(fileName, fps);
        }
        public String processFileName(String fileName){
            return fileName + ORIGINALVIDEOEXTENSION;
        }
    }
}
