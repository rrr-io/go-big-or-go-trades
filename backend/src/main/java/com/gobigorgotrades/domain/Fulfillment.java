package com.gobigorgotrades.domain;

/**
 * Shipment progress shared by ALL joiners of a group order.
 * The order matters: it doubles as the pipeline shown in the UI.
 * The per-joiner final step ("shipped") lives on {@link Order#parcelStatus}.
 */
public enum Fulfillment {
    ORDERED,      // bulk order placed with the Korean store / proxy
    AT_KADDY,     // items arrived at the Korean forwarding address (kaddy)
    ON_THE_WAY,   // shipped from Korea, in international transit
    ON_HAND       // received and customs-cleared, ready to sort and ship
}
