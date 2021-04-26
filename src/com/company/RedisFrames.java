package com.company;

import org.opencv.core.Core;
import org.opencv.core.Mat;
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

    public static RedisFrames getDefaultRedisFrames(){
        return new RedisFrames(RedisServerIP, RedisServerPort, DEFAULTEXPIRATIONSECONDS);
    }

    public void setLastFrameForCamera(Mat img, String cameraIP, String cameraID){
        String key = getKeyForCamera(cameraIP, cameraID);
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
    static  {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
}
