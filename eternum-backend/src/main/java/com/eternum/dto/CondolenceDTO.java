package com.eternum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CondolenceDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotNull(message = "El ID del memorial es obligatorio")
        private Integer fkMemorial;

        @NotBlank(message = "El nombre del autor es obligatorio")
        @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
        private String authorName;

        @Size(max = 255, message = "El email no puede exceder 255 caracteres")
        private String authorEmail;

        @NotBlank(message = "El mensaje es obligatorio")
        @Size(max = 2000, message = "El mensaje no puede exceder 2000 caracteres")
        private String message;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer pkCondolence;
        private Integer fkMemorial;
        private Integer fkAuthorUser;
        private String authorName;
        private String authorEmail;
        private String message;
        private Boolean isApproved;
        private LocalDateTime createdDate;
    }

}
