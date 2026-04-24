package com.eternum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "condolence")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Condolence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_condolence")
    private Integer pkCondolence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_memorial", nullable = false)
    private Memorial memorial;

    @Column(name = "fk_memorial", insertable = false, updatable = false)
    private Integer fkMemorial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_author_user")
    private User authorUser;

    @Column(name = "fk_author_user", insertable = false, updatable = false)
    private Integer fkAuthorUser;

    @Column(name = "author_name", nullable = false, length = 200)
    private String authorName;

    @Column(name = "author_email", nullable = false, length = 255)
    private String authorEmail;

    @Column(name = "message", nullable = false, columnDefinition = "text")
    private String message;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved = true;

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
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }

}
