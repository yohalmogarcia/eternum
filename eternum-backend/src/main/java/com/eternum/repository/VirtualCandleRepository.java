package com.eternum.repository;

import com.eternum.entity.VirtualCandle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VirtualCandleRepository extends JpaRepository<VirtualCandle, Integer> {

    List<VirtualCandle> findByFkMemorialOrderByLitDateDesc(Integer fkMemorial);

    @Query("SELECT COUNT(vc) FROM VirtualCandle vc WHERE vc.fkMemorial = :memorialId")
    Long countByMemorialId(Integer memorialId);

    @Query("SELECT vc FROM VirtualCandle vc WHERE vc.fkMemorial = :memorialId AND vc.litDate >= :since ORDER BY vc.litDate DESC")
    List<VirtualCandle> findRecentByMemorial(Integer memorialId, LocalDateTime since);

}
