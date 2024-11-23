package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.hub.*;
import ru.yandex.practicum.model.sensor.*;
import ru.yandex.practicum.service.CollectorService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/events")
public class CollectorController {

    private final CollectorService service;

    @PostMapping("/sensors")
    public void processingSensors(@Valid @RequestBody SensorEvent event) {
        service.processingSensors(event);
    }

    @PostMapping("/hubs")
    public void processingHubs(@Valid @RequestBody HubEvent event) {
        service.processingHub(event);
    }
}
