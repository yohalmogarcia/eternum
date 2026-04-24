package com.eternum.repository;

import com.eternum.entity.Condolence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CondolenceRepository extends JpaRepository<Condolence, Integer> {

    List<Condolence> findByFkMemorialOrderByCreatedDateDesc(Integer fkMemorial);

    Page<Condolence> findByFkMemorialAndIsApprovedTrueOrderByCreatedDateDesc(Integer fkMemorial, Pageable pageable);

    List<Condolence> findByFkAuthorUserOrderByCreatedDateDesc(Integer fkAuthorUser);

}
