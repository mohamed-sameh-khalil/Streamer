package com.company;

import com.company.interfaces.VideoHandler;
import org.opencv.core.Size;
import org.opencv.videoio.VideoWriter;

import java.util.ArrayList;

public class VideoHandlerImp implements VideoHandler {
    static{ nu.pattern.OpenCV.loadLocally(); }
    @Override
    public void WriteFrames(ArrayList<String> sMats, String fileName) {

        new VideoHandlerThread(sMats, fileName).write();

    }
    private class VideoHandlerThread extends Thread {
        ArrayList<String> sMats;
        VideoWriter videoWriter;
        String fileName;
        private VideoHandlerThread(ArrayList<String> sMats, String fileName){
            this.sMats = sMats;
            this.fileName = processFileName(fileName);
        }

        @Override
        public void run() {
            if(sMats.isEmpty())return;
            combineFramesToVideo();
            //TODO write the file to AWS
            // or add it to a queue to be handled???
            // Then delete the file???
        }

        public void combineFramesToVideo(){
            //TODO make a constant preset frame size instead of depending on the first frame in the sequence??
            Size sz = ImageProcessor.stringToMat(sMats.get(0)).size();
            //TODO is this right, calculating fps based on the average of the chunk
            double fps = Utils.calculateFPS(sMats.size(), Config.chunkTimeInMillis / 1000);
//            videoWriter = new VideoWriter(fileName, VideoWriter.fourcc('M','J','P','G'), Config.fps, sz);
            System.out.println("FPS: " + fps);
            System.out.println("Size: " + sMats.size());
            videoWriter = new VideoWriter();
            videoWriter.open("fileName.avi", VideoWriter.fourcc('M','J','P','G'), fps, sz);
            System.out.println("Is the file opened? " + videoWriter.isOpened());
            for(String sMat : sMats) {
                videoWriter.write(ImageProcessor.stringToMat(sMat));
            }
            System.out.println("Done");
            videoWriter.release();
            System.exit(0);
        }

        public void write(){
            start();
        }
        public String processFileName(String fileName){
            return fileName + Config.VIDEOEXTENSION;
        }
    }
}
