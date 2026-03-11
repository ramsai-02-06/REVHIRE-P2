
# RevHire – Job Portal Application

## Project Overview
RevHire is a full-stack monolithic job portal application developed using Java and Spring Boot. 
The platform connects employers and job seekers where employers can post jobs and job seekers 
can search, apply, and manage applications.

This project follows a layered architecture using Spring MVC.

## Technologies Used

Backend
- Java
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA

Frontend
- Thymeleaf
- HTML
- CSS
- Bootstrap
- JavaScript

Database
- MySQL

Tools
- Maven
- Git
- Log4J
- JUnit4

## System Architecture

Client (Browser)
→ Thymeleaf UI
→ Spring MVC Controllers
→ Service Layer (Business Logic)
→ Repository Layer (Spring Data JPA)
→ MySQL Database

## Modules

### Authentication Module
- User Registration
- User Login
- Password Reset

### Employer Module
- Employer Dashboard
- Post Job
- Edit Job
- Delete Job
- View Applicants

### Job Seeker Module
- Job Seeker Dashboard
- Browse Jobs
- Apply for Job
- Save Jobs
- View Applications

### Resume Builder
- Education
- Experience
- Skills
- Projects
- Certifications
- Upload Resume

### Notification Module
- System notifications for job applications

## Database Entities

User
Employer
JobSeeker
Job
Application
SavedJob
Resume
Education
Experience
Skill
Project
Certification
Notification

## How to Run the Project

1. Clone the repository
2. Import project into Eclipse or IntelliJ
3. Configure MySQL database in application.properties
4. Run the Spring Boot application

Application will run on:

http://localhost:8585

## Project Structure

controller
service
repository
entity
security
config
templates
static

## Future Improvements

- Microservices architecture
- Docker containerization
- Cloud deployment (AWS)
- Advanced job recommendation system

## Author
RevHire Training Project
