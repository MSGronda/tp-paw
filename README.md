# Project Title

Uni is a project intended for students to review subjects completed in their curriculum. It is a webpage for students to share their experiences and help others make informed decisions when applying for new subjects.


## Running the tests
```
    mvn test
```

## Deployment

To run the project locally a file named `application-dev.properties` must be created in the `src/main/resources` folder in the webapp module. This file must contain the following structure:

```
    auth.rememberMe.key=

    baseUrl=http://localhost:8080

    mail.host=
    mail.port=
    mail.username=
    mail.displayname=Uni
    mail.password=

    db.url=
    db.username=
    db.password=
```
Also, the local database must be filed with the following SQL script:

[Master SQL](https://drive.google.com/file/d/1o4RwuEiW01c00MXlRF4y0xqjixtE2NAK/view?usp=sharing)

## Technologies Used

  - Java
  - Spring MVC - Security
  - PostgreSQL
  - Maven
  - Thymeleaf
  - FlyWay

## Versioning

This is the first issue of the project due to 17/5/2023 at 19hs

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
  - Semester Builder
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
    - Moderator can do everything a user can do, but can also delete any review and promote a user to Moderator.
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
