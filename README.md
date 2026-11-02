# 💼 Job Portal Application

Hire-Hub is a **Spring Boot-based Job Portal application** designed to connect job seekers with recruiters. It provides functionalities for recruiters to post and manage jobs and for job seekers to search for opportunities and apply for jobs.

## 🛠️ Tech Stack

- **Backend:** Java, Spring Boot
- **ORM:** Spring Data JPA, Hibernate
- **Database:** MySQL
- **Security:** Spring Security, JWT
- **API:** RESTful APIs
- **Build Tool:** Maven

## 🚀 Features

### 👤 User Management
- User registration and login
- Role-based access for Job Seekers and Recruiters
- JWT-based authentication and authorization

### 💼 Job Management
- Recruiters can create, update, and manage job postings.
- Job seekers can browse available job opportunities.
- Search and filter jobs based on relevant criteria.
- Pagination and sorting for job listings.

### 📄 Application Management
- Job seekers can apply for available jobs.
- Recruiters can manage job applications.
- Resume upload functionality.

### 🔐 Security
- Spring Security integration
- JWT-based authentication
- Role-based API authorization
- Password encryption using BCrypt

## 🏗️ Architecture

The application follows a layered architecture to separate responsibilities.

                 CLIENT / API CONSUMER
                          |
                          v
              SPRING BOOT REST CONTROLLERS
                          |
                          v
               SPRING SECURITY + JWT
                          |
                          v
                    SERVICE LAYER
                          |
             +------------+------------+
             |            |            |
             v            v            v
        USER SERVICE  JOB SERVICE  APPLICATION SERVICE
             |            |            |
             +------------+------------+
                          |
                          v
                   REPOSITORY LAYER
                          |
                          v
                   SPRING DATA JPA
                          |
                          v
                     MYSQL DATABASE


              ADDITIONAL COMPONENTS
              ---------------------

              DTOs
               |
               v
          Request / Response Mapping

              FILE STORAGE
               |
               v
          Resume Upload & Storage

Layer Responsibilities
- Controller Layer: Handles incoming HTTP requests and returns API responses.
- Security Layer: Manages JWT authentication and role-based authorization.
- Service Layer: Contains business logic for users, jobs, and applications.
- Repository Layer: Communicates with the database using Spring Data JPA.
- Database Layer: MySQL stores user, job, and application information.
- DTO Layer: Transfers data between client and backend.
- File Storage: Handles resume uploads.
## ⚙️ Getting Started

### 1. Clone the Repository

```bash
git clone YOUR_REPOSITORY_URL
```

### 2. Configure MySQL

Create a database:

```sql
CREATE DATABASE hire_hub;
```

Update your database credentials in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hire_hub
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Configure your JWT secret and other application properties as required.

### 3. Run the Application

Run the Spring Boot application from your IDE or using Maven:

```bash
mvn spring-boot:run
```

The application will start on the configured server port.

## 📚 Key Learnings

- Building RESTful APIs using Spring Boot
- Implementing authentication and authorization using Spring Security and JWT
- Database integration using Spring Data JPA and MySQL
- Designing layered application architecture
- Implementing pagination, sorting, and dynamic filtering
- DTO-based request and response handling
- Global exception handling and input validation
- File upload handling

## 🔮 Future Improvements

- Frontend integration
- Email notifications for application updates
- Advanced job recommendations
- Deployment to cloud platforms

## 👨‍💻 Author

**Subhojit Roy**

Java | Spring Boot | Backend Development
