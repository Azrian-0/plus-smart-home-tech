package ru.yandex.practicum.model.hub;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.enums.DeviceSensorType;
import ru.yandex.practicum.enums.HubEventType;

import static ru.yandex.practicum.enums.HubEventType.DEVICE_ADDED;

@Getter
@Setter
@ToString
public class DeviceAddedEvent extends HubEvent {

    @NonNull
    private String id;
    @NonNull
    private DeviceSensorType deviceType;

    public HubEventType getType() {
        return DEVICE_ADDED;
    }
}
