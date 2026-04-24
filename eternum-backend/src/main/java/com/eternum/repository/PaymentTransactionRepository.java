package com.eternum.repository;

import com.eternum.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Integer> {

    List<PaymentTransaction> findByFkUserOrderByCreatedDateDesc(Integer fkUser);

    List<PaymentTransaction> findByFkSubscriptionOrderByCreatedDateDesc(Integer fkSubscription);

    Optional<PaymentTransaction> findByExternalTransactionId(String externalTransactionId);

}
