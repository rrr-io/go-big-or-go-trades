package com.gobigorgotrades.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * The central unit of the model: one {@link Joiner} inside one
 * {@link GroupOrder}. It holds the requested {@link OrderItem}s, the four
 * {@link Payment}s, the shipping choice, and the priority ranking.
 *
 * <p>Mapped to a table called "orders" because ORDER is a reserved SQL keyword.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many orders belong to one joiner. LAZY so we don't always load the joiner.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "joiner_id", nullable = false)
    private Joiner joiner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_order_id", nullable = false)
    private GroupOrder groupOrder;

    // cascade = ALL + orphanRemoval: items live and die with their order.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    /** Joiner's member priority for sorting, position 0 = highest priority. */
    @ElementCollection
    @CollectionTable(
            name = "order_priority",
            joinColumns = @JoinColumn(name = "order_id"))
    @OrderColumn(name = "position")
    @Column(name = "member", length = 80)
    private List<String> priorities = new ArrayList<>();

    // --- Shipping: a chosen method plus conditional fields ---
    // null method = "not chosen yet" (the GOM dashboard flags these).
    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_method", length = 20)
    private ShippingMethod shippingMethod;

    @Column(name = "shipping_address", length = 400)
    private String shippingAddress;   // used by STANDARD / TRACKED
    @Column(name = "shipping_vinted", length = 120)
    private String shippingVinted;    // used by VINTED
    @Column(name = "shipping_note", length = 300)
    private String shippingNote;      // used by PICKUP

    // --- Per-joiner final shipment step ---
    @Enumerated(EnumType.STRING)
    @Column(name = "parcel_status", nullable = false, length = 20)
    private ParcelStatus parcelStatus = ParcelStatus.PREPARING;

    @Column(length = 120)
    private String tracking;

    /** GOM-only note. */
    @Column(length = 500)
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false,
            insertable = false)
    private Instant createdAt;

    // --- Helper methods keep both sides of the relationship in sync ---
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setOrder(this);
    }
}
