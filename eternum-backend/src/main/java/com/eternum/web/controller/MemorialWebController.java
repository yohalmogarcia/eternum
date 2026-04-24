package com.eternum.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller web para vistas relacionadas con memoriales
 * Maneja las páginas Thymeleaf (no API REST)
 */
@Controller
@RequestMapping("/memorials")
public class MemorialWebController {

    /**
     * Página para crear un nuevo memorial
     * Requiere autenticación (configurado en SecurityConfig)
     */
    @GetMapping("/create")
    public String createMemorial() {
        return "pages/create-memorial";
    }

    /**
     * Página para editar un memorial existente
     * Solo el creador puede acceder (verificación en frontend)
     */
    @GetMapping("/edit/{id}")
    public String editMemorial() {
        return "pages/edit-memorial";
    }
}
