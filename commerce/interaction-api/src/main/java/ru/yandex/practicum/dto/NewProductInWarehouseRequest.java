package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewProductInWarehouseRequest {
    @NotBlank
    private String productId;

    private boolean fragile;

    @NotNull
    private DimensionDto dimension;

    @Min(value = 1, message = "weight should not be less than 1")
    private double weight;
}
