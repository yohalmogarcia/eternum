package com.eternum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class DeactivationRequestDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "El nombre del fallecido es obligatorio")
        @Size(max = 400, message = "El nombre no puede exceder 400 caracteres")
        private String deceasedFullName;

        @NotBlank(message = "El email de la cuenta es obligatorio")
        @Size(max = 255, message = "El email no puede exceder 255 caracteres")
        private String deceasedEmail;

        @NotBlank(message = "La plataforma de redes sociales es obligatoria")
        @Size(max = 100, message = "La plataforma no puede exceder 100 caracteres")
        private String socialMediaPlatform;

        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(max = 200, message = "El nombre de usuario no puede exceder 200 caracteres")
        private String socialMediaUsername;

        private String proofDocumentUrl;

        private String deathCertificateUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer pkDeactivationRequest;
        private Integer fkRequesterUser;
        private String deceasedFullName;
        private String deceasedEmail;
        private String socialMediaPlatform;
        private String socialMediaUsername;
        private String proofDocumentUrl;
        private String deathCertificateUrl;
        private String requestStatus;
        private String statusNotes;
        private LocalDateTime submittedDate;
        private LocalDateTime processedDate;
        private LocalDateTime createdDate;
    }

}
