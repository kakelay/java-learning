# Learn Spring Boot

This is a demo project for learning Spring Boot, developed by in28minutes.

## Overview

A comprehensive web application built with Spring Boot that demonstrates enterprise-level features including a T24-like user management system, authentication, todo management, and external service integration. The application showcases modern Spring Boot development practices with JPA, PostgreSQL, and RESTful API design.

## Prerequisites

- Java 17 or higher
- Maven 3.6+ (or use the included Maven wrapper `./mvnw`)
- Docker (for running Zipkin and other services)

## Technologies Used

- Java 17
- Spring Boot 3.3.4
- Spring Web MVC
- Spring Data JPA with Hibernate
- PostgreSQL Database
- Spring Boot Actuator
- Spring Boot Validation
- Tomcat Embed Jasper (for JSP support)
- H2 Database (alternative)
- SpringDoc OpenAPI (Swagger UI)
- Lombok
- Micrometer Tracing with Zipkin

## How to Run

### 1. Start Zipkin (for distributed tracing)

The application is configured to send traces to Zipkin. Start Zipkin using Docker:

```bash
docker run -d -p 9411:9411 --name zipkin openzipkin/zipkin
```

Zipkin UI will be available at http://localhost:9411

### 2. Run the Application

```bash
./mvnw spring-boot:run
```

Or with a specific profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 3. Access the Application

- Main application: http://localhost:8080
- Actuator health check: http://localhost:8080/actuator/health
- Actuator metrics: http://localhost:8080/actuator/metrics

## Configuration Profiles

The application supports multiple profiles:

- `dev` (development) - uses `application-dev.properties`
- `prod` (production) - uses `application-prod.properties`
- `uat` (user acceptance testing) - uses `application-uat.properties`

Activate a profile by setting `spring.profiles.active` in `application.properties` or via command line as shown above.

## Features

- **User Management System**: T24-like comprehensive user management with roles, permissions, and profiles
- **RESTful APIs**: Complete set of user management APIs (get by ID, username, email, list all, create, update)
- **Schema-based management APIs**: user profile, role, permission, and audit log endpoints aligned with `schema.sql`
- User authentication and session management
- Todo list CRUD operations
- Internationalization support (English, Chinese, Khmer, Dutch, Vietnamese)
- RESTful API endpoints
- API documentation via Swagger UI at http://localhost:8080/swagger-ui.html
- Application monitoring with Spring Boot Actuator
- Distributed tracing with Zipkin
- External currency service integration
- PostgreSQL database integration with JPA

## API Endpoints

### User Management APIs

- `GET /v1/user` - Get default user information
- `GET /v1/user/{id}` - Get user by ID
- `GET /v1/user/username/{username}` - Get user by username
- `GET /v1/user/email/{email}` - Get user by email address
- `GET /v1/users` - Get all users
- `GET /v1/users/active` - Get all active users
- `POST /v1/user` - Create a new user
- `PUT /v1/user/{id}` - Update an existing user

### Schema-Based APIs

- `GET /api/user-profile/{id}` - Get a user profile by profile ID
- `GET /api/user-profile/user/{userId}` - Get a user profile by user ID
- `GET /api/user-profile/cid/{cid}` - Get a user profile by CID
- `GET /api/user-profile/search?name={name}` - Search user profiles by name
- `GET /api/user-profile/incomplete` - Get all incomplete user profiles
- `GET /api/roles/all` - Get all roles
- `GET /api/roles/active` - Get active roles only
- `GET /api/roles/{id}` - Get a specific role by ID
- `GET /api/roles/user/{userId}` - Get roles assigned to a specific user
- `GET /api/permissions/all` - Get all permissions
- `GET /api/permissions/{id}` - Get a permission by ID
- `GET /api/permissions/resource/{resource}` - Get permissions by resource name
- `GET /api/audit/logs` - Get all audit log entries
- `GET /api/audit/logs/table/{tableName}` - Get audit log entries for a specific table
- `GET /api/audit/logs/record/{tableName}/{recordId}` - Get audit trail for a specific record
- `GET /api/audit/logs/user/{userId}` - Get audit log entries for a specific user

### Schema-Based API Examples

```bash
curl -X GET "http://localhost:8080/api/user-profile/user/1" -H "Content-Type: application/json"
curl -X GET "http://localhost:8080/api/roles/all" -H "Content-Type: application/json"
curl -X GET "http://localhost:8080/api/permissions/resource/USER" -H "Content-Type: application/json"
curl -X GET "http://localhost:8080/api/audit/logs/table/users" -H "Content-Type: application/json"
```

### Todo Management APIs

- `GET /login` - Login page
- `POST /login` - Authenticate user
- `GET /logout` - Logout
- `GET /list-todos` - List user's todos
- `GET /add-todo` - Add new todo form
- `POST /add-todo` - Create new todo
- `GET /delete-todo` - Delete a todo
- `GET /update-todo` - Update todo form
- `POST /update-todo` - Update todo

### Testing the APIs

You can test the user management APIs using curl or any HTTP client:

```bash
# Get user by ID
curl -X GET "http://localhost:8080/v1/user/1" -H "Content-Type: application/json"

# Get user by username
curl -X GET "http://localhost:8080/v1/user/username/admin" -H "Content-Type: application/json"

# Get user by email
curl -X GET "http://localhost:8080/v1/user/email/admin@company.com" -H "Content-Type: application/json"

# Get all users
curl -X GET "http://localhost:8080/v1/users" -H "Content-Type: application/json"

# Get active users only
curl -X GET "http://localhost:8080/v1/users/active" -H "Content-Type: application/json"

# Create a new user
curl -X POST "http://localhost:8080/v1/user" -H "Content-Type: application/json" -d '{
  "username": "new.user",
  "password": "Password123",
  "email": "new.user@example.com",
  "phone": "+1234567890",
  "firstName": "New",
  "lastName": "User",
  "theme": "light",
  "language": "en",
  "timezone": "UTC"
}'

# Update an existing user
curl -X PUT "http://localhost:8080/v1/user/1" -H "Content-Type: application/json" -d '{
  "email": "updated.email@example.com",
  "phone": "+1987654321",
  "city": "New City"
}'
```

**Note**: Most User Management APIs are GET requests and do not require request bodies. The new `POST /v1/user` and `PUT /v1/user/{id}` endpoints require JSON request bodies.

### Sample Request Bodies

#### Create User Request (POST /v1/user)

```json
{
  "username": "new.user",
  "password": "Password123",
  "email": "new.user@example.com",
  "phone": "+1234567890",
  "firstName": "New",
  "lastName": "User",
  "addressLine1": "123 Main St",
  "city": "Anytown",
  "state": "CA",
  "country": "USA",
  "zipCode": "12345",
  "bio": "A new user in the system",
  "website": "https://example.com",
  "theme": "light",
  "language": "en",
  "timezone": "UTC"
}
```

#### Update User Request (PUT /v1/user/{id})

```json
{
  "username": "updated.user",
  "email": "updated.email@example.com",
  "phone": "+1987654321",
  "firstName": "Updated",
  "lastName": "User",
  "addressLine1": "456 Updated St",
  "city": "New City",
  "state": "NY",
  "country": "USA",
  "zipCode": "67890",
  "bio": "Updated bio",
  "website": "https://updated.com",
  "theme": "dark",
  "language": "es",
  "timezone": "EST",
  "active": true,
  "locked": false
}
```

### Sample API Responses

#### Get User by ID Response

```json
{
  "referenceNumber": "550e8400-e29b-41d4-a716-446655440000",
  "responseCode": "00",
  "status": "Success",
  "message": "User retrieved successfully",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@company.com",
    "phone": "+1234567890",
    "status": "ACTIVE",
    "createdDate": "2024-01-15T10:30:00Z",
    "lastLoginDate": "2024-01-20T14:25:00Z",
    "profile": {
      "firstName": "System",
      "lastName": "Administrator",
      "address": "123 Admin Street",
      "city": "Admin City",
      "country": "US",
      "occupation": "System Administrator"
    },
    "preferences": {
      "theme": "dark",
      "language": "en",
      "timezone": "UTC"
    },
    "roles": [
      {
        "name": "ADMIN",
        "description": "System Administrator"
      }
    ]
  }
}
```

#### Get All Users Response

```json
{
  "referenceNumber": "550e8400-e29b-41d4-a716-446655440001",
  "responseCode": "00",
  "status": "Success",
  "message": "Users retrieved successfully",
  "data": [
    {
      "id": 1,
      "username": "admin",
      "email": "admin@company.com",
      "status": "ACTIVE"
    },
    {
      "id": 2,
      "username": "john.doe",
      "email": "john.doe@company.com",
      "status": "ACTIVE"
    },
    {
      "id": 3,
      "username": "jane.smith",
      "email": "jane.smith@company.com",
      "status": "INACTIVE"
    }
  ]
}
```

All API responses follow a consistent format:

```json
{
  "referenceNumber": "uuid",
  "responseCode": "00",
  "status": "Success",
  "message": "Operation successful",
  "data": { ... }
}
```

## Database

The application supports both H2 (default) and PostgreSQL databases.

### H2 Database (Default)

The application uses H2 in-memory database by default. Schema is defined in `src/main/resources/schema.sql`.

- H2 Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

### PostgreSQL Database

To use PostgreSQL instead of H2:

#### 1. Install PostgreSQL

Download and install PostgreSQL from [postgresql.org](https://www.postgresql.org/download/) or use Docker:

```bash
docker run --name postgres -e POSTGRES_PASSWORD=mypassword -d -p 5432:5432 postgres
```

**Note**: If you get a "port is already allocated" error, check for existing PostgreSQL containers:

```bash
docker ps -a | findstr postgres
docker stop <container_name>
docker rm <container_name>
```

#### 2. Create Database

```sql
CREATE DATABASE myfirstwebapp;
```

Or using Docker exec:

```bash
docker exec postgres psql -U postgres -c "CREATE DATABASE myfirstwebapp;"
```

#### 3. Update Configuration

In `src/main/resources/application.properties`, comment out the H2 configuration and uncomment the PostgreSQL configuration:

```properties
# H2 Configuration (comment out)
# spring.h2.console.enabled=true
# spring.h2.console.path=/h2-console
# spring.datasource.url=jdbc:h2:mem:testdb
# spring.datasource.driver-class-name=org.h2.Driver
# spring.datasource.username=sa
# spring.datasource.password=

# PostgreSQL Configuration (uncomment and update)
spring.datasource.url=jdbc:postgresql://localhost:5432/myfirstwebapp
spring.datasource.username=postgres
spring.datasource.password=your_password_here
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# For schema.sql initialization
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=never
```

#### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The schema will be automatically created from `src/main/resources/schema.sql`.

## Database Schema

The application uses a comprehensive T24-like user management system with the following entities:

### Core Entities

- **Users**: Basic user information (username, email, phone, status)
- **User Profiles**: Extended user information (address, occupation, nationality, etc.)
- **User Preferences**: User settings (theme, language, timezone, notifications)
- **Roles**: User roles (ADMIN, USER, MANAGER, AUDITOR)
- **Permissions**: Fine-grained permissions for role-based access control
- **User Roles**: Many-to-many relationship between users and roles
- **Password History**: Track password changes for security
- **User Sessions**: Active user sessions tracking
- **Audit Log**: Comprehensive audit trail for all operations
- **Courses**: Learning management system integration

### Sample Data

The application includes sample data for testing:

- **Admin User**: username: `admin`, email: `admin@company.com`
- **Regular Users**: `john.doe`, `jane.smith`, `sarah.manager`, `tom.auditor`
- **Roles**: ADMIN, USER, MANAGER, AUDITOR
- **Permissions**: User management, role management, audit viewing, system configuration

## Monitoring and Observability

- **Actuator**: Provides health checks, metrics, and info endpoints
- **Zipkin**: Collects and visualizes distributed traces
- **Grafana**: Configuration files are provided in `grafana/` folder for dashboard setup

To run Grafana with Docker:

```bash
docker run -d -p 3000:3000 --name grafana grafana/grafana
```

Grafana UI will be available at http://localhost:3000 (default credentials: admin/admin)

## Project Structure

- `src/main/java`: Java source code
  - Controllers, services, DTOs, configuration
- `src/main/resources`: Application properties, messages, database schema
- `src/test/java`: Unit and integration tests
- `src/main/resources/META-INF/resources/WEB-INF/jsp`: JSP view templates
- `grafana/`: Grafana provisioning configuration</content>
  <parameter name="filePath">e:\Todo\Learn Java Spring Boot\learn-spring-boot\README.md
