package com.eternum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "deactivation_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeactivationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_deactivation_request")
    private Integer pkDeactivationRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_requester_user", nullable = false)
    private User requesterUser;

    @Column(name = "fk_requester_user", insertable = false, updatable = false)
    private Integer fkRequesterUser;

    @Column(name = "deceased_full_name", nullable = false, length = 400)
    private String deceasedFullName;

    @Column(name = "deceased_email", nullable = false, length = 255)
    private String deceasedEmail;

    @Column(name = "social_media_platform", nullable = false, length = 100)
    private String socialMediaPlatform;

    @Column(name = "social_media_username", nullable = false, length = 200)
    private String socialMediaUsername;

    @Column(name = "proof_document_url", nullable = false, length = 500)
    private String proofDocumentUrl;

    @Column(name = "death_certificate_url", nullable = false, length = 500)
    private String deathCertificateUrl;

    @Column(name = "request_status", nullable = false, length = 50)
    private String requestStatus = "PENDING";

    @Column(name = "status_notes", nullable = false, columnDefinition = "text")
    private String statusNotes;

    @Column(name = "submitted_date", nullable = false, updatable = false)
    private LocalDateTime submittedDate;

    @Column(name = "processed_date")
    private LocalDateTime processedDate;

    @Column(name = "processed_by", nullable = false)
    private Integer processedBy = 0;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy = 0;

    @Column(name = "updated_by", nullable = false)
    private Integer updatedBy = 0;

    @PrePersist
    protected void onCreate() {
        submittedDate = LocalDateTime.now();
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }

}
