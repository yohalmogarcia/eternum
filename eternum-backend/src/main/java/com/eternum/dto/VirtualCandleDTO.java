package com.eternum.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class VirtualCandleDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LightRequest {
        @NotNull(message = "El ID del memorial es obligatorio")
        private Integer fkMemorial;

        @Size(max = 255, message = "El mensaje no puede exceder 255 caracteres")
        private String candleMessage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer pkVirtualCandle;
        private Integer fkMemorial;
        private Integer fkUser;
        private String candleMessage;
        private LocalDateTime litDate;
        private LocalDateTime createdDate;
    }

}
