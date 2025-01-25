package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DimensionDto {
    @Min(value = 1, message = "width should not be less than 1")
    private double width;

    @Min(value = 1, message = "height should not be less than 1")
    private double height;

    @Min(value = 1, message = "depth should not be less than 1")
    private double depth;
}
