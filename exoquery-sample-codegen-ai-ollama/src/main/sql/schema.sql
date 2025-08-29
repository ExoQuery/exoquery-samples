-- Consistent PostgreSQL-style schema
-- Conventions used:
-- - lower_snake_case for all identifiers
-- - plural table names
-- - primary key column named `id` in every table
-- - foreign keys named <entity>_id
-- - *_at for TIMESTAMP/TIMESTAMPTZ, *_on for DATE, *_time for TIME
-- - simple types only (INTEGER, VARCHAR, CHAR, DATE, TIME, TIMESTAMP, TIMESTAMPTZ)


-- USERS & ORGS ---------------------------------------------------------------------------

CREATE TABLE "UserProfiles" (
"userId" INTEGER PRIMARY KEY,
"firstName" VARCHAR(80) NOT NULL,
last_name VARCHAR(80) NOT NULL,
"emailAddress" VARCHAR(200) UNIQUE NOT NULL,
"isActive" INTEGER NOT NULL DEFAULT 1,
updated_at TIMESTAMP
);

CREATE TABLE "Organization_Accounts" (
"orgId" SERIAL PRIMARY KEY,
org_name VARCHAR(160) NOT NULL,
legal_name VARCHAR(200),
created_on DATE NOT NULL,
is_active BOOLEAN NOT NULL
);

CREATE TABLE org_accountmembers (
org_member_id SERIAL PRIMARY KEY,
org_id INTEGER NOT NULL REFERENCES "Organization_Accounts"("orgId") ON DELETE CASCADE,
user_id INTEGER NOT NULL REFERENCES "UserProfiles"("userId") ON DELETE CASCADE,
role_name VARCHAR(60) NOT NULL
);

-- Users
INSERT INTO "UserProfiles" ("userId", "firstName", last_name, "emailAddress", "isActive", updated_at) VALUES
  (1, 'Alice', 'Anderson', 'alice@example.com', 1, NULL),
  (2, 'Bob', 'Baker', 'bob@example.com', 1, CURRENT_TIMESTAMP),
  (3, 'Carol', 'Chen', 'carol@example.com', 0, NULL);

-- Organizations
INSERT INTO "Organization_Accounts" ("orgId", org_name, legal_name, created_on, is_active) VALUES
  (10, 'Acme Widgets', 'Acme Widgets LLC', DATE '2023-03-15', true),
  (11, 'Beta Labs', 'Beta Laboratories Inc.', DATE '2024-07-01', true),
  (12, 'Gamma Corp', 'Gamma Corporation', DATE '2022-11-20', false);

-- Organization members (roles within orgs)
INSERT INTO org_accountmembers (org_member_id, org_id, user_id, role_name) VALUES
  (2001, 10, 1, 'admin'),
  (2002, 10, 2, 'member'),
  (2003, 11, 2, 'admin'),
  (2004, 11, 3, 'member'),
  (2005, 12, 1, 'member'),
  (2006, 12, 3, 'admin');
