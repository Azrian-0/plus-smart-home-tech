package ru.yandex.practicum.model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.enums.ActionType;

@Getter
@Setter
@ToString
public class DeviceActionEvent {
    private String sensorId;
    private ActionType type;
    private int value;
}
