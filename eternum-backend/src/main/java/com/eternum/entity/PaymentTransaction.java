package com.eternum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_payment_transaction")
    private Integer pkPaymentTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_subscription", nullable = false)
    private Subscription subscription;

    @Column(name = "fk_subscription", insertable = false, updatable = false)
    private Integer fkSubscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;

    @Column(name = "fk_user", insertable = false, updatable = false)
    private Integer fkUser;

    @Column(name = "amount", nullable = false, precision = 16, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "USD";

    @Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType = "SUBSCRIPTION";

    @Column(name = "payment_status", nullable = false, length = 50)
    private String paymentStatus = "PENDING";

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(name = "external_transaction_id", nullable = false, length = 255)
    private String externalTransactionId;

    @Column(name = "payment_gateway_response", nullable = false, columnDefinition = "text")
    private String paymentGatewayResponse;

    @Column(name = "processed_date")
    private LocalDateTime processedDate;

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
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }

}
