package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.types.QuantityState;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetProductQuantityStateRequest {
    @NotBlank
    private String productId;

    @NotNull
    private QuantityState quantityState;
}
