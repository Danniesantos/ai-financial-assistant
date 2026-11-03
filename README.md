# AI Financial Assistant API

An AI-powered financial assistant built with Java and Spring Boot. The project aims to help users manage financial transactions, understand their spending habits, and receive financial insights through text and audio interactions.

## Technologies

- Java 21
- Spring Boot 4.1.1
- Gradle
- PostgreSQL 17
- Docker
- Spring Data JPA
- Flyway
- JUnit 5
- GitHub Actions

## Architecture

The project follows **Clean Architecture**, separating business rules from external frameworks and infrastructure concerns.

The goal is to keep the domain independent, organized, and easy to maintain and test.

## Current Features

### Domain Layer

The initial domain implementation includes:

- **Money:** Value Object for representing monetary values.
- **TransactionType:** Defines income and expense transaction types.
- **Category:** Represents financial transaction categories.
- **FinancialTransaction:** Represents a financial transaction.

### Testing

Unit tests have been implemented for the domain components, including:

- Money
- Category
- FinancialTransaction

## Planned Features

- Financial transaction management.
- Income and expense summaries.
- Financial analysis using AI.
- Text-based interaction with the assistant.
- Audio input and interaction.
- Integration with OpenAI through Spring AI.
- REST API endpoints.
- Data persistence with PostgreSQL.

## Database

The project uses PostgreSQL 17 running in Docker.

Database versioning and migration management are handled by Flyway.

The application uses Hibernate with `ddl-auto: validate`, keeping database schema management under Flyway.

## Continuous Integration

The project uses **GitHub Actions** to automate testing and build validation.

The workflow runs on pushes and pull requests to the `main` and `develop` branches.

Pipeline steps:

- Checkout repository.
- Set up Java 21 (Temurin).
- Configure Gradle.
- Run automated tests.
- Build the application.

Workflow file: `.github/workflows/ci.yml`.

## Getting Started

### Prerequisites

- Java 21
- Docker Desktop
- Git

### Environment Configuration

Clone the repository:

```bash
git clone https://github.com/Danniesantos/ai-financial-assistant.git
cd ai-financial-assistant
```

Create your local `.env` file based on `.env.example` and configure your database credentials.

**Important:** The `.env` file contains local configuration and should not be committed to Git.

### Start PostgreSQL

```bash
docker compose up -d
```

### Running the Application

In PowerShell, load the environment variables from `.env`:

```powershell
Get-Content .env | ForEach-Object {
    if ($_ -match '^([^#][^=]*)=(.*)$') {
        [Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process')
    }
}
```

Run the application:

```powershell
.\gradlew.bat bootRun
```

The application will be available at:

```text
http://localhost:8080
```

### Health Check

The application provides a health endpoint through Spring Boot Actuator:

```text
http://localhost:8080/actuator/health
```

## Project Status

The project is under development.

- [x] Initial project configuration.
- [x] PostgreSQL configuration with Docker.
- [x] Flyway configuration.
- [x] Domain layer implementation.
- [x] Domain unit tests.
- [x] GitHub Actions CI workflow.
- [x] Automated test and build execution in CI.
- [ ] Application layer.
- [ ] Infrastructure layer.
- [ ] REST API endpoints.
- [ ] Financial transaction persistence.
- [ ] Spring AI and OpenAI integration.
- [ ] AI-powered financial analysis and summaries.
- [ ] Audio interaction.
- [ ] Documentation and final improvements.

## Author

**Daniela Santos**

[GitHub](https://github.com/Danniesantos)
