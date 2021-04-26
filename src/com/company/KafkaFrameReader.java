package com.company;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaFrameReader {
    KafkaConsumer<String, String> consumer;
    Properties properties;
    public KafkaFrameReader(){
        String bootstrapServers = "127.0.0.1:9092";
        String consumerGroupId =  "frames_readers";
        String topic = "frames";
        this.properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        this.consumer =  new KafkaConsumer<String, String>(properties);
    }

    public void readFrame(){
        consumer.subscribe(Arrays.asList("frames"));
        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                Mat frame = ImageProcessor.stringToMat(record.value());
                HighGui.imshow("Original Image", frame);
                HighGui.waitKey(1);
            }
        }
    }

}
