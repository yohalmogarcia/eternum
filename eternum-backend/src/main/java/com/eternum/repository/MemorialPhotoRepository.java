package com.eternum.repository;

import com.eternum.entity.MemorialPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemorialPhotoRepository extends JpaRepository<MemorialPhoto, Integer> {

    List<MemorialPhoto> findByFkMemorialOrderByDisplayOrderAsc(Integer fkMemorial);

    @Query("SELECT p FROM MemorialPhoto p WHERE p.fkMemorial = :memorialId AND p.isMainPhoto = true")
    MemorialPhoto findMainPhotoByMemorialId(Integer memorialId);

    @Modifying
    @Query("UPDATE MemorialPhoto p SET p.isMainPhoto = false WHERE p.fkMemorial = :memorialId")
    void clearMainPhotoFlag(Integer memorialId);

}
