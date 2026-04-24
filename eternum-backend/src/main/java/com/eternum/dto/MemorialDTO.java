package com.eternum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MemorialDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "El nombre del fallecido es obligatorio")
        @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
        private String deceasedName;

        @NotBlank(message = "El apellido del fallecido es obligatorio")
        @Size(max = 200, message = "El apellido no puede exceder 200 caracteres")
        private String deceasedLastName;

        private LocalDate birthDate;

        @NotNull(message = "La fecha de fallecimiento es obligatoria")
        private LocalDate deathDate;

        @Size(max = 5000, message = "La biografía es demasiado larga")
        private String biography;

        @Size(max = 500, message = "La dedicatoria no puede exceder 500 caracteres")
        private String epitaph;

        private String profilePhotoUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
        private String deceasedName;

        @Size(max = 200, message = "El apellido no puede exceder 200 caracteres")
        private String deceasedLastName;

        private LocalDate birthDate;

        private LocalDate deathDate;

        @Size(max = 5000, message = "La biografía es demasiado larga")
        private String biography;

        @Size(max = 500, message = "La dedicatoria no puede exceder 500 caracteres")
        private String epitaph;

        private String profilePhotoUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer pkMemorial;
        private Integer fkUser;
        private String userEmail;
        private String deceasedName;
        private String deceasedLastName;
        private LocalDate birthDate;
        private LocalDate deathDate;
        private String biography;
        private String epitaph;
        private String profilePhotoUrl;
        private Boolean isFeatured;
        private Integer candleCount;
        private Boolean isActive;
        private LocalDateTime createdDate;
        private List<PhotoResponse> photos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoResponse {
        private Integer pkMemorialPhoto;
        private String photoUrl;
        private String caption;
        private Integer displayOrder;
        private Boolean isMainPhoto;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private Integer pkMemorial;
        private String deceasedName;
        private String deceasedLastName;
        private LocalDate birthDate;
        private LocalDate deathDate;
        private String epitaph;
        private String mainPhotoUrl;
        private Integer candleCount;
        private LocalDateTime createdDate;
    }

}
