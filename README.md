# Real Estate Management System

A comprehensive Spring Boot backend for managing real estate properties, transactions, and user interactions.

## Features

- **Role-Based Access Control**: Admin, Agent, and Client roles with appropriate permissions
- **Property Management**: List, search, and manage properties with detailed information
- **Transaction Handling**: Track property sales and rentals
- **User Reviews**: Allow clients to rate and review properties
- **Image Management**: Upload and manage property images
- **Secure Authentication**: JWT-based authentication and authorization

## Technologies

- **Spring Boot 3.2**: For creating the RESTful API
- **Spring Security**: For authentication and authorization
- **Spring Data JPA**: For database operations
- **H2 Database**: For development and testing
- **JWT**: For secure API authentication
- **Validation**: For input validation
- **Exception Handling**: Global exception handling for graceful error responses

## Project Structure

The project follows a standard layered architecture:

- **Model**: Entity classes representing database tables
- **Repository**: Data access interfaces
- **Service**: Business logic implementation
- **Controller**: REST API endpoints
- **DTO**: Data Transfer Objects for API communication
- **Exception**: Custom exceptions and global exception handling
- **Security**: Authentication and authorization configuration

## Entity Relationships

- **User**: Central entity representing system users (Admin, Agent, Client)
- **Property**: Real estate listings with detailed information
- **Category**: Property types (Apartment, House, Office, etc.)
- **Transaction**: Sales and rental records
- **Amenity**: Property features (Pool, Gym, Parking, etc.)
- **Image**: Property images
- **Rating**: User reviews and ratings for properties

## Setup and Installation

1. Clone the repository
2. Ensure you have Java 17+ and Maven installed
3. Run `mvn clean install` to build the project
4. Run `mvn spring-boot:run` to start the application
5. The API will be available at `http://localhost:8080`

## API Documentation

Once the application is running, you can access the API documentation at:
- H2 Console: `http://localhost:8080/h2-console`

## Security

- JWT-based authentication
- Role-based access control
- Password encryption using BCrypt
- Secured endpoints based on user roles

## Frontend Integration

This backend is designed to be integrated with any frontend framework. It provides RESTful endpoints that return JSON responses suitable for consumption by modern frontend applications."# realEstateSpringboot" 
