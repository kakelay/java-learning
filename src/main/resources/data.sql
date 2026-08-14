-- Initial data for T24-like User Management System

-- Insert default roles if missing
INSERT INTO roles (role_name, role_description, is_active, created_by)
VALUES
('ADMIN', 'System Administrator with full access', true, 'SYSTEM'),
('USER', 'Regular user with basic access', true, 'SYSTEM'),
('MANAGER', 'Manager with elevated permissions', true, 'SYSTEM'),
('AUDITOR', 'Audit and compliance role', true, 'SYSTEM')
ON CONFLICT (role_name) DO NOTHING;

-- Insert default permissions if missing
INSERT INTO permissions (permission_name, permission_description, resource, action, is_active)
VALUES
('USER_CREATE', 'Create new users', 'USER', 'CREATE', true),
('USER_READ', 'View user information', 'USER', 'READ', true),
('USER_UPDATE', 'Update user information', 'USER', 'UPDATE', true),
('USER_DELETE', 'Delete users', 'USER', 'DELETE', true),
('ROLE_MANAGE', 'Manage user roles', 'ROLE', 'MANAGE', true),
('AUDIT_VIEW', 'View audit logs', 'AUDIT', 'READ', true),
('SYSTEM_CONFIG', 'System configuration access', 'SYSTEM', 'CONFIGURE', true)
ON CONFLICT (permission_name) DO NOTHING;

-- Insert default admin user
INSERT INTO users (username, password, email, phone, is_active, created_by)
VALUES ('admin', '$2a$10$8K2L0Hkd1Jc8Q8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8', 'admin@company.com', '+1234567890', true, 'SYSTEM')
ON CONFLICT (username) DO NOTHING;

-- Insert admin profile
INSERT INTO user_profiles (user_id, cid, first_name, last_name, phone, preferred_language, is_profile_complete, created_by)
SELECT u.id, 'CID001', 'System', 'Administrator', '+1234567890', 'en', true, 'SYSTEM'
FROM users u
WHERE u.username = 'admin'
ON CONFLICT (user_id) DO NOTHING;

-- Insert admin preferences
INSERT INTO user_preferences (user_id, theme, language, timezone, email_notifications, items_per_page)
SELECT u.id, 'dark', 'en', 'UTC', true, 20
FROM users u
WHERE u.username = 'admin'
ON CONFLICT (user_id) DO NOTHING;

-- Insert sample user
INSERT INTO users (username, password, email, phone, is_active, created_by)
VALUES ('john.doe', '$2a$10$8K2L0Hkd1Jc8Q8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8', 'john.doe@company.com', '+1987654321', true, 'SYSTEM')
ON CONFLICT (username) DO NOTHING;

-- Insert sample user profile
INSERT INTO user_profiles (user_id, cid, first_name, last_name, date_of_birth, gender, address_line1, city, state, country, zip_code, phone, occupation, company, nationality, marital_status, preferred_language, is_profile_complete, created_by)
SELECT u.id, 'CID002', 'John', 'Doe', '1990-05-15', 'Male', '123 Main Street', 'New York', 'NY', 'USA', '10001', '+1987654321', 'Software Engineer', 'Tech Corp', 'American', 'Single', 'en', true, 'SYSTEM'
FROM users u
WHERE u.username = 'john.doe'
ON CONFLICT (user_id) DO NOTHING;

-- Insert sample user preferences
INSERT INTO user_preferences (user_id, theme, language, timezone, email_notifications, sms_notifications, currency, items_per_page)
SELECT u.id, 'light', 'en', 'America/New_York', true, false, 'USD', 10
FROM users u
WHERE u.username = 'john.doe'
ON CONFLICT (user_id) DO NOTHING;

-- Assign roles to users
INSERT INTO user_roles (user_id, role_id)
SELECT admin_user.id, admin_role.id
FROM users admin_user
JOIN roles admin_role ON admin_role.role_name = 'ADMIN'
WHERE admin_user.username = 'admin'
UNION ALL
SELECT sample_user.id, user_role.id
FROM users sample_user
JOIN roles user_role ON user_role.role_name = 'USER'
WHERE sample_user.username = 'john.doe'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- Insert sample courses
INSERT INTO course (id, name, author, description, category, difficulty_level, duration_hours, price, is_active, created_by) VALUES
(1, 'Spring Boot Fundamentals', 'John Doe', 'Learn the basics of Spring Boot framework', 'Programming', 'Beginner', 20, 99.99, true, 'SYSTEM'),
(2, 'Advanced Java Programming', 'Jane Smith', 'Deep dive into advanced Java concepts', 'Programming', 'Advanced', 40, 199.99, true, 'SYSTEM'),
(3, 'Database Design Principles', 'Bob Johnson', 'Learn how to design efficient databases', 'Database', 'Intermediate', 25, 149.99, true, 'SYSTEM'),
(4, 'Web Development with React', 'Alice Brown', 'Build modern web applications with React', 'Web Development', 'Intermediate', 35, 179.99, true, 'SYSTEM')
ON CONFLICT (id) DO NOTHING;

-- Insert sample audit log entries
INSERT INTO audit_log (table_name, record_id, operation, username, operation_timestamp) VALUES
('users', 1, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('users', 2, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('user_profiles', 1, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('user_profiles', 2, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('course', 1, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('course', 2, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('course', 3, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('course', 4, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP);

-- Insert additional sample users
INSERT INTO users (username, password, email, phone, is_active, created_by) VALUES
('jane.smith', '$2a$10$8K2L0Hkd1Jc8Q8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8', 'jane.smith@company.com', '+14255550101', true, 'SYSTEM'),
('sarah.manager', '$2a$10$8K2L0Hkd1Jc8Q8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8', 'sarah.manager@company.com', '+14255550102', true, 'SYSTEM'),
('tom.auditor', '$2a$10$8K2L0Hkd1Jc8Q8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8', 'tom.auditor@company.com', '+14255550103', true, 'SYSTEM')
ON CONFLICT DO NOTHING;

INSERT INTO user_profiles (user_id, cid, first_name, last_name, date_of_birth, gender, address_line1, city, state, country, zip_code, phone, occupation, company, nationality, marital_status, preferred_language, is_profile_complete, created_by)
SELECT u.id, 'CID003', 'Jane', 'Smith', '1988-11-30', 'Female', '221B Baker Street', 'London', 'Greater London', 'UK', 'NW1 6XE', '+442071234567', 'Product Manager', 'Global Solutions', 'British', 'Married', 'en', true, 'SYSTEM'
FROM users u
WHERE u.username = 'jane.smith'
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO user_profiles (user_id, cid, first_name, last_name, date_of_birth, gender, address_line1, city, state, country, zip_code, phone, occupation, company, nationality, marital_status, preferred_language, is_profile_complete, created_by)
SELECT u.id, 'CID004', 'Sarah', 'Manager', '1985-07-24', 'Female', '456 Market Street', 'San Francisco', 'CA', 'USA', '94105', '+14155550102', 'Operations Manager', 'Enterprise Co', 'American', 'Married', 'en', true, 'SYSTEM'
FROM users u
WHERE u.username = 'sarah.manager'
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO user_profiles (user_id, cid, first_name, last_name, date_of_birth, gender, address_line1, city, state, country, zip_code, phone, occupation, company, nationality, marital_status, preferred_language, is_profile_complete, created_by)
SELECT u.id, 'CID005', 'Tom', 'Auditor', '1992-03-10', 'Male', '789 Audit Avenue', 'Chicago', 'IL', 'USA', '60601', '+13125550103', 'Compliance Auditor', 'AuditCorp', 'American', 'Single', 'en', true, 'SYSTEM'
FROM users u
WHERE u.username = 'tom.auditor'
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO user_preferences (user_id, theme, language, timezone, email_notifications, sms_notifications, currency, items_per_page)
SELECT u.id, 'light', 'en', 'Europe/London', true, false, 'GBP', 15
FROM users u
WHERE u.username = 'jane.smith'
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO user_preferences (user_id, theme, language, timezone, email_notifications, sms_notifications, currency, items_per_page)
SELECT u.id, 'dark', 'en', 'America/Los_Angeles', true, true, 'USD', 20
FROM users u
WHERE u.username = 'sarah.manager'
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO user_preferences (user_id, theme, language, timezone, email_notifications, sms_notifications, currency, items_per_page)
SELECT u.id, 'light', 'en', 'America/Chicago', false, true, 'USD', 10
FROM users u
WHERE u.username = 'tom.auditor'
ON CONFLICT (user_id) DO NOTHING;

-- Assign additional roles to users
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.role_name = 'USER'
WHERE u.username = 'jane.smith'
UNION ALL
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.role_name = 'MANAGER'
WHERE u.username = 'sarah.manager'
UNION ALL
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.role_name = 'AUDITOR'
WHERE u.username = 'tom.auditor'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- Insert sample sessions
INSERT INTO user_sessions (user_id, session_id, ip_address, user_agent, device_info, login_time, last_activity, expiry_time, is_active)
SELECT u.id, 'sess-1001', '192.168.1.10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', '{"device":"desktop","browser":"chrome"}'::jsonb, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '8 hours', true
FROM users u WHERE u.username = 'john.doe'
UNION ALL
SELECT u.id, 'sess-1002', '51.140.20.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', '{"device":"laptop","browser":"safari"}'::jsonb, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '8 hours', true
FROM users u WHERE u.username = 'jane.smith'
UNION ALL
SELECT u.id, 'sess-1003', '34.201.33.44', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', '{"device":"laptop","browser":"firefox"}'::jsonb, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '8 hours', true
FROM users u WHERE u.username = 'sarah.manager'
ON CONFLICT (session_id) DO NOTHING;

-- Insert password history
INSERT INTO password_history (user_id, password_hash, changed_by)
SELECT u.id, '$2a$10$8K2L0Hkd1Jc8Q8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8', 'SYSTEM'
FROM users u WHERE u.username = 'admin'
UNION ALL
SELECT u.id, '$2a$10$8K2L0Hkd1Jc8Q8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8', 'SYSTEM'
FROM users u WHERE u.username = 'john.doe'
UNION ALL
SELECT u.id, '$2a$10$8K2L0Hkd1Jc8Q8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8', 'SYSTEM'
FROM users u WHERE u.username = 'jane.smith';

-- Insert additional audit log entries
INSERT INTO audit_log (table_name, record_id, operation, username, operation_timestamp) VALUES
('users', 3, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('users', 4, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('users', 5, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('user_profiles', 3, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('user_profiles', 4, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('user_profiles', 5, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('user_preferences', 3, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('user_preferences', 4, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP),
('user_preferences', 5, 'INSERT', 'SYSTEM', CURRENT_TIMESTAMP);


--- Insert data accounts user for testing

INSERT INTO accounts (
    account_no,
    account_name,
    account_type,
    product_code,
    currency,
    available_balance,
    ledger_balance,
    status,
    open_date,
    user_id,
    created_by,
    updated_by
)
VALUES

-- User 1 : Admin Customer (3 accounts)
('AMK00000001', 'Admin Saving USD', 'SAVINGS', '6001', 'USD',
 5000.00, 5000.00, 'ACTIVE', CURRENT_DATE, 1, 'SYSTEM', 'SYSTEM'),

('AMK00000002', 'Admin Saving KHR', 'SAVINGS', '6001', 'KHR',
 2000000.00, 2000000.00, 'ACTIVE', CURRENT_DATE, 1, 'SYSTEM', 'SYSTEM'),

('AMK00000003', 'Admin Current Account', 'CURRENT', '6002', 'USD',
 10000.00, 10000.00, 'ACTIVE', CURRENT_DATE, 1, 'SYSTEM', 'SYSTEM'),


-- User 2 (2 accounts)
('AMK00000004', 'Kakelay Saving USD', 'SAVINGS', '6001', 'USD',
 1500.00, 1500.00, 'ACTIVE', CURRENT_DATE, 2, 'SYSTEM', 'SYSTEM'),

('AMK00000005', 'Kakelay Fixed Deposit', 'FD', '6003', 'USD',
 5000.00, 5000.00, 'ACTIVE', CURRENT_DATE, 2, 'SYSTEM', 'SYSTEM'),


-- User 3 (3 accounts)
('AMK00000006', 'Menghour Saving USD', 'SAVINGS', '6001', 'USD',
 3000.00, 3000.00, 'ACTIVE', CURRENT_DATE, 3, 'SYSTEM', 'SYSTEM'),

('AMK00000007', 'Menghour Saving KHR', 'SAVINGS', '6001', 'KHR',
 800000.00, 800000.00, 'ACTIVE', CURRENT_DATE, 3, 'SYSTEM', 'SYSTEM'),

('AMK00000008', 'Menghour Loan Account', 'LOAN', '6004', 'USD',
 0.00, 15000.00, 'ACTIVE', CURRENT_DATE, 3, 'SYSTEM', 'SYSTEM'),


-- User 4 (2 accounts)
('AMK00000009', 'Ousing Saving USD', 'SAVINGS', '6001', 'USD',
 2500.00, 2500.00, 'ACTIVE', CURRENT_DATE, 4, 'SYSTEM', 'SYSTEM'),

('AMK00000010', 'Ousing Current USD', 'CURRENT', '6002', 'USD',
 6000.00, 6000.00, 'ACTIVE', CURRENT_DATE, 4, 'SYSTEM', 'SYSTEM'),


-- User 5 (2 accounts)
('AMK00000011', 'Channithona Saving USD', 'SAVINGS', '6001', 'USD',
 1200.00, 1200.00, 'INACTIVE', CURRENT_DATE, 5, 'SYSTEM', 'SYSTEM'),

('AMK00000012', 'Channithona Loan Account', 'LOAN', '6004', 'USD',
 0.00, 9000.00, 'INACTIVE', CURRENT_DATE, 5, 'SYSTEM', 'SYSTEM'),


-- User 6 (3 accounts)
('AMK00000013', 'CoffeeHub Saving USD', 'SAVINGS', '6001', 'USD',
 3500.00, 3500.00, 'ACTIVE', CURRENT_DATE, 6, 'SYSTEM', 'SYSTEM'),

('AMK00000014', 'CoffeeHub Current USD', 'CURRENT', '6002', 'USD',
 7000.00, 7000.00, 'ACTIVE', CURRENT_DATE, 6, 'SYSTEM', 'SYSTEM'),

('AMK00000015', 'CoffeeHub Fixed Deposit', 'FD', '6003', 'USD',
 15000.00, 15000.00, 'ACTIVE', CURRENT_DATE, 6, 'SYSTEM', 'SYSTEM'),


-- User 7 (2 accounts)
('AMK00000016', 'User7 Saving KHR', 'SAVINGS', '6001', 'KHR',
 900000.00, 900000.00, 'ACTIVE', CURRENT_DATE, 7, 'SYSTEM', 'SYSTEM'),

('AMK00000017', 'User7 Saving USD', 'SAVINGS', '6001', 'USD',
 1800.00, 1800.00, 'ACTIVE', CURRENT_DATE, 7, 'SYSTEM', 'SYSTEM'),


-- User 8 (2 accounts)
('AMK00000018', 'Kakelay1805 Saving USD', 'SAVINGS', '6001', 'USD',
 2200.00, 2200.00, 'ACTIVE', CURRENT_DATE, 8, 'SYSTEM', 'SYSTEM'),

('AMK00000019', 'Kakelay1805 Current USD', 'CURRENT', '6002', 'USD',
 4500.00, 4500.00, 'ACTIVE', CURRENT_DATE, 8, 'SYSTEM', 'SYSTEM'),


-- User 9 (2 accounts)
('AMK00000020', 'Menghour Example Saving USD', 'SAVINGS', '6001', 'USD',
 1000.00, 1000.00, 'ACTIVE', CURRENT_DATE, 9, 'SYSTEM', 'SYSTEM'),

('AMK00000021', 'Menghour Example FD', 'FD', '6003', 'USD',
 8000.00, 8000.00, 'ACTIVE', CURRENT_DATE, 9, 'SYSTEM', 'SYSTEM'),


-- User 10 (2 accounts)
('AMK00000022', 'Niza Saving USD', 'SAVINGS', '6001', 'USD',
 500.00, 500.00, 'INACTIVE', CURRENT_DATE, 10, 'SYSTEM', 'SYSTEM'),

('AMK00000023', 'Niza Current KHR', 'CURRENT', '6002', 'KHR',
 300000.00, 300000.00, 'INACTIVE', CURRENT_DATE, 10, 'SYSTEM', 'SYSTEM')
ON CONFLICT (account_no) DO NOTHING;