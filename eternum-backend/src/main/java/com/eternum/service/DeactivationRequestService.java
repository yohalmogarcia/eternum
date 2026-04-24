package com.eternum.service;

import com.eternum.dto.DeactivationRequestDTO;
import com.eternum.entity.DeactivationRequest;
import com.eternum.entity.User;
import com.eternum.repository.DeactivationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeactivationRequestService {

    private final DeactivationRequestRepository requestRepository;

    @Transactional
    public DeactivationRequest createRequest(User user, DeactivationRequestDTO.CreateRequest request) {
        DeactivationRequest req = new DeactivationRequest();
        req.setRequesterUser(user);
        req.setDeceasedFullName(request.getDeceasedFullName());
        req.setDeceasedEmail(request.getDeceasedEmail());
        req.setSocialMediaPlatform(request.getSocialMediaPlatform());
        req.setSocialMediaUsername(request.getSocialMediaUsername());
        req.setProofDocumentUrl(request.getProofDocumentUrl() != null ? request.getProofDocumentUrl() : "");
        req.setDeathCertificateUrl(request.getDeathCertificateUrl() != null ? request.getDeathCertificateUrl() : "");
        req.setRequestStatus("PENDING");
        req.setStatusNotes("");

        return requestRepository.save(req);
    }

    @Transactional(readOnly = true)
    public List<DeactivationRequest> findByRequester(Integer userId) {
        return requestRepository.findByFkRequesterUserOrderByCreatedDateDesc(userId);
    }

    @Transactional(readOnly = true)
    public Optional<DeactivationRequest> findById(Integer id) {
        return requestRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<DeactivationRequest> findByStatus(String status) {
        return requestRepository.findByRequestStatusOrderByCreatedDateDesc(status);
    }

    @Transactional
    public DeactivationRequest updateStatus(Integer requestId, String newStatus, String notes, Integer processedBy) {
        DeactivationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        request.setRequestStatus(newStatus);
        if (notes != null) {
            request.setStatusNotes(notes);
        }
        if ("APPROVED".equals(newStatus) || "REJECTED".equals(newStatus) || "COMPLETED".equals(newStatus)) {
            request.setProcessedDate(LocalDateTime.now());
            request.setProcessedBy(processedBy);
        }

        return requestRepository.save(request);
    }

    public DeactivationRequestDTO.Response toResponse(DeactivationRequest request) {
        return DeactivationRequestDTO.Response.builder()
                .pkDeactivationRequest(request.getPkDeactivationRequest())
                .fkRequesterUser(request.getFkRequesterUser())
                .deceasedFullName(request.getDeceasedFullName())
                .deceasedEmail(request.getDeceasedEmail())
                .socialMediaPlatform(request.getSocialMediaPlatform())
                .socialMediaUsername(request.getSocialMediaUsername())
                .proofDocumentUrl(request.getProofDocumentUrl())
                .deathCertificateUrl(request.getDeathCertificateUrl())
                .requestStatus(request.getRequestStatus())
                .statusNotes(request.getStatusNotes())
                .submittedDate(request.getSubmittedDate())
                .processedDate(request.getProcessedDate())
                .createdDate(request.getCreatedDate())
                .build();
    }

    public List<DeactivationRequestDTO.Response> toResponseList(List<DeactivationRequest> requests) {
        return requests.stream().map(this::toResponse).collect(Collectors.toList());
    }

}
