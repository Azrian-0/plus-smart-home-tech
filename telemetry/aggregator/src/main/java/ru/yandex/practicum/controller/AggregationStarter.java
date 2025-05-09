package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.repository.SnapshotRepository;

import java.time.Duration;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private final KafkaProducer<String, SpecificRecordBase> producer;
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final SnapshotRepository snapshotRepository;
    private final KafkaConfig config;

    public void start() {
        try {
            consumer.subscribe(List.of(config.getTopicIn()));
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(Duration.ofMillis(config.getConsumerPollDuration()));
                if (records.isEmpty()) continue;

                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    try {
                        log.info("Получено сообщение: {}, со смещением {}", record.value(), record.offset());

                        Optional<SensorsSnapshotAvro> snapshotAvro = snapshotRepository.updateState(record.value());
                        if (snapshotAvro.isPresent()) {
                            ProducerRecord<String, SpecificRecordBase> producerRecord =
                                    new ProducerRecord<>(config.getTopicOut(), snapshotAvro.get().getHubId(),
                                            snapshotAvro.get());
                            producer.send(producerRecord, (recordMetadata, e) -> {
                                if (e != null) {
                                    log.error("Ошибка отправки", e);
                                    throw new RuntimeException("Ошибка отправки", e);
                                }
                                log.info("Сообщение отправлено в topic - {}", recordMetadata.topic());
                            });
                        }

                        consumer.commitSync(Collections.singletonMap(
                                new TopicPartition(record.topic(), record.partition()),
                                new OffsetAndMetadata(record.offset() + 1)
                        ));
                    } catch (Exception e) {
                        log.error("Ошибка обработки записи: {}, смещение {}", record.value(), record.offset(), e);
                    }
                }
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            log.info("Закрываем консьюмер");
            consumer.close();
            log.info("Закрываем продюсер");
            producer.close();
        }
    }
}
