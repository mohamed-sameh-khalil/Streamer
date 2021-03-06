package com.company;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;

public class S3Uploader {
    public static void upload(String objectname, String path){
        Dotenv dotenv = Dotenv.configure().directory(System.getProperty("user.dir") + "/").load();
        String clientRegion = dotenv.get("AWS_REGION");
        String bucketName = dotenv.get("AWS_BUCKET_NAME");
        try {
            //This code expects that you have AWS credentials set up
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .build();

            // Upload a file as a new object with ContentType
            PutObjectRequest request = new PutObjectRequest(bucketName, objectname, new File(path));
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("video/mp4");
            request.setMetadata(metadata);
            s3Client.putObject(request);
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }

}

