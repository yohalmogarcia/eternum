package com.eternum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "memorial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Memorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_memorial")
    private Integer pkMemorial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;

    @Column(name = "fk_user", insertable = false, updatable = false)
    private Integer fkUser;

    @Column(name = "deceased_name", nullable = false, length = 200)
    private String deceasedName;

    @Column(name = "deceased_last_name", nullable = false, length = 200)
    private String deceasedLastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "death_date", nullable = false)
    private LocalDate deathDate;

    @Column(name = "biography", nullable = false, columnDefinition = "text")
    private String biography;

    @Column(name = "epitaph", nullable = false, length = 500)
    private String epitaph;

    @Column(name = "profile_photo_url", nullable = false, length = 500)
    private String profilePhotoUrl;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    @Column(name = "candle_count", nullable = false)
    private Integer candleCount = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy = 0;

    @Column(name = "updated_by", nullable = false)
    private Integer updatedBy = 0;

    @OneToMany(mappedBy = "memorial", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemorialPhoto> photos = new ArrayList<>();

    @OneToMany(mappedBy = "memorial", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Condolence> condolences = new ArrayList<>();

    @OneToMany(mappedBy = "memorial", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VirtualCandle> candles = new ArrayList<>();

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
