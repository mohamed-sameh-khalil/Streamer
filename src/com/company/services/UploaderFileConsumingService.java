package com.company.services;

import com.company.S3Uploader;

import java.io.File;

public class UploaderFileConsumingService extends TimedFileConsumingService {
    public UploaderFileConsumingService(String path) {
        super(path);
    }
    public UploaderFileConsumingService(String path, long delay) {
        super(path, delay);
    }
    @Override
    protected void execute(File file) {
        S3Uploader.upload(file.getName(), file);
    }
}
