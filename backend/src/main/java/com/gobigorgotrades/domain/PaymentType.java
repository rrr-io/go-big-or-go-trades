package com.gobigorgotrades.domain;

/**
 * The four payment phases a joiner goes through, in chronological order.
 * Each is tracked separately because amounts become known at different times.
 */
public enum PaymentType {
    ITEMS,    // cost of the goods (due when the GO closes)
    EMS,      // international shipping from Korea (joiner's share)
    CUSTOMS,  // import duties (variable; may be zero)
    DOMS      // domestic shipping to the joiner (depends on chosen method)
}
