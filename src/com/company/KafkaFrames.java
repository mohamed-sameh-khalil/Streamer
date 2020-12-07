package com.company;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

public class KafkaFrames {
    KafkaProducer<String, String> producer;
    Properties properties;
    public KafkaFrames(){
        String bootstrapServers = "127.0.0.1:9092";
        this.properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.producer =  new KafkaProducer<String, String>(properties);
    }

    private String MatToString(Mat frame){
        int type = BufferedImage.TYPE_3BYTE_BGR;
        int bufferSize = frame.channels()*frame.cols()*frame.rows();
        byte [] byteArray = new byte[bufferSize];
        frame.get(0,0,byteArray); // get all the pixels
        BufferedImage image = new BufferedImage(frame.cols(),frame.rows(), type);
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

    public void writeFrame(Mat frame){
        String encodedFrame = MatToString(frame);
        KafkaProducer<String, String> producer =  new KafkaProducer<String, String>(properties);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("first_topic",encodedFrame);

        producer.send(producerRecord);
        producer.flush();
        producer.close();
    }

}
