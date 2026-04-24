package com.eternum.repository;

import com.eternum.entity.DeactivationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeactivationRequestRepository extends JpaRepository<DeactivationRequest, Integer> {

    List<DeactivationRequest> findByFkRequesterUserOrderByCreatedDateDesc(Integer fkRequesterUser);

    List<DeactivationRequest> findByRequestStatusOrderByCreatedDateDesc(String requestStatus);

    @Query("SELECT r FROM DeactivationRequest r WHERE r.fkRequesterUser = :userId ORDER BY r.createdDate DESC")
    List<DeactivationRequest> findAllByRequester(Integer userId);

}
