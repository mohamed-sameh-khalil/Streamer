package com.company.Dispatcher;

public class Camera {
    private int Camera_ID;
    private String IP;
    private String PORT;

    public Camera (int Camera_ID, String IP, String PORT) {
        this.Camera_ID = Camera_ID;
        this.IP = IP;
        this.PORT = PORT;
    }

    public int getCamera_ID() {
        return Camera_ID;
    }

    public String getIP() {
        return IP;
    }

    public String getPORT() {
        return PORT;
    }

    @Override
    public String toString() {
        return "Camera{" +
                "Camera_ID=" + Camera_ID +
                ", IP='" + IP + '\'' +
                ", PORT='" + PORT + '\'' +
                '}';
    }
}
