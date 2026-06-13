-- akibaroom 0단계 스키마
-- 전역 규칙: PK UUID v7(BINARY(16)) / No FK(논리 참조 + 인덱스) / 금액·수량 BIGINT / 시각 DATETIME(6) UTC

CREATE TABLE member (
    id         BINARY(16)  NOT NULL,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE money_account (
    id         BINARY(16)  NOT NULL,
    member_id  BINARY(16)  NOT NULL,
    balance    BIGINT      NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_money_account_member_id (member_id),
    CONSTRAINT chk_money_account_balance CHECK (balance >= 0)
);

CREATE TABLE money_ledger (
    id            BINARY(16)  NOT NULL,
    account_id    BINARY(16)  NOT NULL,
    type          VARCHAR(20) NOT NULL,
    amount        BIGINT      NOT NULL,
    balance_after BIGINT      NOT NULL,
    order_id      BINARY(16)  NULL,
    created_at    DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_money_ledger_account_id (account_id)
);

CREATE TABLE goods_stock (
    id                 BINARY(16)   NOT NULL,
    name               VARCHAR(255) NOT NULL,
    price              BIGINT       NOT NULL,
    total_quantity     BIGINT       NOT NULL,
    remaining_quantity BIGINT       NOT NULL,
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_goods_stock_price CHECK (price > 0),
    CONSTRAINT chk_goods_stock_total_quantity CHECK (total_quantity > 0),
    CONSTRAINT chk_goods_stock_remaining_quantity CHECK (remaining_quantity >= 0)
);

CREATE TABLE purchase_order (
    id         BINARY(16)  NOT NULL,
    member_id  BINARY(16)  NOT NULL,
    goods_id   BINARY(16)  NOT NULL,
    quantity   BIGINT      NOT NULL,
    amount     BIGINT      NOT NULL,
    status     VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_purchase_order_member_id (member_id),
    KEY idx_purchase_order_goods_id (goods_id),
    CONSTRAINT chk_purchase_order_quantity CHECK (quantity > 0)
);
