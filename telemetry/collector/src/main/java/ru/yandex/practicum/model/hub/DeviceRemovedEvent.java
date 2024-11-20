package ru.yandex.practicum.model.hub;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.enums.HubEventType;

import static ru.yandex.practicum.enums.HubEventType.DEVICE_REMOVED;

@Getter
@Setter
@ToString
public class DeviceRemovedEvent extends  HubEvent{

    @NonNull
    private String id;

    public HubEventType getType(){
        return DEVICE_REMOVED;
    }
}
