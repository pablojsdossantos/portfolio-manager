-- Create asset_event table in the portfolio-manager schema
CREATE TABLE "portfolio-manager".asset_event (
    event_id UUID PRIMARY KEY,
    asset_id BIGINT NOT NULL,
    event_type VARCHAR(20) NOT NULL,
    amount DECIMAL NOT NULL,
    event_date DATE NOT NULL,
    create_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (asset_id) REFERENCES "portfolio-manager".asset(id)
);

-- Create asset_history table in the portfolio-manager schema
CREATE TABLE "portfolio-manager".asset_history (
    asset_id BIGINT NOT NULL,
    date DATE NOT NULL,
    price DECIMAL NOT NULL,
    variation_1d DECIMAL,
    variation_30d DECIMAL,
    variation_60d DECIMAL,
    variation_180d DECIMAL,
    variation_360d DECIMAL,
    PRIMARY KEY (asset_id, date),
    FOREIGN KEY (asset_id) REFERENCES "portfolio-manager".asset(id)
);