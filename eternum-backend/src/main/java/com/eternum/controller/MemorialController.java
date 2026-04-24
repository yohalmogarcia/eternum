package com.eternum.controller;

import com.eternum.dto.CondolenceDTO;
import com.eternum.dto.MemorialDTO;
import com.eternum.dto.VirtualCandleDTO;
import com.eternum.entity.Memorial;
import com.eternum.entity.User;
import com.eternum.service.CondolenceService;
import com.eternum.service.MemorialService;
import com.eternum.service.UserService;
import com.eternum.service.VirtualCandleService;
import com.eternum.util.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/memorials")
public class MemorialController {

    @Autowired
    private MemorialService memorialService;

    @Autowired
    private CondolenceService condolenceService;

    @Autowired
    private VirtualCandleService candleService;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ResponseHandler.ApiResponse<List<MemorialDTO.ListResponse>>> getMemorials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        var memorials = memorialService.findActiveMemorials(PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(memorialService::toListResponse)
                .collect(Collectors.toList());
        return ResponseHandler.success(memorials);
    }

    @GetMapping("/featured")
    public ResponseEntity<ResponseHandler.ApiResponse<List<MemorialDTO.ListResponse>>> getFeaturedMemorials() {
        var memorials = memorialService.findFeaturedMemorials()
                .stream()
                .map(memorialService::toListResponse)
                .collect(Collectors.toList());
        return ResponseHandler.success(memorials);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseHandler.ApiResponse<List<MemorialDTO.ListResponse>>> searchMemorials(
            @RequestParam String query) {
        var memorials = memorialService.searchMemorials(query)
                .stream()
                .map(memorialService::toListResponse)
                .collect(Collectors.toList());
        return ResponseHandler.success(memorials);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHandler.ApiResponse<MemorialDTO.Response>> getMemorial(@PathVariable Integer id) {
        return memorialService.findByIdWithUser(id)
                .map(memorial -> ResponseHandler.success(memorialService.toResponse(memorial)))
                .orElse(ResponseHandler.notFound("Memorial no encontrado"));
    }

    @PostMapping
    public ResponseEntity<ResponseHandler.ApiResponse<MemorialDTO.Response>> createMemorial(
            @Valid @RequestBody MemorialDTO.CreateRequest request,
            @RequestHeader("X-User-Email") String userEmail) {
        // Verificar que se proporcionó el email del usuario
        if (userEmail == null || userEmail.isEmpty()) {
            return ResponseHandler.unauthorized("Debe iniciar sesión para crear un memorial");
        }
        
        // Buscar el usuario por email
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Memorial memorial = memorialService.createMemorial(user, request);
        return ResponseHandler.created(memorialService.toResponse(memorial), "Memorial creado exitosamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseHandler.ApiResponse<MemorialDTO.Response>> updateMemorial(
            @PathVariable Integer id,
            @Valid @RequestBody MemorialDTO.UpdateRequest request,
            @RequestHeader("X-User-Email") String userEmail) {
        try {
            // Verificar que se proporcionó el email del usuario
            if (userEmail == null || userEmail.isEmpty()) {
                return ResponseHandler.unauthorized("Debe iniciar sesión para editar un memorial");
            }
            
            // Buscar el memorial
            Memorial memorial = memorialService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Memorial no encontrado"));
            
            // Verificar que el usuario es el creador del memorial
            if (memorial.getUser() == null || !userEmail.equals(memorial.getUser().getEmail())) {
                return ResponseHandler.unauthorized("No tiene permiso para editar este memorial");
            }
            
            Memorial updatedMemorial = memorialService.updateMemorial(id, request);
            return ResponseHandler.success(memorialService.toResponse(updatedMemorial), "Memorial actualizado exitosamente");
        } catch (RuntimeException e) {
            return ResponseHandler.notFound(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseHandler.ApiResponse<Void>> deleteMemorial(@PathVariable Integer id) {
        try {
            memorialService.deleteMemorial(id);
            return ResponseHandler.success(null, "Memorial eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseHandler.notFound(e.getMessage());
        }
    }

    @PostMapping("/{id}/condolences")
    public ResponseEntity<ResponseHandler.ApiResponse<CondolenceDTO.Response>> addCondolence(
            @PathVariable Integer id,
            @Valid @RequestBody CondolenceDTO.CreateRequest request) {
        return memorialService.findById(id)
                .map(memorial -> {
                    // Placeholder: obtener usuario autenticado (puede ser null)
                    User user = null;
                    var condolence = condolenceService.createCondolence(memorial, user, request);
                    return ResponseHandler.created(condolenceService.toResponse(condolence), "Condolencia agregada exitosamente");
                })
                .orElse(ResponseHandler.notFound("Memorial no encontrado"));
    }

    @GetMapping("/{id}/condolences")
    public ResponseEntity<ResponseHandler.ApiResponse<List<CondolenceDTO.Response>>> getCondolences(
            @PathVariable Integer id) {
        var condolences = condolenceService.findByMemorial(id)
                .stream()
                .map(condolenceService::toResponse)
                .collect(Collectors.toList());
        return ResponseHandler.success(condolences);
    }

    @PostMapping("/{id}/candles")
    public ResponseEntity<ResponseHandler.ApiResponse<VirtualCandleDTO.Response>> lightCandle(
            @PathVariable Integer id,
            @Valid @RequestBody VirtualCandleDTO.LightRequest request) {
        return memorialService.findById(id)
                .map(memorial -> {
                    // Placeholder: obtener usuario autenticado (puede ser null)
                    User user = null;
                    var candle = candleService.lightCandle(memorial, user, request.getCandleMessage());
                    return ResponseHandler.created(candleService.toResponse(candle), "Vela encendida exitosamente");
                })
                .orElse(ResponseHandler.notFound("Memorial no encontrado"));
    }

    @GetMapping("/{id}/candles")
    public ResponseEntity<ResponseHandler.ApiResponse<List<VirtualCandleDTO.Response>>> getCandles(
            @PathVariable Integer id) {
        var candles = candleService.findByMemorial(id)
                .stream()
                .map(candleService::toResponse)
                .collect(Collectors.toList());
        return ResponseHandler.success(candles);
    }

    @GetMapping("/{id}/candles/count")
    public ResponseEntity<ResponseHandler.ApiResponse<java.util.Map<String, Long>>> getCandleCount(@PathVariable Integer id) {
        Long count = candleService.countByMemorial(id);
        return ResponseHandler.success(java.util.Map.of("count", count));
    }

}
