# Online Bookstore

## Table of Contents
- [About](#about)
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Deployment](#deployment)
- [Built With](#built-with)
- [Authors](#authors)
- [Acknowledgements](#acknowledgements)

## About
The Online Bookstore project is a web application that allows users to browse, search, and purchase books. It includes features like user authentication, book reviews, and an admin panel to manage inventory and orders.

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

# Step 5: Set up environment variables
cp .env.example .env
# Edit .env file to match your configuration (e.g., database connection details)

# Step 6: Build JAR file
mvn clean package

# Step 6: Build Docker Image
docker build -t online-bookstore .

# Step 7: Run the application
docker-compose up

# Step 8: Set up the database
# Create a database named `online_bookstore` in MySQL
# Import the SQL script to create tables and insert initial data
mysql -u your-username -p online_bookstore < src/main/resources/db/schema.sql
```
### Usage
Once the application is running, you can access it at `http://localhost:8080`. The application provides the following features:
- Browse books
- Search for books
- User authentication (registration, login, logout)
- Add books to cart
- Checkout and place orders
- Leave reviews for books

## Running Tests
To run the automated tests, use the following command:

# Running unit tests
```
mvn test
```

## Deployment
For deployment to a live system, ensure you have a server with Java and MySQL installed. Follow these steps:

1. Build the project:
   ```
    mvn clean package
   ```
   
3. Copy the `target/online-bookstore.jar` file to your server.

4. Run the application on the server:
   ```
    java -jar online-bookstore.jar
   ```
## Built With
- Spring Boot - Framework
- Hibernate - ORM
- MySQL - Database
- Docker - Containerization
- Mockito - Testing
- TestContainers - Testing

## Authors
- **Hlib Bykovskyi** - *Initial work* - [hlibmurphy](https://github.com/hlibmurphy)
  
## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgements
- Thanks to the Spring Boot and Hibernate communities for their excellent documentation and support.
- Special thanks to MateAcademy for advising on the development.
