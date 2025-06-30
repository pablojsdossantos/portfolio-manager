-- Add active field to asset_event table
ALTER TABLE "portfolio-manager".asset_event
ADD COLUMN active BOOLEAN DEFAULT TRUE;

-- Create a function to update update_dt
CREATE OR REPLACE FUNCTION "portfolio-manager".update_update_dt()
RETURNS TRIGGER AS $$
BEGIN
    NEW.update_dt = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create a trigger to automatically update update_dt when a record is updated
CREATE TRIGGER update_asset_event_update_dt
BEFORE UPDATE ON "portfolio-manager".asset_event
FOR EACH ROW
EXECUTE FUNCTION "portfolio-manager".update_update_dt();