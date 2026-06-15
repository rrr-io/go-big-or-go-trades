package com.gobigorgotrades.domain;

/**
 * How the joiner wants the parcel sent. The chosen value drives which extra
 * fields are required (e.g. VINTED needs only a username, TRACKED needs an
 * address) and the DOMS amount. A null method means "not chosen yet".
 */
public enum ShippingMethod {
    VINTED,
    STANDARD,
    TRACKED,
    PICKUP
}
