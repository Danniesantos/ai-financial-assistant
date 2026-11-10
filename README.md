# AI Financial Assistant API

An AI-powered financial assistant built with Java and Spring Boot. The project aims to help users manage financial transactions, understand their spending habits, and receive personalized financial insights through text and audio interactions.

The application is being developed with a focus on clean code, maintainability, automated testing, and separation of concerns.

## Technologies

- Java 21
- Spring Boot 4.1.1
- Gradle
- PostgreSQL 17
- Docker Compose
- Spring Data JPA
- Hibernate
- Flyway
- Spring Boot Actuator
- JUnit 5
- Mockito
- AssertJ
- Testcontainers
- GitHub Actions

### Planned Technologies

- Spring AI
- OpenAI API

## Architecture

The project follows Clean Architecture, separating business rules from frameworks, databases, and external services.

The application is organized into four main layers:

- **Domain:** Contains business entities, value objects, enums, domain exceptions, and repository contracts.
- **Application:** Contains use cases responsible for orchestrating business operations.
- **Infrastructure:** Contains persistence entities, mappers, Spring Data repositories, and repository implementations.
- **Presentation:** Contains REST controllers, HTTP request and response DTOs, validation, and centralized exception handling.

This structure helps keep business rules independent of external technologies, making the application easier to maintain, test, and extend.

## Implemented Features

### Domain Layer

The initial domain model includes:

- **Money:** Value Object for representing and validating monetary values.
- **TransactionType:** Defines income and expense transaction types.
- **Category:** Represents financial transaction categories, including activation and deactivation.
- **FinancialTransaction:** Represents a financial transaction and its business rules.

### Application Layer

Use cases currently implemented:

**Financial Transactions**
- Create financial transactions.
- Retrieve all financial transactions.
- Retrieve a financial transaction by ID.
- Retrieve active financial transactions.
- Activate financial transactions.
- Deactivate financial transactions.

**Categories**
- Create categories.
- Retrieve all categories.
- Retrieve active categories.

### Infrastructure Layer

- JPA entities for categories and financial transactions.
- Money persistence through an embeddable value.
- Mappers between domain models and persistence entities.
- Repository implementations using Spring Data JPA.
- PostgreSQL persistence.
- Database schema versioning with Flyway.

### REST API

The application exposes REST endpoints for managing categories and financial transactions.

**Categories**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/categories` | Create a category |
| GET | `/api/categories` | Retrieve all categories |
| GET | `/api/categories/active` | Retrieve active categories |

**Financial Transactions**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/transactions` | Create a financial transaction |
| GET | `/api/transactions` | Retrieve all transactions |
| GET | `/api/transactions/active` | Retrieve active transactions |
| GET | `/api/transactions/{transactionId}` | Retrieve a transaction by ID |
| PATCH | `/api/transactions/{transactionId}/activate` | Activate a transaction |
| PATCH | `/api/transactions/{transactionId}/deactivate` | Deactivate a transaction |

The REST layer includes:

- Request validation using Jakarta Bean Validation.
- Separate request and response DTOs.
- Centralized exception handling.
- Consistent HTTP error responses using Spring ProblemDetail.

## Testing

Automated tests cover the domain, application, infrastructure, and presentation layers.

- **Unit tests:** JUnit 5, Mockito, and AssertJ.
- **Integration tests:** Testcontainers with PostgreSQL 17.
- **Repository tests:** Validate persistence operations, entity mapping, and database interactions.
- **Controller tests:** Validate HTTP endpoints, request handling, input validation, and error responses.

## Database

The application uses PostgreSQL 17, running in Docker.

Database schema creation and versioning are managed by Flyway.

Hibernate is configured with:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
```

This configuration keeps schema management under Flyway and prevents Hibernate from automatically modifying the database structure.

## Continuous Integration

The project uses GitHub Actions to automate testing and build validation.

The workflow is configured to run on pushes and pull requests targeting the `main` and `develop` branches.

Pipeline steps:

1. Checkout repository.
2. Set up Java 21 (Temurin).
3. Configure Gradle.
4. Run automated tests.
5. Build the application.

Workflow file: `.github/workflows/ci.yml`.

## Getting Started

### Prerequisites

- Java 21
- Docker Desktop
- Git

### 1. Clone the repository

```bash
git clone https://github.com/Danniesantos/ai-financial-assistant.git
cd ai-financial-assistant
```

### 2. Configure environment variables

Create a local `.env` file using `.env.example` as a reference.

Configure the database credentials and connection settings.

Example:

```properties
POSTGRES_DB=financial_assistant
POSTGRES_USER=financial_user
POSTGRES_PASSWORD=your_password
POSTGRES_PORT=5433
```

**Important:** The `.env` file contains local configuration and must not be committed to Git.

Spring Boot does not automatically load `.env` files. On Windows PowerShell, load the variables into the current terminal session:

```powershell
Get-Content .env | ForEach-Object {
    if ($_ -match '^([^#][^=]*)=(.*)$') {
        [Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process')
    }
}
```

### 3. Start PostgreSQL

From the project root, run:

```bash
docker compose up -d
```

### 4. Run the application

On Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

The application will be available at:

http://localhost:8080

### 5. Check application health

The project uses Spring Boot Actuator to expose a health endpoint:

http://localhost:8080/actuator/health

### 6. Run automated tests

```powershell
.\gradlew.bat test
```

Integration tests use Testcontainers and require Docker to be available.

### 7. Build the application

```powershell
.\gradlew.bat build
```

## Project Status

The project is currently under development.

### Completed

- [x] Initial project configuration.
- [x] PostgreSQL configuration with Docker Compose.
- [x] Flyway configuration and initial database migration.
- [x] Domain layer implementation.
- [x] Domain unit tests.
- [x] Application use cases for financial transactions.
- [x] Application use cases for category management.
- [x] Application unit tests.
- [x] Infrastructure persistence entities and mappers.
- [x] Repository adapters using Spring Data JPA.
- [x] Persistence integration tests with Testcontainers.
- [x] GitHub Actions CI workflow configuration.
- [x] REST API endpoints for categories and financial transactions.
- [x] HTTP request and response DTOs.
- [x] Request validation and centralized exception handling.
- [x] REST controller tests.

### In Progress / Planned

- [ ] Financial summaries and aggregation.
- [ ] Spring AI and OpenAI integration.
- [ ] AI-powered financial analysis and summaries.
- [ ] Spring AI Tool Calling.
- [ ] Audio input and interaction.
- [ ] End-to-end tests and additional API tests.
- [ ] Final documentation and improvements.

## Author

Daniela Santos

[GitHub](https://github.com/Danniesantos)
