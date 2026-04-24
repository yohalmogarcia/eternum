package com.eternum.repository;

import com.eternum.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    List<Subscription> findByFkUser(Integer fkUser);

    @Query("SELECT s FROM Subscription s WHERE s.fkUser = :userId AND s.isActive = true")
    Optional<Subscription> findActiveByUserId(Integer userId);

    @Query("SELECT s FROM Subscription s WHERE s.fkUser = :userId AND s.planType = 'FREE'")
    Optional<Subscription> findFreeSubscriptionByUserId(Integer userId);

}
