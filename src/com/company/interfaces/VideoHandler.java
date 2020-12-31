package com.company.interfaces;

import org.opencv.core.Mat;

import java.util.ArrayList;

public interface VideoHandler {
    void WriteFrames(ArrayList<String> sMats, String fileName);

}
