-- ===========================================================================
-- V1: initial schema.
-- Flyway runs this once and records it; it is the source of truth for the DB.
-- Column names and types here must match the JPA entity mappings, because the
-- app starts with hibernate.ddl-auto=validate (it checks, never alters).
-- ===========================================================================

-- Account-level payment methods (PayPal, Wise, ...) shown as payment instructions.
CREATE TABLE payment_method (
    id     BIGSERIAL PRIMARY KEY,
    label  VARCHAR(80)  NOT NULL,
    detail VARCHAR(200) NOT NULL
);

-- A group order: one artist + one release/era.
CREATE TABLE group_order (
    id          BIGSERIAL PRIMARY KEY,
    artist      VARCHAR(120) NOT NULL,
    title       VARCHAR(160) NOT NULL,
    type        VARCHAR(20)  NOT NULL,   -- GoType
    fulfillment VARCHAR(20)  NOT NULL,   -- Fulfillment
    eta         DATE,
    note        VARCHAR(500),
    due_items   DATE,                    -- default payment deadlines
    due_ems     DATE,
    due_customs DATE,
    due_doms    DATE,
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);

-- Ordered member list for a GO (used to build joiner priority rankings).
CREATE TABLE group_order_member (
    group_order_id BIGINT      NOT NULL REFERENCES group_order(id) ON DELETE CASCADE,
    position       INT         NOT NULL,
    member         VARCHAR(80) NOT NULL,
    PRIMARY KEY (group_order_id, position)
);

-- A joiner. The token is the private-link credential (unique + indexed).
CREATE TABLE joiner (
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(120) NOT NULL,
    handle           VARCHAR(120),
    email            VARCHAR(200),
    contact_internal VARCHAR(300),
    token            VARCHAR(64)  NOT NULL UNIQUE,
    created_at       TIMESTAMP    NOT NULL DEFAULT now()
);

-- The central unit: one joiner inside one GO. "orders" because ORDER is reserved.
CREATE TABLE orders (
    id               BIGSERIAL PRIMARY KEY,
    joiner_id        BIGINT NOT NULL REFERENCES joiner(id)      ON DELETE CASCADE,
    group_order_id   BIGINT NOT NULL REFERENCES group_order(id) ON DELETE CASCADE,
    shipping_method  VARCHAR(20),         -- ShippingMethod, null = not chosen
    shipping_address VARCHAR(400),
    shipping_vinted  VARCHAR(120),
    shipping_note    VARCHAR(300),
    parcel_status    VARCHAR(20) NOT NULL DEFAULT 'PREPARING',
    tracking         VARCHAR(120),
    note             VARCHAR(500),
    created_at       TIMESTAMP NOT NULL DEFAULT now()
);

-- Items requested within an order.
CREATE TABLE order_item (
    id          BIGSERIAL PRIMARY KEY,
    order_id    BIGINT        NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    description VARCHAR(200)  NOT NULL,
    qty         INT           NOT NULL,
    unit_amount NUMERIC(10,2) NOT NULL
);

-- The four payment phases per order. (order_id, type) is unique.
CREATE TABLE payment (
    id        BIGSERIAL PRIMARY KEY,
    order_id  BIGINT      NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    type      VARCHAR(20) NOT NULL,    -- PaymentType
    amount    NUMERIC(10,2),           -- null while TO_DEFINE
    status    VARCHAR(20) NOT NULL,    -- PaymentStatus
    due_date  DATE,
    paid_date DATE,
    UNIQUE (order_id, type)
);

-- Joiner's member priority ranking for an order (position 0 = highest).
CREATE TABLE order_priority (
    order_id BIGINT      NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    position INT         NOT NULL,
    member   VARCHAR(80) NOT NULL,
    PRIMARY KEY (order_id, position)
);

-- Helpful indexes for the most common lookups.
CREATE INDEX idx_orders_joiner       ON orders(joiner_id);
CREATE INDEX idx_orders_group_order  ON orders(group_order_id);
