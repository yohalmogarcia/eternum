package com.eternum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "social_media_search")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialMediaSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_social_media_search")
    private Integer pkSocialMediaSearch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;

    @Column(name = "fk_user", insertable = false, updatable = false)
    private Integer fkUser;

    @Column(name = "search_query", nullable = false, length = 400)
    private String searchQuery;

    @Column(name = "platform_searched", nullable = false, length = 100)
    private String platformSearched;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "search_results_json", nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> searchResultsJson;

    @Column(name = "results_count", nullable = false)
    private Integer resultsCount = 0;

    @Column(name = "search_date", nullable = false)
    private LocalDateTime searchDate;

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
        searchDate = LocalDateTime.now();
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }

}
