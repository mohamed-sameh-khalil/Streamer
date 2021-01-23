package com.company;

import com.company.services.TimedFileConsumingService;

import java.io.File;

import static org.opencv.imgcodecs.Imgcodecs.imread;

public class Main {
    static{ nu.pattern.OpenCV.loadLocally(); }
    static class UploaderFileService extends TimedFileConsumingService {
        public UploaderFileService(String path) {
            super(path);
        }

        public UploaderFileService(String path, long delay) {
            super(path, delay);
        }

        @Override
        protected void execute(File file) {
            System.out.println("Got to a file: " + file.getName());
        }
    }
    public static void main(String[] args) {
        UploaderFileService ufs = new UploaderFileService("tests", 10000);
        ufs.run();
    }
}
