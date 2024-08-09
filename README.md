# Online Bookstore

## Table of Contents
- [About](#about)
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Built With](#built-with)
- [Authors](#authors)
- [Acknowledgements](#acknowledgements)

## About
The Online Bookstore project is a web application that allows users to browse, search, and purchase books. It includes features like user authentication, and an admin panel to comfortably manage inventory and orders.

## Getting Started
These instructions will help you set up the project on your local machine for development and testing purposes.

### Prerequisites
Ensure you have the following software installed:
- [Java JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/install.html)
- [MySQL](https://dev.mysql.com/downloads/installer/)
- [Docker](https://www.docker.com/get-started)

### Installation
Follow these steps to set up the project:

```bash
# Step 1: Clone the repository
git clone https://github.com/hlibmurphy/Online-Bookstore.git

# Step 2: Navigate to the project directory
cd Online-Bookstore

# Step 3: Install dependencies using Maven
mvn clean install

# Step 4: Set up environment variables
cp .env.template .env
# Edit .env file to match your configuration (e.g., database connection details)

# Step 5: Build JAR file
mvn clean package

# Step 6: Run the application
docker-compose up
```
### Usage
Once the application is running, you can access it at `http://localhost:8081`. 
For documentation go to `http://localhost:8081/api/swagger-ui/index.html`

You can login as admin using these credentials on `http://localhost:8081/api/auth/login`
```bash
{
    "email": "admin@email.com",
    "password": "12345678"
}
```

The application provides the following features:
- Browse books
- Search for books
- User authentication (registration, login)
- Add books to cart
- Place orders

## Running Tests
To run the automated tests, use the following command:

```
mvn test
```

## Built With
- Java 17
- Spring Boot 3.2.3 - Framework
- Hibernate 6.4.4 - ORM
- MySQL - Database
- Liquibase 4.26.0 - Database change management
- Spring Doc 2.4.0 (Swagger) - documentation
- Docker 4.33.1 - Containerization
- Mockito 5.11.0 - Testing
- TestContainers 1.19.7 - Testing

## Authors
- **Hlib Bykovskyi** - *Initial work* - [hlibmurphy](https://github.com/hlibmurphy)

## Acknowledgements
- Thanks to the Spring Boot and Hibernate communities for their excellent documentation and support.
- Special thanks to MateAcademy for advising on the development.
