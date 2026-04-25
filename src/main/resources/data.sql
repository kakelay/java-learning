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
VALUES (1, 'CID001', 'System', 'Administrator', '+1234567890', 'en', true, 'SYSTEM')
ON CONFLICT (user_id) DO NOTHING;

-- Insert admin preferences
INSERT INTO user_preferences (user_id, theme, language, timezone, email_notifications, items_per_page)
VALUES (1, 'dark', 'en', 'UTC', true, 20)
ON CONFLICT (user_id) DO NOTHING;

-- Insert sample user
INSERT INTO users (username, password, email, phone, is_active, created_by)
VALUES ('john.doe', '$2a$10$8K2L0Hkd1Jc8Q8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8', 'john.doe@company.com', '+1987654321', true, 'SYSTEM')
ON CONFLICT (username) DO NOTHING;

-- Insert sample user profile
INSERT INTO user_profiles (user_id, cid, first_name, last_name, date_of_birth, gender, address_line1, city, state, country, zip_code, phone, occupation, company, nationality, marital_status, preferred_language, is_profile_complete, created_by)
VALUES (2, 'CID002', 'John', 'Doe', '1990-05-15', 'Male', '123 Main Street', 'New York', 'NY', 'USA', '10001', '+1987654321', 'Software Engineer', 'Tech Corp', 'American', 'Single', 'en', true, 'SYSTEM')
ON CONFLICT (user_id) DO NOTHING;

-- Insert sample user preferences
INSERT INTO user_preferences (user_id, theme, language, timezone, email_notifications, sms_notifications, currency, items_per_page)
VALUES (2, 'light', 'en', 'America/New_York', true, false, 'USD', 10)
ON CONFLICT (user_id) DO NOTHING;

-- Assign roles to users
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1), -- Admin gets ADMIN role
       (2, 2)
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

INSERT INTO user_profiles (user_id, cid, first_name, last_name, date_of_birth, gender, address_line1, city, state, country, zip_code, phone, occupation, company, nationality, marital_status, preferred_language, is_profile_complete, created_by) VALUES (3, 'CID003', 'Jane', 'Smith', '1988-11-30', 'Female', '221B Baker Street', 'London', 'Greater London', 'UK', 'NW1 6XE', '+442071234567', 'Product Manager', 'Global Solutions', 'British', 'Married', 'en', true, 'SYSTEM') ON CONFLICT (user_id) DO NOTHING;
INSERT INTO user_profiles (user_id, cid, first_name, last_name, date_of_birth, gender, address_line1, city, state, country, zip_code, phone, occupation, company, nationality, marital_status, preferred_language, is_profile_complete, created_by) VALUES (4, 'CID004', 'Sarah', 'Manager', '1985-07-24', 'Female', '456 Market Street', 'San Francisco', 'CA', 'USA', '94105', '+14155550102', 'Operations Manager', 'Enterprise Co', 'American', 'Married', 'en', true, 'SYSTEM') ON CONFLICT (user_id) DO NOTHING;
INSERT INTO user_profiles (user_id, cid, first_name, last_name, date_of_birth, gender, address_line1, city, state, country, zip_code, phone, occupation, company, nationality, marital_status, preferred_language, is_profile_complete, created_by) VALUES (5, 'CID005', 'Tom', 'Auditor', '1992-03-10', 'Male', '789 Audit Avenue', 'Chicago', 'IL', 'USA', '60601', '+13125550103', 'Compliance Auditor', 'AuditCorp', 'American', 'Single', 'en', true, 'SYSTEM') ON CONFLICT (user_id) DO NOTHING;

INSERT INTO user_preferences (user_id, theme, language, timezone, email_notifications, sms_notifications, currency, items_per_page) VALUES
(3, 'light', 'en', 'Europe/London', true, false, 'GBP', 15),
(4, 'dark', 'en', 'America/Los_Angeles', true, true, 'USD', 20),
(5, 'light', 'en', 'America/Chicago', false, true, 'USD', 10)
ON CONFLICT (user_id) DO NOTHING;

-- Assign additional roles to users
INSERT INTO user_roles (user_id, role_id)
VALUES
(3, 2), -- Jane gets USER role
(4, 3), -- Sarah gets MANAGER role
(5, 4)
ON CONFLICT (user_id, role_id) DO NOTHING;

-- Insert sample sessions
INSERT INTO user_sessions (user_id, session_id, ip_address, user_agent, device_info, login_time, last_activity, expiry_time, is_active) VALUES
(2, 'sess-1001', '192.168.1.10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', '{"device":"desktop","browser":"chrome"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '8 hours', true),
(3, 'sess-1002', '51.140.20.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', '{"device":"laptop","browser":"safari"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '8 hours', true),
(4, 'sess-1003', '34.201.33.44', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', '{"device":"laptop","browser":"firefox"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '8 hours', true)
ON CONFLICT (session_id) DO NOTHING;

-- Insert password history
INSERT INTO password_history (user_id, password_hash, changed_by) VALUES
(1, '$2a$10$8K2L0Hkd1Jc8Q8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8', 'SYSTEM'),
(2, '$2a$10$8K2L0Hkd1Jc8Q8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8', 'SYSTEM'),
(3, '$2a$10$8K2L0Hkd1Jc8Q8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8Y8', 'SYSTEM');

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