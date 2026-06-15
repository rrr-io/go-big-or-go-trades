package com.gobigorgotrades.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * One of the four payment phases for an order (ITEMS / EMS / CUSTOMS / DOMS).
 * Modelled as its own rows (one per type) rather than four columns, so adding
 * a future phase doesn't require reshaping the orders table. The (order, type)
 * pair is unique. amount is nullable while the status is TO_DEFINE.
 */
@Entity
@Table(name = "payment",
       uniqueConstraints = @UniqueConstraint(columnNames = {"order_id", "type"}))
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentType type;

    // Null until the amount is known (e.g. customs before the parcel arrives).
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "paid_date")
    private LocalDate paidDate;
}
