package com.gobigorgotrades.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An account-level way for joiners to pay the GOM (e.g. PayPal, Wise, Revolut).
 * These are shown as payment instructions in the joiner portal whenever a
 * balance is outstanding.
 */
@Entity
@Table(name = "payment_method")
@Getter
@Setter
@NoArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String label;   // e.g. "PayPal"

    @Column(nullable = false, length = 200)
    private String detail;  // e.g. "paypal.me/yourname (F&F)"
}
