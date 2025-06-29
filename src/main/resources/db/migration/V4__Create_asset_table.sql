-- Create asset table in the portfolio-manager schema
CREATE TABLE "portfolio-manager".asset (
    id SERIAL PRIMARY KEY,
    ticker VARCHAR(20) NOT NULL,
    name VARCHAR(256) NOT NULL,
    country VARCHAR(10) NOT NULL,
    category VARCHAR(64) NOT NULL,
    active BOOLEAN DEFAULT TRUE
);