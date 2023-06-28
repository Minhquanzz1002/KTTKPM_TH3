package vn.edu.iuh.client;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Arrays;
import java.util.Properties;

import static java.lang.System.getProperty;

public class KafkaClient {
    private static final String BOOTSTRAP_SERVERS = getProperty("server", "localhost:9092");
    private static final String TOPIC = getProperty("topic", "my-topic");
    private static final String GROUP_ID = getProperty("user", "chat-b");
    private static KafkaClient INSTANCE = null;
    private final KafkaConsumer<String, String> consumer;
    private final KafkaProducer<String, String> producer;

    private KafkaClient() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        properties.put("group.id",GROUP_ID);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(TOPIC));
        producer = new KafkaProducer<>(properties);
    }

    public void sendMessage(String msg) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC, GROUP_ID, msg);
        producer.send(producerRecord);
        producer.flush();
    }

    public ConsumerRecords<String, String> pollRecords() {
        return consumer.poll(100);
    }

    public void commitAsync() {
        consumer.commitAsync();
    }

    public static KafkaClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new KafkaClient();
        }
        return INSTANCE;
    }
}
