DROP TYPE IF EXISTS trade_type CASCADE;
CREATE TYPE trade_type AS ENUM ('BUY', 'SELL');

DROP TABLE IF EXISTS holdings CASCADE;
DROP TABLE IF EXISTS trades CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id         UUID PRIMARY KEY,
    balance    DECIMAL(20, 8) NOT NULL DEFAULT 10000,
    created_at TIMESTAMPTZ             DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS trades
(
    id                    UUID PRIMARY KEY        DEFAULT gen_random_uuid(),
    user_id               UUID           NOT NULL,
    trade_type            trade_type     NOT NULL,
    cryptocurrency_symbol VARCHAR(10)    NOT NULL,
    quantity              DECIMAL(20, 8) NOT NULL,
    price_per_unit        DECIMAL(20, 8) NOT NULL,
    fiat_currency         VARCHAR(4)     NOT NULL DEFAULT 'USD',
    profit_loss           DECIMAL(20, 8),
    timestamp             TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS holdings
(
    id                    UUID PRIMARY KEY        DEFAULT gen_random_uuid(),
    user_id               UUID           NOT NULL,
    cryptocurrency_symbol VARCHAR(10)    NOT NULL,
    quantity              DECIMAL(20, 8) NOT NULL,
    average_price         DECIMAL(20, 8) NOT NULL,
    fiat_currency         VARCHAR(4)     NOT NULL DEFAULT 'USD',
    updated_at            TIMESTAMPTZ    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    UNIQUE (user_id, cryptocurrency_symbol)
)