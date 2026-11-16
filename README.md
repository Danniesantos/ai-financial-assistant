# AI Financial Assistant API

An AI-powered financial assistant built with Java and Spring Boot. The project aims to help users manage financial transactions, understand their spending habits, and access financial assistance through text-based interactions and audio transcription.

The application is designed with a focus on clean code, maintainability, automated testing, and separation of concerns.

## Technologies

- Java 21
- Spring Boot 4.1.1
- Spring AI 2.0.2
- OpenAI API
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

## Architecture

The project follows Clean Architecture, separating business rules from frameworks, databases, and external services.

The application is organized into four main layers:

- **Domain:** Contains business entities, value objects, enums, domain exceptions, and repository contracts.
- **Application:** Contains use cases responsible for orchestrating business operations.
- **Infrastructure:** Contains persistence entities, mappers, Spring Data repositories, repository implementations, and external AI integrations.
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
- Retrieve active transactions within a date range.

**Categories**

- Create categories.
- Retrieve all categories.
- Retrieve active categories.

**Financial Summaries**

- Calculate total income.
- Calculate total expenses.
- Calculate the financial balance.
- Count transactions within a specified date range.
- Exclude inactive transactions from financial summaries.

**AI Integration**

- Application port for AI assistant communication.
- Application port for audio transcription.
- Use case for generating AI responses.
- Use case for audio transcription.
- Basic text-based integration with Spring AI and OpenAI.
- Audio transcription integration using OpenAI Whisper.
- Initial AI system instructions for personal finance assistance.
- Financial tools for querying transactions, categories,
  retrieving financial summaries, and preparing transaction previews.
- Transaction preview workflow requiring explicit confirmation
  before persistence.

### Infrastructure Layer

- JPA entities for categories and financial transactions.
- Money persistence through an embeddable value.
- Mappers between domain models and persistence entities.
- Repository implementations using Spring Data JPA.
- PostgreSQL persistence.
- Database schema versioning with Flyway.
- OpenAI assistant adapter using Spring AI.
- OpenAI audio transcription adapter using Spring AI.
- Financial summary tool.
- Transaction query tool.
- Category query tool.
- Transaction preview tool with explicit confirmation workflow.
- AI tools registered with the assistant's ChatClient.

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
| POST | `/api/transactions/confirm/{previewId}` | Confirm and persist a transaction preview |
| GET | `/api/transactions` | Retrieve all transactions |
| GET | `/api/transactions/active` | Retrieve active transactions |
| GET | `/api/transactions/summary?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` | Retrieve a financial summary for a date range |
| GET | `/api/transactions/{transactionId}` | Retrieve a transaction by ID |
| PATCH | `/api/transactions/{transactionId}/activate` | Activate a transaction |
| PATCH | `/api/transactions/{transactionId}/deactivate` | Deactivate a transaction |

**AI Assistant**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/assistant/chat` | Send a text prompt to the AI assistant |
| POST | `/api/assistant/transcribe` | Transcribe an uploaded audio file |

The REST layer includes:

- Request validation using Jakarta Bean Validation.
- Separate request and response DTOs.
- Centralized exception handling.
- Consistent HTTP error responses using Spring ProblemDetail.

## Testing

Automated tests cover the domain, application, infrastructure, and presentation layers.

- **Unit tests:** JUnit 5, Mockito, and AssertJ.
- **Domain tests:** Validate business rules and value objects.
- **Application tests:** Validate use cases and business workflows.
- **Integration tests:** Testcontainers with PostgreSQL 17.
- **Repository tests:** Validate persistence operations, entity mapping, and database interactions.
- **Controller tests:** Validate HTTP endpoints, request handling, input validation, and error responses.
- **AI adapter tests:** Validate text generation and audio transcription adapters, including error handling, without requiring real API calls.
- **AI tool tests:** Validate tool behavior, parameter conversion, and use case delegation.
- **AI tool integration tests:** Validate the financial summary tool execution path through the application, repository adapter, and PostgreSQL database.
- **Audio transcription tests:** Validate audio transcription configuration, adapter behavior, and HTTP request handling.

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

The workflow runs on:

- Pushes to `main`, `develop`, and `feature/**` branches.
- Pull requests targeting `main` and `develop`.
- Manual workflow dispatch.

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

Configure the database credentials and connection settings, as well as your OpenAI API key.

Example:

```properties
POSTGRES_DB=financial_assistant
POSTGRES_USER=financial_user
POSTGRES_PASSWORD=your_password
POSTGRES_PORT=5433
OPENAI_API_KEY=your-openai-api-key
```

Important: The `.env` file contains local configuration and must not be committed to Git.

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

`http://localhost:8080`

### 5. Check application health

The project uses Spring Boot Actuator to expose a health endpoint:

`http://localhost:8080/actuator/health`

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

The main application features have been implemented. The project is currently in the final review and documentation phase.

### Completed

- [x] Initial project configuration.
- [x] PostgreSQL configuration with Docker Compose.
- [x] Flyway configuration and database migrations.
- [x] Domain layer implementation and unit tests.
- [x] Application use cases and unit tests.
- [x] Category and financial transaction management.
- [x] Financial summary calculation and REST endpoint.
- [x] Infrastructure persistence entities, mappers, and repository adapters.
- [x] Persistence integration tests with Testcontainers.
- [x] GitHub Actions CI workflow.
- [x] REST API endpoints and centralized exception handling.
- [x] AI assistant text endpoint.
- [x] Spring AI and OpenAI adapter implementation.
- [x] Financial summary, transaction query, and category query tools.
- [x] Transaction preview tool with explicit confirmation workflow.
- [x] AI tools registered with ChatClient.
- [x] Audio transcription integration using OpenAI Whisper.
- [x] Audio transcription REST endpoint and tests.
- [x] Full automated test suite passing.
- [x] Manual validation of core REST API operations.

### Pending

- [ ] Validate live Spring AI Tool Calling execution with the OpenAI model.
- [ ] Perform final project review and documentation.
- [ ] Prepare the project for final delivery.

**Note:** AI tools are implemented, registered with ChatClient, and covered by automated tests. Live model-driven Tool Calling has not yet been validated because a funded OpenAI API key is required. Audio transcription is implemented and tested with mocked provider responses; live transcription has not yet been validated.

## Author

Daniela Santos

[GitHub](https://github.com/Danniesantos)