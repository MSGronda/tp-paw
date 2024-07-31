<img src="webapp/src/main/webapp/static/uni-transparent.png" alt="Uni" width="200"/>

## Description

Uni is a webapp where university students from ITBA can manage and track their progress through their careers and plan their semesters. 

## Requirements

- Use Java 8.
- Have a PostgresSQL running on port 5432
- Complete the application-dev.properties file 

## Deployment

Run the following commands: 
```
    mvn clean package
```
## Running the tests
```
    mvn test
```

## Concepts implemented

  - Layered Arquitecture Pattern
  - Model View Controller (MVC)
  - Authentication & Authorization
  - Object Relational Mapper (ORM)
  - True REST API
  - Caching 

## Technologies Used

  - Java
  - Jersey
  - PostgreSQL
  - Maven
  - Thymeleaf
  - FlyWay
  - React Framework
  - Mantine UI kit
  - Mockito
  - JUnit

## REST API documentation
https://app.swaggerhub.com/apis-docs/MROJASPELLICCIA/paw-2023a-06/1.0.0

## Features

  - Email Verification
  - User registration
  - User login
  - Password Recovery
  - User Roles (User and Moderator)
  - Profile view
  - Profile editing (username, password and picture)
  - Subject Search
  - Subject Filtering and Sorting
  - Subject Review
  - Review Voting
  - Review sorting
  - Review Paging in subject information and user profile (10 reviews per page)
  - Semester Builder with insights 
  - Previous semesters
  - Progress tracking and history
  - Subject creation, editing and deletion
  - Degree creation, editing and deletion
  - Logging
  - Mailing
  - Internationalization (English and Spanish)

### Feature Explanation

  - Mailing:
    - It is used to verify an email linked to a new user's account.
    - It is used to send a password recovery link to a user.
    - It is used to send a notification to a user when a subject is marked as completed but the user did not post review for it (within 10 minutes of completion).
  - User Roles: 
    - User can post and vote reviews, edit and delete their own reviews.
    - Moderator can do everything a user can do, but can also delete any review and promote a user to Moderator. Also create, edit and delete subjects and degrees.
  - Moderator Account:
    - Username: paw.uni.mod@gmail.com
    - Password: Pawuni23

## Authors

  - Carro Wetzel, Andrés
  - Casiraghi, Marcos
  - Gronda, Marcos
  - Morantes, Agustín
  - Rojas Pelliccia, Máximo 


## Acknowledgments

  - Sotuyo Dodero, Juan Martin
  - D'Onofrio, Nicolás
  - Sanguineti Arena, Francisco Javier
  - Arce Doncella, Julian Francisco
