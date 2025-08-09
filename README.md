# 🚀 Spring Microservices Starter

A production-ready starter template for building scalable Spring Boot applications using a multi-module Maven architecture.

## 📋 What's Included

### 🏗️ Architecture
- **Multi-module Maven project** with clean separation of concerns
- **Core modules** providing shared infrastructure
- **A service** (`user-service`) demonstrating best practices
- **Complete integration** between all components

### 🛠️ Technology Stack
- **Spring Boot 3.2.0** - Application framework
- **Maven Multi-Module** - Build and dependency management
- **PostgreSQL** - Primary database
- **Flyway** - Database migrations
- **Apache Kafka** - Event streaming
- **HashiCorp Vault** - Secrets management
- **Testcontainers** - Integration testing
- **Spring JDBC** - Direct database access
- **Docker** - Containerization ready

## 📂 Project Structure

```
spring-microservices-starter/
├── pom.xml                    # Parent POM with dependency management
│
├── core/                      # Shared infrastructure modules
│   ├── core-database/         # PostgreSQL + Flyway + JDBC utilities
│   ├── core-messaging/        # Kafka producers/consumers
│   ├── core-vault/            # Vault secret management
│   └── core-testing/          # Testcontainers setup
│
├── services/                  # Deployable applications
│   └── user-service/          # Sample microservice
│
└── docker-compose.yml         # Local development environment
```

## 🚀 Quick Start

### 1️⃣ Prerequisites
- Java 17+
- Docker & Docker Compose
- Maven 3.6+

### 2️⃣ Start Infrastructure
```bash
docker compose up -d
```
This starts PostgreSQL, Kafka, Zookeeper, and Vault.

### 3️⃣ Initialize Vault
```bash
# Unseal Vault and create secrets
curl -X POST http://localhost:8200/v1/sys/init \
  -d '{"secret_shares": 1, "secret_threshold": 1}'

# Use the root token from response to authenticate
export VAULT_TOKEN="your-root-token"
```

### 4️⃣ Build & Run
```bash
# Build all modules
./mvnw clean install

# Run the user service
./mvnw spring-boot:run -pl user-service
```

### 5️⃣ Test the API
```bash
# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com"}'

# Get all users
curl http://localhost:8080/api/users
```

## 🏛️ Core Modules Deep Dive

### 🗄️ core-database
- **PostgreSQL connection management** with HikariCP
- **Flyway migrations** auto-applied on startup
- **JDBC utilities** for direct database access
- **Health checks** and monitoring

**Key Features:**
- Connection pooling and optimization
- Database migration versioning
- Transaction management
- Query utilities and row mappers

### 📨 core-messaging
- **Kafka integration** with Spring Kafka
- **Producer/Consumer abstractions** for easy usage
- **Serialization handling** (JSON by default)
- **Error handling and retry logic**

**Key Features:**
- Topic auto-creation for development
- Dead letter queue patterns
- Message tracing and monitoring
- Async processing support

### 🔐 core-vault
- **HashiCorp Vault integration** for secrets
- **Dynamic secret management** with auto-refresh
- **Database credential rotation** support
- **Configuration property injection**

**Key Features:**
- Automatic token renewal
- KV secrets engine support
- Database secrets engine integration
- Environment-based configuration

### 🧪 core-testing
- **Testcontainers setup** for integration tests
- **Test data management** utilities
- **Mock configurations** for unit tests
- **Test profiles** for different scenarios

**Key Features:**
- PostgreSQL test containers
- Kafka test containers
- Vault test setup
- Data cleanup utilities

## 📋 Service Implementation

### 👤 user-service
A complete REST API demonstrating:
- **CRUD operations** with proper HTTP methods
- **Database integration** using core-database
- **Event publishing** using core-messaging
- **Secret management** using core-vault
- **Comprehensive testing** using core-testing

**API Endpoints:**
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

## 🔧 Configuration

### Environment Variables
```bash
# Database
POSTGRES_URL=jdbc:postgresql://localhost:5432/userdb
POSTGRES_USERNAME=postgres
POSTGRES_PASSWORD=postgres

# Kafka
KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# Vault
VAULT_URI=http://localhost:8200
VAULT_TOKEN=your-vault-token
```

### Application Properties
Each service has its own `application.yml` with:
- Database configuration
- Kafka settings
- Vault integration
- Actuator endpoints
- Logging configuration

## 🧪 Testing Strategy

### Unit Tests
- **Service layer testing** with mocks
- **Repository testing** with H2
- **Controller testing** with WebMvcTest

### Integration Tests
- **Full application context** with Testcontainers
- **Database migrations** applied automatically
- **Kafka message testing** with embedded broker
- **End-to-end API testing**

**Run Tests:**
```bash
# All tests
./mvnw test

# Integration tests only
./mvnw test -Dtest="*IT"

# Specific service tests
./mvnw test -pl user-service
```

## 📦 Deployment

### Docker
Each service includes a `Dockerfile`:
```bash
cd services/user-service
docker build -t user-service:latest .
docker run -p 8080:8080 user-service:latest
```

### Kubernetes
Ready for Kubernetes deployment with:
- ConfigMaps for configuration
- Secrets for sensitive data
- Services for internal communication
- Ingress for external access

## 🔄 Adding New Services

### 1️⃣ Create Service Module
```bash
mkdir -p services/order-service/src/main/java
cd services/order-service
# Copy and modify user-service structure
```

### 2️⃣ Update Parent POM
```xml
<modules>
    <module>services/user-service</module>
    <module>services/order-service</module>
</modules>
```

### 3️⃣ Add Dependencies
Reference only the core modules you need:
```xml
<dependencies>
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>core-database</artifactId>
    </dependency>
    <!-- Add other core modules as needed -->
</dependencies>
```

## 🎯 Best Practices Implemented

### 🏗️ Architecture
- **Clean separation** between core and services
- **Provided scope** for core dependencies
- **Interface-based design** for flexibility
- **Event-driven communication** between services

### 🔒 Security
- **Vault integration** for secret management
- **Connection pooling** with security best practices
- **Input validation** and sanitization
- **Proper error handling** without information leakage

### 📊 Observability
- **Health checks** for all components
- **Metrics** via Spring Actuator
- **Structured logging** with correlation IDs
- **Distributed tracing** ready

### 🧪 Testing
- **Test pyramid** with unit, integration, and E2E tests
- **Testcontainers** for realistic testing
- **Test data builders** for maintainable tests
- **Separate test configurations**

## 📈 Scaling Guidelines

### Horizontal Scaling
- Each service is **stateless** and can be scaled independently
- **Database connection pooling** handles multiple instances
- **Kafka consumer groups** distribute message processing

### Vertical Scaling
- **JVM tuning** parameters in Dockerfile
- **Connection pool sizing** based on load
- **Cache configuration** for performance

## 🔍 Monitoring & Operations

### Health Checks
- `/actuator/health` - Overall application health
- `/actuator/health/db` - Database connectivity
- `/actuator/health/kafka` - Kafka connectivity
- `/actuator/health/vault` - Vault connectivity

### Metrics
- JVM metrics, database pool metrics
- Kafka consumer/producer metrics
- Custom business metrics
- Request/response metrics

## 🆘 Troubleshooting

### Common Issues
1. **Vault unsealed**: Ensure Vault is initialized and unsealed
2. **Database migrations**: Check Flyway migration status
3. **Kafka connectivity**: Verify broker accessibility
4. **Port conflicts**: Ensure ports 8080, 5432, 9092, 8200 are available

### Debugging
```bash
# Enable debug logging
export LOGGING_LEVEL_COM_EXAMPLE=DEBUG

# View container logs
docker compose logs -f postgres
docker compose logs -f kafka
```

## 📚 Next Steps

1. **Add more services** following the established patterns
2. **Implement API Gateway** for centralized routing
3. **Add service discovery** (Consul/Eureka)
4. **Implement circuit breakers** (Resilience4j)
5. **Add distributed tracing** (Zipkin/Jaeger)
6. **Set up CI/CD pipeline** with automated testing

## 🤝 Contributing

This starter template provides a solid foundation for enterprise Spring Boot applications. Feel free to extend it based on your specific requirements!

---

**Happy coding!** 🎉