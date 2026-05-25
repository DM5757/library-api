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

## How to Run the Project

The project can be run from IntelliJ by starting the main class.

    LibraryApiApplication

It can also be run from terminal.

    .\mvnw.cmd spring-boot:run

After the app starts, open Swagger and test the endpoints.

## How to Run Tests

Run this command in the project folder.

    .\mvnw.cmd clean test

Current test result.

    Tests run: 6, Failures: 0, Errors: 0, Skipped: 0

    BUILD SUCCESS

## Testing Notes

The main application uses a file based H2 database.

Tests use an in memory H2 database.

This avoids database locking problems during tests.

## Project Status

The project runs successfully.

Swagger works correctly.

Author CRUD works correctly.

Book CRUD works correctly.

Validation works correctly.

Not found errors return 404.

Duplicate email returns 409.

Security works correctly.

Admin functionality is protected.

Maven tests pass successfully.