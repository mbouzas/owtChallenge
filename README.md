# owtChallenge backend
# Boat API - Spring Boot JWT Example

A simple REST API for managing boats, secured with JWT authentication using Spring Boot.

## Features
- CRUD operations for boats
- JWT-based authentication
- Spring Security integration
- In-memory user for demo
- H2 in-memory database

## Requirements

- Java 21+
- Maven 3.8+

## Setup

1. **Clone the repository**
   ```sh
   git clone https://github.com/yourusername/boat-api.git
   cd boat-api
    ```

2. **Build the project**
   ```sh
   mvn clean install
   ```

3. **Run the application**
   ```sh
   mvn spring-boot:run
   ```
   The API will be available at `http://localhost:8080`.

4. **Database**
    -  By default, uses H2 in-memory DB.
    -  Data is loaded from `src/main/resources/data.sql`.

## Authentication

1. **Obtain a JWT token**

   Send a POST request to `/auth/login` with the default user credentials:

    - **Username:** `user`
    - **Password:** `password`

   Example using `curl`:
   ```sh
   curl -X POST "http://localhost:8080/auth/login" -d "username=user&password=password"
   ```

   Response:
   ```
   { "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6..." }
   ```

2. **Use the token**

   Add the token to the `Authorization` header for all API requests:
   ```
   Authorization: Bearer <token>
   ```

## API Endpoints

- `POST   /auth/login`         — Get JWT token
- `GET    /api/boats`          — List all boats
- `GET    /api/boats/{id}`     — Get boat by ID
- `POST   /api/boats`          — Create a new boat
- `PUT    /api/boats/{id}`     — Update a boat
- `DELETE /api/boats/{id}`     — Delete a boat

_All `/api/boats` endpoints require a valid JWT token._

## Example: List Boats

```sh
curl -H "Authorization: Bearer <token>" http://localhost:8080/api/boats
```

## Running Tests

```sh 
mvn test
```

## Notes

- Lombok is used for model boilerplate.
- JWT secret and user details are for demo only; update for production use.

