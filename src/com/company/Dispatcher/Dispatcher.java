package com.company.Dispatcher;

import com.company.Config;
import com.company.Streaming.Streamer;
import com.company.VideoCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Dispatcher {
    private HashMap<Integer, Camera> camerasMap; // CameraID --> Camera Object
    private SQLDatabaseConnection db_conn;

    public Dispatcher ()
    {
        db_conn = new SQLDatabaseConnection();
        camerasMap = new HashMap<>();
    }

    public void startDispatcher(){
        // opens a new thread for dispatcher
        new Thread(()-> {
            while (true) {
                try {
                    this.refresh();
                    TimeUnit.MINUTES.sleep(Config.DISPATCHERREFRESHMINUTS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Quires the camera table, and open process for existing cameras
    private void refresh() {
        ArrayList<Camera> cameras = db_conn.getCameras();

        for (Camera c : cameras) {
            if (!camerasMap.containsValue(c.getCamera_ID())) {
                new Thread(() -> {
                    Streamer streamer = new Streamer(c.getCamera_ID(), c.getIP(), c.getPORT());
                    streamer.stream();
                }).start();

                new Thread(() -> {
                    VideoCollector videoCollector = new VideoCollector(c.getIP(), Integer.toString(c.getCamera_ID()));
                    videoCollector.collect();
                }).start();

                camerasMap.put(c.getCamera_ID(), c);
                System.out.println(c);
            }
        }
    }
}
