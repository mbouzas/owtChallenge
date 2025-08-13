# owtChallenge backend
# Boat API - Spring Boot JWT Example

A simple REST API for managing boats, secured with JWT authentication using Spring Boot.

## Features
- CRUD operations for boats
- JWT-based authentication
- Spring Security integration
- In-memory user for demo (username: `user`, password: `pass`)
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
    -  The schema.sql file defines the structure of the DB

## Authentication

1. **Obtain a JWT token**

   Send a POST request to `/api/auth` with the default user credentials:

   Authentication:
   Basic Authentication

   Example using `curl`:
   ```sh
   curl -X POST "http://localhost:8080/api/auth" \
     -H "Authorization: Basic $(echo -n "user:pass" | base64)"
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

curl -H "Authorization: Bearer eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJvd3RDaGFsbGVuZ2UiLCJzdWIiOiJ1c2VyIiwiZXhwIjoxNzU1MTIwMjEwLCJpYXQiOjE3NTUwODQyMTAsInNjb3BlIjoiUk9MRV9VU0VSIn0.mSKsqkhc3PSOxajUo3ZIAM9PqyAphwgi9P-v0wN7Uo-xtp1lCpoyjHuEL935gTam_zIoqpCK_WzXEE02mB1PvsqPsLsa0Mfz6ppctKkR8DpqUCl91tu8LFq-pwzGg-aX_jm9fvNi4YkeF2oidUAqpSyIpZi4_s1mEyR5TmckMeIBCVzILlR7H8hWJ8xmEg_RCqmjClsYMOoIr4R8GzrG2T0BhPS7sgJXbhHsTkXOl0w5fJa1QVdEy7ehUnPxz6rIxQ_39LBGoRL9vEGzWY8UJ4SypPmg2IvFPy6b4a1DhWLk0BWaEM4vyydyRge6wJ4YIQ8ofU3TCvLMHAXnDuMDUw" http://localhost:8080/api/boats
```

[
{
"id": 1,
"name": "Sea Explorer",
"description": "A sturdy sailboat for coastal trips",
"capacity": 8,
"size": 30,
"type": "SAILBOAT",
"createdAt": "13/08/2025",
"updatedAt": "13/08/2025"
},
{
"id": 2,
"name": "Wave Rider",
"description": "Fast motorboat with great acceleration",
"capacity": 4,
"size": 20,
"type": "MOTORBOAT",
"createdAt": "13/08/2025",
"updatedAt": "13/08/2025"
}
]

## Postman Collection

You can find a Postman collection for testing the API at:
src/main/resources/Boat API.postman_collection.json

## Running Tests

```sh 
mvn test
```

## Running the Project with Docker
1. Build the Docker Image
   From the root of the project (where the Dockerfile is located), run:
```sh
docker build -t otp-challenge .
```
2. Run the Docker Container
   Run the container and map port 8080 from the container to your host:
```sh
docker run -p 8080:8080 otp-challenge
```
The backend will be available at `http://localhost:8080`.

## Notes

- Lombok is used for model boilerplate.
- JWT secret and user details are for demo only.

