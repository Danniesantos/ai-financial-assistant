# AI Financial Assistant API

An AI-powered personal finance assistant built with Java, Spring Boot, and Spring AI.

The application allows users to manage financial transactions, analyze income and expenses, and interact with an AI assistant through natural language. By integrating OpenAI models with financial tools, the assistant can retrieve transaction data, generate financial summaries, and prepare transactions for user confirmation before saving them.

The project also supports audio transcription using OpenAI Whisper, allowing users to submit financial information through audio recordings.

Built with Clean Architecture principles, the application emphasizes separation of concerns, domain-driven business rules, maintainability, and automated testing.

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
- Springdoc OpenAPI / Swagger UI
- JaCoCo
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
- Text-based integration with Spring AI and OpenAI.
- Audio transcription integration using OpenAI Whisper.
- Initial AI system instructions for personal finance assistance.
- Financial tools for querying transactions, categories, retrieving financial summaries, and preparing transaction previews.
- Transaction preview workflow requiring explicit confirmation before persistence.

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

The application exposes REST endpoints for managing categories, financial transactions, and AI assistant interactions.

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

**Transaction Preview**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/transactions/preview` | Prepare a transaction preview |
| POST | `/api/transactions/confirm` | Confirm a transaction preview |

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
- API documentation with Springdoc OpenAPI and Swagger UI.

## API Documentation

Interactive API documentation is available through Swagger UI.

- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI specification:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Start the application before accessing these URLs.

## Usage Examples

The following examples demonstrate how to interact with the AI assistant through the REST API.

### 1. Chat with the AI Assistant

Send a natural language prompt to the AI assistant.

**Request**

`POST /api/assistant/chat`

```json
{
  "prompt": "How much did I spend this month?"
}
```

**Example Response**

```json
{
  "response": "Your total expenses for the selected period are..."
}
```

The assistant can use registered financial tools to retrieve transaction data, query categories, and generate financial summaries based on the information stored in the database.

### 2. Audio Transcription

Upload an audio file to convert spoken financial information into text.

**Request**

`POST /api/assistant/transcribe`

Content-Type: `multipart/form-data`

Form field: `file`

**Example using cURL**

```bash
curl -X POST http://localhost:8080/api/assistant/transcribe \
  -F "file=@recording.m4a"
```

**Example Response**

```json
{
  "transcription": "I spent 20 reais on coffee today."
}
```

Audio transcription is powered by OpenAI Whisper.

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

### Test Coverage

JaCoCo is used to generate code coverage reports.

- **Instruction coverage:** 98%
- **Branch coverage:** 75%

Generate the report with:

```bash
./gradlew clean test
```

The HTML report is available at:

`build/reports/jacoco/test/html/index.html`

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

`http://localhost:8080`

### 5. Check application health

The project uses Spring Boot Actuator to expose a health endpoint:

`http://localhost:8080/actuator/health`

### 6. Access API documentation

With the application running, open:

`http://localhost:8080/swagger-ui/index.html`

### 7. Run automated tests

```powershell
.\gradlew.bat clean test
```

Integration tests use Testcontainers and require Docker to be available.

### 8. Build the application

```powershell
.\gradlew.bat build
```

## Project Status

<ins>Backend MVP: Completed.</ins>

The backend implementation is complete for the current project scope and DIO challenge requirements.

### Completed

- Domain and application layers.
- Financial transaction and category management.
- Financial summaries and REST endpoints.
- PostgreSQL persistence and Flyway migrations.
- Clean Architecture implementation.
- Spring AI integration with OpenAI.
- AI assistant with financial tools.
- Transaction preview and explicit confirmation workflow.
- Audio transcription using OpenAI Whisper.
- Automated tests across application layers.
- JaCoCo test coverage reporting.
- Swagger / OpenAPI documentation.
- GitHub Actions CI pipeline.
- Manual validation of core API operations, AI assistant interactions, and audio transcription.

### Potential Future Improvements

The current backend is complete and functional within the defined MVP scope. Future iterations may expand the project with additional capabilities, such as:

- **Frontend application:** Develop a web interface to interact with the API, manage financial transactions, visualize financial summaries, and communicate with the AI assistant.
- **Data visualization:** Add dashboards and charts for income, expenses, and financial trends.
- **User authentication and authorization:** Introduce user accounts and secure access to personal financial data.
- **Multi-user support:** Extend the application to manage independent financial data for multiple users.

These improvements are planned as possible future enhancements and are not required for the current backend MVP or DIO challenge delivery.

## Author

Daniela Santos

[GitHub](https://github.com/Danniesantos)