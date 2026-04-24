package com.eternum.service;

import com.eternum.dto.UserDTO;
import com.eternum.entity.User;
import com.eternum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(UserDTO.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user = new User();
        user.setEmail(request.getEmail().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone() != null ? request.getPhone() : "");
        user.setEmailVerificationToken(UUID.randomUUID().toString());
        user.setHasConsentPrivacy(request.getHasConsentPrivacy() != null ? request.getHasConsentPrivacy() : false);
        if (Boolean.TRUE.equals(user.getHasConsentPrivacy())) {
            user.setConsentDate(LocalDateTime.now());
        }

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Transactional
    public boolean verifyEmail(String token) {
        Optional<User> userOpt = userRepository.findByEmailVerificationToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIsEmailVerified(true);
            user.setEmailVerificationToken("");
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    public String initiatePasswordReset(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email.toLowerCase());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPasswordResetToken(UUID.randomUUID().toString());
            user.setPasswordResetExpires(LocalDateTime.now().plusHours(24));
            userRepository.save(user);
            return user.getPasswordResetToken();
        }
        return null;
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<User> userOpt = userRepository.findByPasswordResetToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPasswordResetExpires() != null && user.getPasswordResetExpires().isAfter(LocalDateTime.now())) {
                user.setPasswordHash(passwordEncoder.encode(newPassword));
                user.setPasswordResetToken("");
                user.setPasswordResetExpires(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public boolean validatePassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPasswordHash());
    }

    public UserDTO.Response toResponse(User user) {
        return UserDTO.Response.builder()
                .pkUser(user.getPkUser())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .isEmailVerified(user.getIsEmailVerified())
                .isActive(user.getIsActive())
                .hasConsentPrivacy(user.getHasConsentPrivacy())
                .createdDate(user.getCreatedDate())
                .build();
    }

}
