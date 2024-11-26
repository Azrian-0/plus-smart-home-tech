package ru.yandex.practicum.kafka.serialization;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public class SensorEventDeserializer extends BaseAvroDeserializer<SensorEventAvro> {
    public SensorEventDeserializer(){
        super(SensorEventAvro.getClassSchema());
    }
}