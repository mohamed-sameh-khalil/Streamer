package com.company;

import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import redis.clients.jedis.Jedis;

import static com.company.Config.*;

public class RedisFrames {

    static public class FrameNotExist extends RuntimeException { }
    private Jedis jedis;
    private String IP;
    private int port;
    private int ExpirationTimeInSeconds;

    public RedisFrames(){
        this(RedisServerIP, RedisServerPort);
    }

    public RedisFrames(String IP, int port){
        this(IP, port, 1);
    }

    public RedisFrames(String IP, int port, int ExpirationTimeInSeconds){
        this.IP = IP;
        this.port = port;
        this.ExpirationTimeInSeconds = ExpirationTimeInSeconds;
        jedis = new Jedis(IP, port);
    }

    public void setLastFrameForCamera(Mat img, String cameraIP, String cameraID){
        // TODO change img.toString() into the correct format
        String key = getKeyForCamera(cameraIP,cameraID);
        String value = ImageProcessor.matToString(img);
        jedis.setex(key, ExpirationTimeInSeconds, value);

    }

    public String getKeyForCamera(String cameraIP, String cameraID){
        return cameraIP + SEPARATOR + cameraID;
    }

    public String getLastFrameForCamera(String cameraIP, String cameraID) throws FrameNotExist{
        String key = getKeyForCamera(cameraIP,cameraID);
        if(jedis.exists(key)){
            return jedis.get(key);
        }
        throw new FrameNotExist();
    }
    static{ nu.pattern.OpenCV.loadLocally(); }
    public static void main(String[] args){
        RedisFrames rf = new RedisFrames("127.0.0.1", 6379);
        Mat src = Imgcodecs.imread("/home/darth/GP/darknet/predictions.jpg");

        rf.setLastFrameForCamera(src, "1", "1");
        src = ImageProcessor.stringToMat(rf.getLastFrameForCamera("1", "1"));

        HighGui.imshow("Original Image", src);
        HighGui.waitKey();
    }
}
