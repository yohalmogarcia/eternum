package com.eternum.controller;

import com.eternum.dto.DeactivationRequestDTO;
import com.eternum.entity.User;
import com.eternum.service.DeactivationRequestService;
import com.eternum.service.UserService;
import com.eternum.util.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deactivation-requests")
public class DeactivationRequestController {

    @Autowired
    private DeactivationRequestService requestService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ResponseHandler.ApiResponse<DeactivationRequestDTO.Response>> createRequest(
            @Valid @RequestBody DeactivationRequestDTO.CreateRequest request) {
        // Placeholder: obtener usuario autenticado
        User user = new User();
        user.setPkUser(1);

        var req = requestService.createRequest(user, request);
        return ResponseHandler.created(requestService.toResponse(req), "Solicitud creada exitosamente");
    }

    @GetMapping("/my-requests")
    public ResponseEntity<ResponseHandler.ApiResponse<List<DeactivationRequestDTO.Response>>> getMyRequests() {
        // Placeholder: obtener usuario autenticado
        Integer userId = 1;

        var requests = requestService.toResponseList(requestService.findByRequester(userId));
        return ResponseHandler.success(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHandler.ApiResponse<DeactivationRequestDTO.Response>> getRequest(
            @PathVariable Integer id) {
        return requestService.findById(id)
                .map(req -> ResponseHandler.success(requestService.toResponse(req)))
                .orElse(ResponseHandler.notFound("Solicitud no encontrada"));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseHandler.ApiResponse<List<DeactivationRequestDTO.Response>>> getByStatus(
            @PathVariable String status) {
        var requests = requestService.toResponseList(requestService.findByStatus(status.toUpperCase()));
        return ResponseHandler.success(requests);
    }

}
