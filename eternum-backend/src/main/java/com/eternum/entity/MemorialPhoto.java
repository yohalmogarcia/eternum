package com.eternum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "memorial_photo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemorialPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_memorial_photo")
    private Integer pkMemorialPhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_memorial", nullable = false)
    private Memorial memorial;

    @Column(name = "fk_memorial", insertable = false, updatable = false)
    private Integer fkMemorial;

    @Column(name = "photo_url", nullable = false, length = 500)
    private String photoUrl;

    @Column(name = "caption", nullable = false, length = 255)
    private String caption;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @Column(name = "is_main_photo", nullable = false)
    private Boolean isMainPhoto = false;

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
