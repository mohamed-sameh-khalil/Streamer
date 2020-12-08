package com.company;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ImageProcessor {
    static{ nu.pattern.OpenCV.loadLocally(); }
    public static String matToString(Mat frame) {
        int type = BufferedImage.TYPE_3BYTE_BGR;
        int bufferSize = frame.channels() * frame.cols() * frame.rows();
        byte[] byteArray = new byte[bufferSize];
        frame.get(0, 0, byteArray); // get all the pixels
        BufferedImage image = new BufferedImage(frame.cols(), frame.rows(), type);
        byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(byteArray, 0, targetPixels, 0, byteArray.length);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();

        return Base64.getEncoder().encodeToString(bytes);
    }

    public static Mat stringToMat(String imageString) {
        BufferedImage image = null;
        byte[] imageByte;
        try {
            imageByte = Base64.getDecoder().decode(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
            File outputfile = new File("image.jpg");
            ImageIO.write(image, "jpg", outputfile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat frame = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        frame.put(0, 0, pixels);
        return frame;
    }

//  For Testing
//    public static void main(String[] args) {
//        Mat src = Imgcodecs.imread("lady.jfif");
//        String s = matToString(src);
//        Mat frame = stringToMat(s);
//        HighGui.imshow("Original Image", frame);
//        HighGui.waitKey(0);
//    }
}