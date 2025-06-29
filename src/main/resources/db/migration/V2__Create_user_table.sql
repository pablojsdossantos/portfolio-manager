-- Create user table in the portfolio-manager schema
CREATE TABLE "portfolio-manager".user (
    id SERIAL PRIMARY KEY,
    email VARCHAR(128) NOT NULL,
    first_name VARCHAR(128) NOT NULL,
    last_name VARCHAR(128) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    create_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add trigger to update the update_dt column on update
CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.update_dt = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_user_modtime
BEFORE UPDATE ON "portfolio-manager".user
FOR EACH ROW
EXECUTE FUNCTION update_modified_column();