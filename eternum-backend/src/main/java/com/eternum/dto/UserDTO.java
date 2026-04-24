package com.eternum.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe tener formato válido")
        private String email;

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        private String password;

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        private String firstName;

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
        private String lastName;

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        private String phone;

        private Boolean hasConsentPrivacy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe tener formato válido")
        private String email;

        @NotBlank(message = "La contraseña es obligatoria")
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordResetRequest {
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe tener formato válido")
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordUpdateRequest {
        @NotBlank(message = "El token es obligatorio")
        private String token;

        @NotBlank(message = "La nueva contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        private String newPassword;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer pkUser;
        private String email;
        private String firstName;
        private String lastName;
        private String phone;
        private Boolean isEmailVerified;
        private Boolean isActive;
        private Boolean hasConsentPrivacy;
        private LocalDateTime createdDate;
    }

}
