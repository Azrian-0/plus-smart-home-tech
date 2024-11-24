package ru.yandex.practicum.model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.enums.ConditionType;
import ru.yandex.practicum.enums.ConditionOperation;

@Getter
@Setter
@ToString
public class ScenarioCondition {

    private String sensorId;
    private ConditionType type;
    private ConditionOperation operation;
    private int value;
}
