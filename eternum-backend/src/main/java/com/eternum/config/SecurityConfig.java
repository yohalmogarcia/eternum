package com.eternum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad de Spring Security
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean para codificación de contraseñas con BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración de la cadena de filtros de seguridad
     * Permite acceso público a recursos estáticos y endpoints de API
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos - permitir primero
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()
                // Páginas públicas
                .requestMatchers("/", "/login", "/register", "/deactivation-request").permitAll()
                // Listado y detalle de memoriales - público (manejado por HomeController)
                .requestMatchers("/memorials", "/memorial/**").permitAll()
                // Crear memorial - accesible pero el formulario solo funciona con autenticación
                .requestMatchers("/memorials/create").permitAll()
                // Editar memorial - accesible pero el formulario solo funciona con autenticación
                .requestMatchers("/memorials/edit/**").permitAll()
                // API REST de usuarios - login y registro son públicos
                .requestMatchers("/api/users/login", "/api/users/register", "/api/users/verify-email", "/api/users/password-reset-request", "/api/users/password-reset").permitAll()
                .requestMatchers("/api/users/**").authenticated()
                // API REST de memoriales - todos públicos (la autenticación se valida en el controlador con JWT)
                .requestMatchers("/api/memorials", "/api/memorials/**").permitAll()
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline' https://cdn.tailwindcss.com https://code.jquery.com https://cdnjs.cloudflare.com; style-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com https://fonts.googleapis.com; font-src 'self' https://cdnjs.cloudflare.com https://fonts.gstatic.com; img-src 'self' data: https:"))
            );

        return http.build();
    }
}
