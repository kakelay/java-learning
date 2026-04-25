# Learn Spring Boot

This is a demo project for learning Spring Boot, developed by in28minutes.

## Overview

A simple web application built with Spring Boot that demonstrates essential features including user authentication, todo management, and external service integration.

## Prerequisites

- Java 17 or higher
- Maven 3.6+ (or use the included Maven wrapper `./mvnw`)
- Docker (for running Zipkin and other services)

## Technologies Used

- Java 17
- Spring Boot 3.3.4
- Spring Web MVC
- Spring Data JDBC
- Spring Boot Actuator
- Spring Boot Validation
- Tomcat Embed Jasper (for JSP support)
- H2 Database
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

- User authentication and session management
- Todo list CRUD operations
- Internationalization support (English, Chinese, Khmer, Dutch, Vietnamese)
- RESTful API endpoints
- API documentation via Swagger UI at http://localhost:8080/swagger-ui.html
- Application monitoring with Spring Boot Actuator
- Distributed tracing with Zipkin
- External currency service integration

## API Endpoints

- `GET /login` - Login page
- `POST /login` - Authenticate user
- `GET /logout` - Logout
- `GET /list-todos` - List user's todos
- `GET /add-todo` - Add new todo form
- `POST /add-todo` - Create new todo
- `GET /delete-todo` - Delete a todo
- `GET /update-todo` - Update todo form
- `POST /update-todo` - Update todo

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
spring.sql.init.mode=always
```

#### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The schema will be automatically created from `src/main/resources/schema.sql`.

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
