This is a demo project for a tourism management system. It is a microservice built using Spring Boot and follows clean architecture principles with clear separation of concerns.

## Technology Stack
- Spring Boot 3.5.7
- MySQL
- Maven
- Java 21

## Main Features

- Exception Handling:

    - Custom Exception Handling
    - Global Exception Handling
    - Custom Error Response

- JWT Authentication:
    - JWT Token Generation
    - JWT Token Validation
    
    JWT is implemented only on select routes, ensuring that user can still be added without authentication.

- Data Pull from External API:
    - Feign Client for external API calls
    - Data validation and processing
        - Ensures that all existing validation apply to the external API Also.
        - Proper error messages are sent back to the client.
    - Adding the data to database from external API

- Validations:

    - Duplicate Booking Validation
    - Invalid Booking Date Validation
    - Customer exists in the tour package validation

Setup Instructions:

1. Create a new database in MySQL named tourism
2. Clone the repository
3. Open the project in your preferred IDE
4. Run the application
5. Test the endpoints

rajkarthi
