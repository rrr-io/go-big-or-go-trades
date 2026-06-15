package com.gobigorgotrades.domain;

/** State of a single payment phase. */
public enum PaymentStatus {
    DUE,        // amount known, waiting to be paid
    PAID,
    NA,         // not applicable -> shown as 0 in the portal (e.g. no customs)
    TO_DEFINE   // amount not known yet (typical for customs before arrival)
}
