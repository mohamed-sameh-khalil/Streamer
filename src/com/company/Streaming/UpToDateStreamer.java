package com.company.Streaming;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.util.concurrent.locks.ReentrantLock;

public class UpToDateStreamer {
    static  {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    private Mat lastFrame;
    private final ReentrantLock lock = new ReentrantLock();
    VideoCapture vc;
    public UpToDateStreamer(String URL){
        vc = new VideoCapture(URL);
        new Thread(()->{
            Mat frame = new Mat();
            int cnt = 0;
            while(true) {
                cnt++;
                vc.read(frame);
                lock.lock();
                try {
                    lastFrame = frame;
                }
                finally {
                    lock.unlock();
//                    System.out.println("Updated frame:" + (cnt / 30));
                }
            }
        }).start();
    }
    public Mat getLastFrame() {
        Mat frame;
        lock.lock();
        try {
            frame = lastFrame;
        }
        finally {
            lock.unlock();
        }
        return frame;
    }
}
