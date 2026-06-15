package com.gobigorgotrades.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * A person who joins group orders.
 *
 * <p>The {@link #token} is the heart of the access model: each joiner gets a
 * private, unguessable link (e.g. /j/{token}) that grants read access to ONLY
 * their own data, plus a few allowed edits. There is no login for joiners.
 * The token is unique and indexed, and can be regenerated to revoke the old link.
 */
@Entity
@Table(name = "joiner")
@Getter
@Setter
@NoArgsConstructor
public class Joiner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 120)
    private String handle;

    /** Where reminders are sent. The joiner can edit this from their portal. */
    @Column(length = 200)
    private String email;

    /** Private GOM-only note. Never exposed through the joiner portal. */
    @Column(name = "contact_internal", length = 300)
    private String contactInternal;

    /** High-entropy capability token. Unique so it can be looked up directly. */
    @Column(nullable = false, unique = true, length = 64)
    private String token;

    @Column(name = "created_at", nullable = false, updatable = false,
            insertable = false)
    private Instant createdAt;
}
