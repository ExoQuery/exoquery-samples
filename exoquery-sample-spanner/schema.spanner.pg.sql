-- Sample schema for Google Cloud Spanner Emulator (PostgreSQL dialect)
-- Notes:
-- - Prefer simple supported types. Avoid SERIAL; use explicit BIGINT ids.
-- - DEFAULT CURRENT_TIMESTAMP is supported for TIMESTAMP.
-- - Foreign keys are created without ON DELETE CASCADE for maximum compatibility.

CREATE TABLE user_profiles (
  id BIGINT PRIMARY KEY,
  first_name VARCHAR(80) NOT NULL,
  last_name VARCHAR(80) NOT NULL,
  email_address VARCHAR(200) UNIQUE NOT NULL,
  is_active INT NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE organization_accounts (
  id BIGINT PRIMARY KEY,
  org_name VARCHAR(160) NOT NULL,
  legal_name VARCHAR(200),
  created_on DATE NOT NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE org_account_members (
  id BIGINT PRIMARY KEY,
  org_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  role_name VARCHAR(60) NOT NULL,
  joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Optional foreign keys (without cascading actions)
ALTER TABLE org_account_members
  ADD CONSTRAINT fk_org_account_members_org
  FOREIGN KEY (org_id) REFERENCES organization_accounts(id);

ALTER TABLE org_account_members
  ADD CONSTRAINT fk_org_account_members_user
  FOREIGN KEY (user_id) REFERENCES user_profiles(id);
