# Library API Midterm Project

## Project Description

This project is a Spring Boot REST API created for the midterm assignment.

The application is a small library system. It can manage authors and books.

The project follows a layered architecture.

Controller to Service to Repository.

Controllers handle HTTP requests and responses.

Services contain the main business logic.

Repositories work with the database.

Business logic is not placed in controllers.

## Technologies Used

Java 17

Spring Boot

Spring Security

Spring Web

Spring Data JPA

Hibernate

H2 Database

Bean Validation

Lombok

Swagger and OpenAPI

Maven

## Main Entities

The project has two main entities.

## Author

Author has these fields.

id

fullName

email

## Book

Book has these fields.

id

title

isbn

publishedDate

author

## Relationship

One author can have many books.

Many books belong to one author.

This relationship is implemented with JPA annotations.

@OneToMany

@ManyToOne

## DTO Usage

The project uses DTO classes.

Request DTOs are used for input data.

Response DTOs are used for output data.

The API does not return entity classes directly from controllers.

## API Endpoints

## Author Endpoints

POST /api/authors

Creates a new author.

GET /api/authors

Returns all authors.

GET /api/authors/{id}

Returns one author by id.

PUT /api/authors/{id}

Updates an author by id.

DELETE /api/authors/{id}

Deletes an author by id.

## Book Endpoints

POST /api/books

Creates a new book.

GET /api/books

Returns all books.

GET /api/books/{id}

Returns one book by id.

PUT /api/books/{id}

Updates a book by id.

DELETE /api/books/{id}

Deletes a book by id.

## Validation

The project uses validation annotations.

@NotNull

@NotBlank

@Size

@Email

Invalid request data returns 400 Bad Request.

Example invalid author request.

    {
      "fullName": "A",
      "email": "wrong-email"
    }

This request returns 400 Bad Request because the name is too short and the email format is not valid.

## Exception Handling

The project has global exception handling with @RestControllerAdvice.

Missing author or book returns 404 Not Found.

Invalid request data returns 400 Bad Request.

Duplicate author email returns 409 Conflict.

Unexpected errors return 500 Internal Server Error.

## Swagger UI

Swagger UI is added to the project.

After running the application, Swagger can be opened here.

    http://localhost:8080/swagger-ui/index.html

The root URL also redirects to Swagger UI.

    http://localhost:8080/

Another possible URL is this.

    http://localhost:8080/swagger-ui.html

Swagger shows all API endpoints and allows testing them from the browser.

## Security

Spring Security is added to protect the API.

## Login Credentials

There are two in-memory users for testing.

**User Role:**
- Username: `user`
- Password: `user123`

**Admin Role:**
- Username: `admin`
- Password: `admin123`

Passwords are hashed with BCrypt.

## Access Rules

**Public Endpoints:**
- `GET /` (Redirects to Swagger UI)
- `GET /api/metadata` (Application metadata and settings)
- `GET /swagger-ui/**`
- `GET /swagger-ui.html`
- `GET /v3/api-docs/**`
- `GET /h2-console/**`
- `GET /login`

**Authenticated Endpoints (USER and ADMIN):**
- `GET /api/authors/**`
- `GET /api/books/**`

**ADMIN-Only Endpoints:**
- `POST /api/authors`
- `PUT /api/authors/**`
- `DELETE /api/authors/**`
- `POST /api/books`
- `PUT /api/books/**`
- `DELETE /api/books/**`

**Security Behavior:**
- Unauthenticated requests to `/api/**` return `401 Unauthorized` instead of redirecting to login.
- Forbidden requests (e.g., USER trying to POST) return `403 Forbidden`.
- Other protected paths redirect to `/login`.

## Method Security

Method-level security is enabled with `@EnableMethodSecurity`.
Critical service methods in `AuthorService` and `BookService` are protected with `@PreAuthorize("hasRole('ADMIN')")`.

## CSRF Explanation

CSRF is disabled in this project because it is a REST API tested with Swagger and it does not use server-side HTML forms.

## H2 Database Console

The project uses H2 database.

The H2 console can be opened here.

    http://localhost:8080/h2-console

Use these values.

    JDBC URL: jdbc:h2:file:./data/library-db

    User Name: sa

    Password:

The password field is empty.

## Profiles and Configuration

The project uses Spring profiles for different environments.

### Available Profiles
- **dev**: Uses H2 in-memory database, enables H2 console, and sets logging to DEBUG.
- **prod**: Expects real database settings through environment variables (PostgreSQL style).

### Custom App Settings
Custom settings are defined under `app.settings` prefix:
- `applicationTitle`: Title of the application.
- `defaultPageSize`: Default size for pagination.
- `supportEmail`: Support contact email.
- `externalServiceUrl`: URL for an external service.
- `featureEnabled`: Boolean flag for features.

## Internationalization (i18n)

The API supports multiple languages for responses and validation errors using the `Accept-Language` header.
- **English (en)**: Default language.
- **Georgian (ka)**: Georgian translations.

### How to Test i18n
Add the `Accept-Language` header to your request:
- `Accept-Language: en` returns English messages.
- `Accept-Language: ka` returns Georgian messages.

Supported i18n features:
- Resource not found messages.
- Duplicate email conflict messages.
- Validation error messages (e.g., name required, invalid email).
- Metadata retrieval success message.

## Application Monitoring

Spring Boot Actuator is used for monitoring.
- **Health**: `GET /actuator/health` (Public) - Reports application and library status.
- **Info**: `GET /actuator/info` (Public) - Displays project metadata.
- **Metrics**: `GET /actuator/metrics` (Requires ADMIN) - Shows application metrics.

Custom metrics include:
- `library.authors.created`: Counter for total authors created.
- `library.books.created`: Counter for total books created.

## Structured Logging

Logging is implemented using SLF4J with Lombok `@Slf4j`.
- **File Logging**: Logs are saved to `logs/app.log`.
- **Rolling Policy**: Daily rotation, max 10MB per file, 7 days history.
- **Log Levels**: 
  - **Dev**: DEBUG level for the project package.
  - **Prod**: WARN level for the project package.

## How to Run the Project

### From IntelliJ
- Run the `LibraryApiApplication` main class.
- To use a specific profile, add `-Dspring.profiles.active=dev` or `-Dspring.profiles.active=prod` to VM options.

### From Command Line
- Run with **dev** profile:
  ```
  .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
  ```
- Run with **prod** profile (ensure environment variables like DATABASE_URL are set):
  ```
  .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
  ```

## User Credentials and Roles

| Username | Password | Roles |
| :--- | :--- | :--- |
| user | user123 | USER |
| admin | admin123 | ADMIN |

## Security Rules

- `GET /api/**`: Requires USER or ADMIN.
- `POST/PUT/DELETE /api/**`: Requires ADMIN.
- `/actuator/health`, `/actuator/info`: Public.
- `/actuator/metrics`: Requires ADMIN.
- `/swagger-ui/**`, `/h2-console/**`: Public.

## Testing Instructions

Run the following command in the project folder to execute all tests:

    .\mvnw.cmd clean test

To check code coverage with JaCoCo:
1. Run tests.
2. Open `target/site/jacoco/index.html` in a browser.

## Custom App Settings

Configuration is managed via `AppSettingsProperties`.
- `app.settings.application-title`: Title of the API.
- `app.settings.support-email`: Support contact.

## Internationalization (i18n)

Error and validation messages are localized.
- Use `Accept-Language: en` for English.
- Use `Accept-Language: ka` for Georgian.

## Documentation

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **H2 Console**: `http://localhost:8080/h2-console` (only in dev profile)