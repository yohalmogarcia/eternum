package com.eternum.controller;

import com.eternum.dto.UserDTO;
import com.eternum.entity.User;
import com.eternum.service.UserService;
import com.eternum.util.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseHandler.ApiResponse<UserDTO.Response>> register(
            @Valid @RequestBody UserDTO.RegisterRequest request) {
        try {
            User user = userService.register(request);
            return ResponseHandler.created(userService.toResponse(user), "Usuario registrado exitosamente");
        } catch (RuntimeException e) {
            return ResponseHandler.badRequest(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseHandler.ApiResponse<Map<String, Object>>> login(
            @Valid @RequestBody UserDTO.LoginRequest request) {
        try {
            User user = userService.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

            if (!userService.validatePassword(user, request.getPassword())) {
                return ResponseHandler.unauthorized("Credenciales inválidas");
            }

            if (!user.getIsEmailVerified()) {
                return ResponseHandler.forbidden("Debe verificar su email antes de iniciar sesión");
            }

            Map<String, Object> data = Map.of(
                    "user", userService.toResponse(user),
                    "token", "placeholder-jwt-token"
            );

            return ResponseHandler.success(data, "Inicio de sesión exitoso");
        } catch (RuntimeException e) {
            return ResponseHandler.badRequest(e.getMessage());
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ResponseHandler.ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        if (userService.verifyEmail(token)) {
            return ResponseHandler.success(null, "Email verificado exitosamente");
        }
        return ResponseHandler.badRequest("Token de verificación inválido o expirado");
    }

    @PostMapping("/password-reset-request")
    public ResponseEntity<ResponseHandler.ApiResponse<Map<String, String>>> requestPasswordReset(
            @Valid @RequestBody UserDTO.PasswordResetRequest request) {
        String token = userService.initiatePasswordReset(request.getEmail());
        if (token != null) {
            return ResponseHandler.success(Map.of("token", token), "Se ha enviado un enlace de recuperación a su email");
        }
        return ResponseHandler.success(null, "Si el email existe, recibirá instrucciones");
    }

    @PostMapping("/password-reset")
    public ResponseEntity<ResponseHandler.ApiResponse<Void>> resetPassword(
            @Valid @RequestBody UserDTO.PasswordUpdateRequest request) {
        if (userService.resetPassword(request.getToken(), request.getNewPassword())) {
            return ResponseHandler.success(null, "Contraseña actualizada exitosamente");
        }
        return ResponseHandler.badRequest("Token inválido o expirado");
    }

}
