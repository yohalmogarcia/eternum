package com.eternum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "virtual_candle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCandle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_virtual_candle")
    private Integer pkVirtualCandle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_memorial", nullable = false)
    private Memorial memorial;

    @Column(name = "fk_memorial", insertable = false, updatable = false)
    private Integer fkMemorial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user")
    private User user;

    @Column(name = "fk_user", insertable = false, updatable = false)
    private Integer fkUser;

    @Column(name = "candle_message", nullable = false, length = 255)
    private String candleMessage;

    @Column(name = "lit_date", nullable = false)
    private LocalDateTime litDate;

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
        litDate = LocalDateTime.now();
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }

}
