# AI Financial Assistant API

An AI-powered financial assistant built with Java and Spring Boot, designed to manage financial transactions through natural language and voice interactions.

The project explores the integration of Large Language Models (LLMs) with backend applications, using Spring AI and Tool Calling to interpret user requests and execute financial operations.

## Project Overview

The AI Financial Assistant API aims to provide an intelligent way to interact with personal financial data.

Users will be able to send voice commands, which will be transcribed and interpreted by an AI model. Based on the user's intent, the assistant will execute operations such as creating and retrieving financial transactions.

The project will also explore AI-generated financial summaries and insights.

## Main Goals

- Process audio input and convert speech into text.
- Interpret natural language requests using an LLM.
- Execute financial operations through AI Tool Calling.
- Create and retrieve financial transactions.
- Persist financial data in PostgreSQL.
- Generate voice responses.
- Provide intelligent financial summaries and insights.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Main programming language |
| Spring Boot | Backend framework |
| Spring AI | AI integration |
| OpenAI | Language model and AI services |
| Spring Web | REST API |
| Spring Data JPA | Data persistence |
| PostgreSQL | Relational database |
| Flyway | Database migrations |
| Bean Validation | Input validation |
| JUnit 5 | Unit and integration testing |
| Mockito | Mocking and isolated tests |
| Springdoc OpenAPI | API documentation |
| Gradle | Build automation |
| Docker | Database containerization |

## Architecture

The application will follow Clean Architecture principles, separating business rules from infrastructure and external dependencies.

Planned package organization:

```text
com.financialassistant.ai_financial_assistant
├── domain
│   ├── model
│   ├── valueobject
│   └── repository
├── application
│   ├── usecase
│   └── port
└── infrastructure
    ├── web
    ├── persistence
    └── ai
```

## Getting Started

### Prerequisites

- Java 21
- Docker Desktop
- Git

### Database Setup

Clone the repository:

```bash
git clone https://github.com/Danniesantos/ai-financial-assistant.git
cd ai-financial-assistant
```

Create your local `.env` file based on `.env.example` and configure your database credentials.

Start PostgreSQL:

```bash
docker compose up -d
```

### Running the Application

Configure the required environment variables in your terminal:

```powershell
$env:POSTGRES_PORT="5433"
$env:POSTGRES_DB="financial_assistant"
$env:POSTGRES_USER="financial_user"
$env:POSTGRES_PASSWORD="your_local_password"
```

Run the application:

```powershell
.\gradlew.bat bootRun
```

The API will be available at:

```text
http://localhost:8080
```

### Health Check

To verify that the application is running:

```http
GET http://localhost:8080/actuator/health
```

## Development Roadmap

- [x] Initial Spring Boot project setup
- [x] PostgreSQL configuration with Docker
- [x] Database connectivity
- [ ] Initial database migrations
- [ ] Domain modeling and business rules
- [ ] Financial transaction management
- [ ] REST API endpoints
- [ ] Automated tests
- [ ] Spring AI integration
- [ ] AI Tool Calling
- [ ] Audio transcription
- [ ] Voice response generation
- [ ] AI-powered financial summaries and insights
- [ ] API documentation
- [ ] Final documentation and deployment preparation

## Project Status

<escape>🚧</escape> **In Development**

This project is being developed as part of a practical learning journey focused on Java, Spring Boot, AI integration, and backend software engineering.

## License

This project is currently under development. License information will be added later.
