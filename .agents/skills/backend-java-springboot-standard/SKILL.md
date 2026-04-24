---
name: backend-java-springboot-standard
description: 'Adherence to specific organizational standards for Java 25 and Spring Boot 3.4 development.'
---

# Backend Development Standards (Java & Spring Boot)

Your goal is to ensure all backend development strictly follows the defined organizational standards for architecture, persistence, and deployment.

## Execution Environment

- **Language:** Use Java 25 (OpenJDK).
- **Framework:** Use Spring Boot 3.4.
- **Server:** Target Apache Tomcat 11 for deployment in WAR format.
- **Dependency Management:** Use Maven with a valid `pom.xml` file.

## Architecture & Logic

- **Controllers:** Utilize the `@Controller` annotation to provide hybrid support for both Thymeleaf views and RESTful responses.
- **Service Layer:** All business logic must reside exclusively within classes annotated with `@Service`.
- **Dependency Injection:** Use the `@Autowired` annotation for injecting dependencies.
- **Error Handling:** - Implement global exception management via `@ControllerAdvice`.
    - All JSON responses must be processed through the `ResponseHandler` utility class.
    - Avoid unnecessary try-catch blocks within the controller layer.

## Configuration
- Use  `application.properties` for configuration.

## Data Persistence (PostgreSQL / SQL Server)

- **Framework:** Use Spring Data JPA by extending `JpaRepository`.
- **Naming Conventions:** Use `snake_case` for all database table and column names.
- **Entity Mapping:**
    - Explicitly define table names using `@Table(name = "table_name")`.
    - Explicitly define column names using `@Column(name = "column_name")`.
    - Data Loading: Prefer `FetchType.LAZY` by default.
- **Schema Management:** Set the configuration `spring.jpa.hibernate.ddl-auto` to `none`.
- **RESTful APIs:** Design clear and consistent RESTful endpoints.
- **DTOs (Data Transfer Objects):** Use DTOs to expose and consume data in the API layer. Do not expose JPA entities directly to the client.
- **Validation:** Use Java Bean Validation (JSR 380) with annotations (`@Valid`, `@NotNull`, `@Size`) on DTOs to validate request payloads.
- **Error Handling:** Implement a global exception handler using `@ControllerAdvice` and `@ExceptionHandler` to provide consistent error responses.

## Security

- **Secrets Management:** Hardcoding credentials, tokens, or access keys in the source code or property files is strictly prohibited.