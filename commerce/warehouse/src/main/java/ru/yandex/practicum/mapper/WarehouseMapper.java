package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {
    WarehouseProduct toWarehouse(NewProductInWarehouseRequest request);
}
