package com.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ImageProcessor {
    static  {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    private static class SerializableImage{
        public byte[] img;
        public int height;
        public int width;
        public int type;

        public SerializableImage(byte[] img, int height, int width, int type) {
            this.img = img;
            this.height = height;
            this.width = width;
            this.type = type;
        }


        public SerializableImage() {
        }
    }
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
            //File outputfile = new File("image.jpg");
            //ImageIO.write(image, "jpg", outputfile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat frame = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        frame.put(0, 0, pixels);
        return frame;
    }

    private static String serializeMat(Mat mat){
        int n = (int)mat.size().height;
        int m = (int)mat.size().width;
        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] byteArray = new byte[bufferSize];
        mat.get(0, 0, byteArray); // get all the pixels

        SerializableImage ans = new SerializableImage(byteArray, n, m, mat.type());

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(ans);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    static private Mat deserializeMat(String json){
        try {
            ObjectMapper mapper = new ObjectMapper();
            SerializableImage si = mapper.readValue(json, SerializableImage.class);
            Mat mat = new Mat(si.height, si.width, si.type);
            mat.put(0, 0, si.img);
            return mat;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

//  For Testing
    public static void main(String[] args) {
//        Mat src = Imgcodecs.imread("5.jpg");
//        String s = matToString(src);
//        Mat frame = stringToMat(s);
//        Imgcodecs imageCodecs = new Imgcodecs();
//        Mat matrix = imageCodecs.imread("1.jpg");
//        imageCodecs.imwrite("size.jpg", matrix);
//        HighGui.imshow("Original Image", frame);
//        HighGui.waitKey(0);
    }
}