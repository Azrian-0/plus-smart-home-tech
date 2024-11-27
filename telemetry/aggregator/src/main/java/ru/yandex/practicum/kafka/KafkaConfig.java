package ru.yandex.practicum.kafka;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.Properties;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    String bootstrapServer;
    @Value("${spring.kafka.group.id}")
    String groupId;
    @Getter
    @Value("${spring.kafka.topic.in}")
    String topicIn;
    @Getter
    @Value("${spring.kafka.topic.out}")
    String topicOut;
    @Value("${spring.kafka.producer.key-serializer}")
    Class<?> keySerializer;
    @Value("${spring.kafka.producer.value-serializer}")
    Class<?> valueSerializer;
    @Value("${spring.kafka.consumer.key-deserializer}")
    Class<?> keyDeserializer;
    @Value("${spring.kafka.consumer.value-deserializer}")
    Class<?> valueDeserializer;
    @Value("${spring.kafka.consumer.enable-auto-commit}")
    boolean enableAutoCommit;
    @Getter
    @Value("${spring.kafka.consumer.poll-duration}")
    private long consumerPollDuration;


    @Bean
    public KafkaProducer<String, SpecificRecordBase> getProducer() {
        Properties configProps = new Properties();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

        return new KafkaProducer<>(configProps);
    }

    @Bean
    public KafkaConsumer<String, SensorEventAvro> getConsumer() {
        Properties configProps = new Properties();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        return new KafkaConsumer<>(configProps);
    }
}
