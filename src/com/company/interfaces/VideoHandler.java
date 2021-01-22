package com.company.interfaces;

import org.opencv.core.Mat;

import java.util.ArrayList;

public interface VideoHandler {
    void writeFrames(ArrayList<String> sMats, String fileName);
    void writeFrames(ArrayList<String> sMats, String fileName, long timeInMillis);

}
