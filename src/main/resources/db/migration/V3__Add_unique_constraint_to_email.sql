-- Add unique constraint to email field in user table
ALTER TABLE "portfolio-manager".user
ADD CONSTRAINT user_email_unique UNIQUE (email);