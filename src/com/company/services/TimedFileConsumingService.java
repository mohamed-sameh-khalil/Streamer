package com.company.services;

import com.company.Config;
import com.company.Utils;

import java.io.File;

public abstract class TimedFileConsumingService {
    private final long delay;
    private final String path;

    public TimedFileConsumingService(String path){
        this(path, Config.FileServiceTimeDelay);
    }

    public TimedFileConsumingService(String path, long delay){
        this.delay = delay;
        this.path = path;
    }

    public final void run(){
        while(true){
            for(File file : getFilesInPath()){
                execute(file);
                file.delete();
            }
            Utils.MilliWait(delay);
        }
    }

    protected abstract void execute(File file);

    private File[] getFilesInPath() {
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null)
            return directoryListing;
        return new File[0];
    }
}
