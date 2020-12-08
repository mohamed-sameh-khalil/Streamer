package com.company;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.opencv.core.Mat;

import java.util.Properties;

public class KafkaFrameWriter {
//    private final ImageProcessor imageProcessor = new ImageProcessor();
    KafkaProducer<String, String> producer;
    public KafkaFrameWriter(){
        String bootstrapServers = "127.0.0.1:9092";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.producer =  new KafkaProducer<String, String>(properties);
    }

    public void writeFrame(Mat frame){
        String encodedFrame = ImageProcessor.matToString(frame);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("frames",encodedFrame);
        this.producer.send(producerRecord);
        this.producer.flush();

    }

    public void closeProducer(){
        producer.close();
    }

}
