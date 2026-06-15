package com.gobigorgotrades.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A group order ("GO"): one artist + one release/era that joiners buy into.
 * In this project we keep the rule "1 GO = 1 shipment", so {@link #fulfillment}
 * (the shipment progress) lives here and is shared by every joiner in the GO.
 */
@Entity
@Table(name = "group_order")
@Getter
@Setter
@NoArgsConstructor
public class GroupOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Postgres BIGSERIAL
    private Long id;

    @Column(nullable = false, length = 120)
    private String artist;

    @Column(nullable = false, length = 160)
    private String title;

    // Stored as text (e.g. "ALBUM"), not an ordinal, so reordering the enum is safe.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GoType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Fulfillment fulfillment = Fulfillment.ORDERED;

    /** Estimated arrival of the whole shipment (nullable while still open). */
    private LocalDate eta;

    @Column(length = 500)
    private String note;

    // Default payment deadlines for this GO. When an order is created, these are
    // copied onto each of its four Payment rows (which can then be overridden).
    @Column(name = "due_items")
    private LocalDate dueItems;
    @Column(name = "due_ems")
    private LocalDate dueEms;
    @Column(name = "due_customs")
    private LocalDate dueCustoms;
    @Column(name = "due_doms")
    private LocalDate dueDoms;

    /**
     * Ordered list of member names for this release, used by joiners to build
     * their priority ranking. Modelled as a simple ordered collection of
     * strings rather than its own entity to keep things light.
     */
    @ElementCollection
    @CollectionTable(
            name = "group_order_member",
            joinColumns = @JoinColumn(name = "group_order_id"))
    @OrderColumn(name = "position")
    @Column(name = "member", length = 80)
    private List<String> members = new ArrayList<>();

    /** When true, this GO needs member sorting: joiners rank members in the portal. */
    @Column(name = "sorting_enabled", nullable = false)
    private boolean sortingEnabled = false;

    // Set automatically by Hibernate on insert.
    @Column(name = "created_at", nullable = false, updatable = false,
            insertable = false)
    private Instant createdAt;
}
