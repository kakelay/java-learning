-- T24-like User Management Database Schema
-- Comprehensive user information storage with relationships

-- Users table (main authentication table)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT true,
    is_locked BOOLEAN DEFAULT false,
    failed_login_attempts INTEGER DEFAULT 0,
    last_login_date TIMESTAMP,
    password_changed_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

-- Roles table (RBAC - Role Based Access Control)
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    role_description VARCHAR(255),
    is_active BOOLEAN DEFAULT true,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

-- User Roles junction table (many-to-many relationship)
CREATE TABLE IF NOT EXISTS user_roles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    assigned_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    UNIQUE(user_id, role_id)
);

-- User Profiles table (extended user information)
CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    cid VARCHAR(20) UNIQUE, -- Customer ID like T24
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    full_name VARCHAR(100) GENERATED ALWAYS AS (first_name || ' ' || last_name) STORED,
    date_of_birth DATE,
    gender VARCHAR(10),
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    zip_code VARCHAR(20),
    phone VARCHAR(20),
    alternate_phone VARCHAR(20),
    profile_picture VARCHAR(500),
    cover_picture VARCHAR(500),
    bio TEXT,
    website VARCHAR(255),
    occupation VARCHAR(100),
    company VARCHAR(100),
    nationality VARCHAR(100),
    id_type VARCHAR(50), -- Passport, National ID, etc.
    id_number VARCHAR(50),
    id_expiry_date DATE,
    marital_status VARCHAR(20),
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    emergency_contact_relationship VARCHAR(50),
    preferred_language VARCHAR(10) DEFAULT 'en',
    timezone VARCHAR(50) DEFAULT 'UTC',
    is_profile_complete BOOLEAN DEFAULT false,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Permissions table (fine-grained permissions)
CREATE TABLE IF NOT EXISTS permissions (
    id BIGSERIAL PRIMARY KEY,
    permission_name VARCHAR(100) UNIQUE NOT NULL,
    permission_description VARCHAR(255),
    resource VARCHAR(100) NOT NULL, -- e.g., 'USER', 'ACCOUNT', 'TRANSACTION'
    action VARCHAR(50) NOT NULL, -- e.g., 'CREATE', 'READ', 'UPDATE', 'DELETE'
    is_active BOOLEAN DEFAULT true,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Role Permissions junction table
CREATE TABLE IF NOT EXISTS role_permissions (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    granted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    granted_by VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE,
    UNIQUE(role_id, permission_id)
);

-- Audit Log table (comprehensive audit trail like T24)
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGSERIAL PRIMARY KEY,
    table_name VARCHAR(100) NOT NULL,
    record_id BIGINT,
    operation VARCHAR(20) NOT NULL, -- INSERT, UPDATE, DELETE
    old_values JSONB,
    new_values JSONB,
    changed_fields TEXT[],
    user_id BIGINT,
    username VARCHAR(50),
    ip_address INET,
    user_agent TEXT,
    session_id VARCHAR(255),
    transaction_id VARCHAR(255),
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- User Sessions table (track active sessions)
CREATE TABLE IF NOT EXISTS user_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    session_id VARCHAR(255) UNIQUE NOT NULL,
    ip_address INET,
    user_agent TEXT,
    device_info JSONB,
    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiry_time TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Password History table (security requirement)
CREATE TABLE IF NOT EXISTS password_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    changed_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- User Preferences table
CREATE TABLE IF NOT EXISTS user_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    theme VARCHAR(20) DEFAULT 'light',
    language VARCHAR(10) DEFAULT 'en',
    timezone VARCHAR(50) DEFAULT 'UTC',
    email_notifications BOOLEAN DEFAULT true,
    sms_notifications BOOLEAN DEFAULT false,
    two_factor_enabled BOOLEAN DEFAULT false,
    two_factor_secret VARCHAR(255),
    date_format VARCHAR(20) DEFAULT 'YYYY-MM-DD',
    time_format VARCHAR(20) DEFAULT 'HH:mm:ss',
    currency VARCHAR(3) DEFAULT 'USD',
    items_per_page INTEGER DEFAULT 10,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Course table (existing table, keeping for compatibility)
CREATE TABLE IF NOT EXISTS course (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    difficulty_level VARCHAR(20),
    duration_hours INTEGER,
    price DECIMAL(10,2),
    is_active BOOLEAN DEFAULT true,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_phone ON users(phone);
CREATE INDEX IF NOT EXISTS idx_user_profiles_cid ON user_profiles(cid);
CREATE INDEX IF NOT EXISTS idx_user_profiles_user_id ON user_profiles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role_id ON user_roles(role_id);
CREATE INDEX IF NOT EXISTS idx_audit_log_table_record ON audit_log(table_name, record_id);
CREATE INDEX IF NOT EXISTS idx_audit_log_user_timestamp ON audit_log(user_id, operation_timestamp);
CREATE INDEX IF NOT EXISTS idx_user_sessions_user_id ON user_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_user_sessions_session_id ON user_sessions(session_id);

-- Insert default roles
INSERT INTO roles (role_name, role_description, created_by)
VALUES
    ('ADMIN', 'System Administrator with full access', 'SYSTEM'),
    ('USER', 'Regular user with basic access', 'SYSTEM'),
    ('MANAGER', 'Manager with elevated permissions', 'SYSTEM'),
    ('AUDITOR', 'Audit and compliance role', 'SYSTEM')
ON CONFLICT (role_name) DO NOTHING;

-- Insert default permissions
INSERT INTO permissions (permission_name, permission_description, resource, action)
VALUES
    ('USER_CREATE', 'Create new users', 'USER', 'CREATE'),
    ('USER_READ', 'View user information', 'USER', 'READ'),
    ('USER_UPDATE', 'Update user information', 'USER', 'UPDATE'),
    ('USER_DELETE', 'Delete users', 'USER', 'DELETE'),
    ('ROLE_MANAGE', 'Manage user roles', 'ROLE', 'MANAGE'),
    ('AUDIT_VIEW', 'View audit logs', 'AUDIT', 'READ'),
    ('SYSTEM_CONFIG', 'System configuration access', 'SYSTEM', 'CONFIGURE')
ON CONFLICT (permission_name) DO NOTHING;

-- Assign permissions to roles
INSERT INTO role_permissions (role_id, permission_id, granted_by)
SELECT r.id, p.id, 'SYSTEM'
FROM roles r
JOIN permissions p ON (
    (r.role_name = 'ADMIN' AND p.permission_name IN ('USER_CREATE','USER_READ','USER_UPDATE','USER_DELETE','ROLE_MANAGE','AUDIT_VIEW','SYSTEM_CONFIG'))
    OR (r.role_name = 'USER' AND p.permission_name IN ('USER_READ','USER_UPDATE'))
    OR (r.role_name = 'MANAGER' AND p.permission_name IN ('USER_CREATE','USER_READ','USER_UPDATE'))
    OR (r.role_name = 'AUDITOR' AND p.permission_name IN ('AUDIT_VIEW'))
)
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- Audit trigger function and triggers are skipped during boot initialization
-- to keep schema startup compatible with Spring Boot's SQL script parser.
-- They can be added manually later in a PostgreSQL client if audit logging is needed.


-- Create accounts table (child table of users)
-- One user can have multiple accounts (1:N relationship)

CREATE TABLE IF NOT EXISTS accounts (
                          id BIGSERIAL PRIMARY KEY,

                          account_no VARCHAR(30) UNIQUE NOT NULL,
                          account_name VARCHAR(100) NOT NULL,

                          account_type VARCHAR(50) NOT NULL,       -- SAVINGS, CURRENT, LOAN, FD
                          product_code VARCHAR(10),                -- 6001, 6002...
                          currency VARCHAR(3) NOT NULL,            -- USD, KHR

                          available_balance DECIMAL(18,2) DEFAULT 0.00
                              CHECK (available_balance >= 0),

                          ledger_balance DECIMAL(18,2) DEFAULT 0.00,

                          status VARCHAR(20) DEFAULT 'ACTIVE'
                              CHECK (status IN ('ACTIVE', 'INACTIVE', 'DORMANT', 'CLOSED', 'BLOCKED')),

                          open_date DATE DEFAULT CURRENT_DATE,
                          close_date DATE,

                          user_id BIGINT NOT NULL,

                          created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          created_by VARCHAR(50),
                          updated_by VARCHAR(50),

                          CONSTRAINT fk_account_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_accounts_user_id
    ON accounts(user_id);

CREATE INDEX IF NOT EXISTS idx_accounts_account_no
    ON accounts(account_no);