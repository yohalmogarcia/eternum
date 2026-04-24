package com.eternum.repository;

import com.eternum.entity.Memorial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemorialRepository extends JpaRepository<Memorial, Integer> {

    List<Memorial> findByFkUser(Integer fkUser);

    Page<Memorial> findByIsActiveTrueOrderByCreatedDateDesc(Pageable pageable);

    @Query("SELECT m FROM Memorial m WHERE m.isFeatured = true AND m.isActive = true ORDER BY m.createdDate DESC")
    List<Memorial> findFeaturedMemorials();

    @Query("SELECT m FROM Memorial m WHERE LOWER(m.deceasedName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.deceasedLastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Memorial> searchMemorials(String query);

    @Modifying
    @Query("UPDATE Memorial m SET m.candleCount = m.candleCount + 1 WHERE m.pkMemorial = :memorialId")
    void incrementCandleCount(Integer memorialId);

    @Query("SELECT m FROM Memorial m LEFT JOIN FETCH m.user WHERE m.pkMemorial = :id")
    Optional<Memorial> findByIdWithUser(Integer id);

}
