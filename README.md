# Domain-Driven Design (DDD) Template

A comprehensive Java template implementing Domain-Driven Design principles with Spring Boot, following clean architecture and hexagonal architecture patterns.

## Overview

This template provides a production-ready starting point for building enterprise applications using Domain-Driven Design (DDD) tactical patterns. It includes:

- **Clean separation of concerns** across four layers
- **Rich domain model** with entities, value objects, and aggregates
- **Domain events** for cross-aggregate communication
- **Use case driven** application layer
- **Infrastructure independence** through ports and adapters
- **Modern Java stack** with Spring Boot 3.x and Java 25

## Architecture

### Layer Structure

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │  ← REST Controllers, Exception Handlers
├─────────────────────────────────────────┤
│         Application Layer               │  ← Use Cases, DTOs, Mappers
├─────────────────────────────────────────┤
│         Domain Layer                    │  ← Entities, Value Objects, Domain Services
├─────────────────────────────────────────┤
│         Infrastructure Layer            │  ← Repositories, Persistence, Messaging
└─────────────────────────────────────────┘
```

### Modules

#### 1. Domain Layer (`domain/`)
The heart of the application containing business logic and rules.

**Key Components:**
- `model/` - Aggregates, entities, and value objects
  - `Product` - Sample aggregate root with business logic
  - `ProductId` - Strongly-typed identifier value object
  - `Money` - Value object for monetary amounts
- `repository/` - Repository interfaces (ports)
- `service/` - Domain services for cross-aggregate operations
- `event/` - Domain events
- `exception/` - Domain-specific exceptions
- `shared/` - Base classes (AggregateRoot, Entity, ValueObject, etc.)

**Characteristics:**
- No external dependencies (except Lombok and validation)
- Pure business logic
- Framework-agnostic
- Contains domain invariants and business rules

#### 2. Application Layer (`application/`)
Orchestrates use cases and coordinates domain objects.

**Key Components:**
- `usecase/` - Use case implementations (application services)
  - `CreateProductUseCase` - Create new product
  - `GetProductUseCase` - Retrieve product
  - `UpdateProductUseCase` - Update product
  - `ListProductsUseCase` - List products
- `dto/` - Data transfer objects for input/output
- `mapper/` - Converts between domain and DTOs
- `port/` - Output ports (interfaces for infrastructure)

**Characteristics:**
- Depends only on domain layer
- Transaction boundaries
- Orchestration logic
- No business logic

#### 3. Infrastructure Layer (`infrastructure/`)
Implements technical capabilities and external integrations.

**Key Components:**
- `persistence/` - JPA entities and repository implementations
  - `ProductEntity` - JPA entity
  - `JpaProductRepository` - Spring Data repository
  - `ProductRepositoryImpl` - Adapter implementing domain repository
- `messaging/` - Event publishing and handling
  - `SpringEventPublisher` - Event publisher implementation
  - `ProductEventHandler` - Domain event handlers
- `config/` - Spring configuration classes
- `resources/db/changelog/` - Liquibase database migrations

**Characteristics:**
- Implements infrastructure concerns
- Database access (JPA, Liquibase)
- Event publishing (Spring Events)
- External service integration

#### 4. Presentation Layer (`presentation/`)
Exposes the application to external consumers.

**Key Components:**
- `controller/` - REST API endpoints
  - `ProductController` - Product REST API
- `exception/` - Global exception handling
  - `GlobalExceptionHandler` - Translates domain exceptions to HTTP responses
- `DddApplication` - Spring Boot main class

**Characteristics:**
- REST API with Spring MVC
- Request/response validation
- API documentation (SpringDoc/OpenAPI)
- Exception translation

## DDD Patterns Implemented

### Tactical Patterns

1. **Aggregates** - `Product` is an aggregate root that ensures consistency boundaries
2. **Entities** - Objects with identity (e.g., `Product`)
3. **Value Objects** - Immutable objects identified by attributes (e.g., `Money`, `ProductId`)
4. **Domain Events** - `ProductCreatedEvent`, `ProductPriceChangedEvent`
5. **Repositories** - Collection-like interface for aggregates
6. **Domain Services** - `ProductDomainService` for logic that doesn't fit in entities
7. **Factories** - Factory methods like `Product.create()`

### Architectural Patterns

1. **Hexagonal Architecture (Ports & Adapters)**
   - Ports: `ProductRepository`, `EventPublisher`
   - Adapters: `ProductRepositoryImpl`, `SpringEventPublisher`

2. **Use Case Pattern**
   - Each use case is a separate class
   - Clear input/output boundaries

3. **Event-Driven Architecture**
   - Domain events published on state changes
   - Event handlers for side effects

## Getting Started

### Prerequisites

- Java 25 or higher
- Maven 3.9+
- PostgreSQL 14+ (or modify for your database)
- Docker (optional, for running PostgreSQL)

### Database Setup

1. **Using Docker:**
```bash
docker run --name ddd-postgres \
  -e POSTGRES_DB=ddd_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:15
```

2. **Manual Setup:**
```sql
CREATE DATABASE ddd_db;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE ddd_db TO postgres;
```

### Build and Run

1. **Clone the repository:**
```bash
git clone <repository-url>
cd ddd-template
```

2. **Build the project:**
```bash
mvn clean install
```

3. **Run the application:**
```bash
mvn spring-boot:run -pl presentation
```

The application will start on `http://localhost:8080`

### API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

## Example Usage

### Create a Product

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "currency": "USD",
    "initialStock": 10
  }'
```

### Get a Product

```bash
curl http://localhost:8080/api/products/{productId}
```

### Update a Product

```bash
curl -X PUT http://localhost:8080/api/products/{productId} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Laptop",
    "description": "Updated description"
  }'
```

### List All Products

```bash
curl http://localhost:8080/api/products
```

## Project Structure

```
ddd-template/
├── domain/                          # Domain Layer
│   └── src/main/java/.../domain/
│       ├── model/                   # Aggregates, Entities, Value Objects
│       │   ├── Product.java
│       │   ├── ProductId.java
│       │   ├── Money.java
│       │   └── ProductStatus.java
│       ├── repository/              # Repository interfaces
│       │   └── ProductRepository.java
│       ├── service/                 # Domain services
│       │   └── ProductDomainService.java
│       ├── event/                   # Domain events
│       │   ├── ProductCreatedEvent.java
│       │   └── ProductPriceChangedEvent.java
│       ├── exception/               # Domain exceptions
│       └── shared/                  # Base classes
│
├── application/                     # Application Layer
│   └── src/main/java/.../application/
│       ├── usecase/                 # Use cases
│       │   ├── CreateProductUseCase.java
│       │   ├── GetProductUseCase.java
│       │   ├── UpdateProductUseCase.java
│       │   └── ListProductsUseCase.java
│       ├── dto/                     # DTOs
│       ├── mapper/                  # Mappers
│       └── port/                    # Output ports
│
├── infrastructure/                  # Infrastructure Layer
│   ├── src/main/java/.../infrastructure/
│   │   ├── persistence/             # JPA entities & repositories
│   │   ├── messaging/               # Event publishing
│   │   └── config/                  # Spring configuration
│   └── src/main/resources/
│       ├── application.yml
│       └── db/changelog/            # Liquibase migrations
│
└── presentation/                    # Presentation Layer
    └── src/main/java/.../presentation/
        ├── controller/              # REST controllers
        ├── exception/               # Exception handlers
        └── DddApplication.java      # Main class
```

## Key Technologies

- **Java 25** - Programming language
- **Spring Boot 3.5.6** - Application framework
- **Spring Data JPA** - Data access
- **Hibernate 7.2** - ORM
- **Liquibase 5.0** - Database migrations
- **PostgreSQL** - Database
- **Lombok** - Boilerplate reduction
- **JMolecules** - DDD annotations and support
- **SpringDoc OpenAPI 3** - API documentation
- **JUnit 5** - Testing
- **Mockito** - Mocking
- **ArchUnit** - Architecture testing
- **Testcontainers** - Integration testing

## Testing Strategy

### Unit Tests
- Domain logic testing without frameworks
- Use case testing with mocked dependencies
- Located in each module's `src/test/java`

### Integration Tests
- Repository tests with Testcontainers
- API tests with REST Assured
- End-to-end flow testing

### Architecture Tests
- Use ArchUnit to enforce layer boundaries
- Ensure dependencies flow in the correct direction
- Validate DDD patterns

## Best Practices

### Domain Layer
- Keep domain logic in the domain layer
- Use value objects for concepts with no identity
- Aggregate roots enforce consistency boundaries
- Emit domain events for state changes
- Validate invariants in constructors/methods

### Application Layer
- One use case per class
- Keep use cases thin - delegate to domain
- Handle transactions at this level
- Convert between domain and DTOs here

### Infrastructure Layer
- Implement ports defined by domain/application
- Keep framework code isolated here
- JPA entities separate from domain entities
- Handle technical concerns (caching, logging, etc.)

### Presentation Layer
- Validate input
- Translate exceptions to HTTP responses
- Keep controllers thin
- Use DTOs for API contracts

## Extending the Template

### Adding a New Aggregate

1. **Domain Layer:**
   - Create entity/aggregate root in `domain/model/`
   - Define value objects
   - Create repository interface in `domain/repository/`
   - Define domain events in `domain/event/`

2. **Application Layer:**
   - Create DTOs in `application/dto/`
   - Create use cases in `application/usecase/`
   - Create mapper in `application/mapper/`

3. **Infrastructure Layer:**
   - Create JPA entity in `infrastructure/persistence/`
   - Create Spring Data repository
   - Implement domain repository
   - Create Liquibase migration

4. **Presentation Layer:**
   - Create REST controller
   - Add exception handling if needed

## Configuration

### Application Properties

Edit `infrastructure/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ddd_db
    username: postgres
    password: postgres

  jpa:
    hibernate:
      ddl-auto: validate  # Use Liquibase for schema management
```

### Logging

Configure logging levels in `application.yml`:

```yaml
logging:
  level:
    com.example.ddd: DEBUG
    org.springframework: INFO
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Resources

### Domain-Driven Design
- [Domain-Driven Design by Eric Evans](https://www.domainlanguage.com/ddd/)
- [Implementing Domain-Driven Design by Vaughn Vernon](https://vaughnvernon.com/)
- [DDD Reference by Eric Evans](https://www.domainlanguage.com/ddd/reference/)

### Clean Architecture
- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)

### Spring Boot
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
