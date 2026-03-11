
# RevHire Testing Artifacts

## Testing Strategy
The application uses unit testing and manual testing to verify functionality.

Testing Types
- Unit Testing
- Integration Testing
- UI Testing
- Functional Testing

## Unit Testing

Framework Used
- JUnit4

Tested Components
- Service Layer
- Repository Layer
- Controller Layer

Example Test Cases

### Test Case 1
Test Case ID: TC_LOGIN_01
Description: Verify user login with valid credentials
Input: valid email and password
Expected Result: User successfully logged in and redirected to dashboard

### Test Case 2
Test Case ID: TC_REGISTER_01
Description: Verify user registration
Input: valid registration details
Expected Result: New user account created successfully

### Test Case 3
Test Case ID: TC_POST_JOB_01
Description: Employer posts a job
Input: valid job details
Expected Result: Job is stored in database

### Test Case 4
Test Case ID: TC_APPLY_JOB_01
Description: Job seeker applies for a job
Input: job id and user session
Expected Result: Application record created

### Test Case 5
Test Case ID: TC_SAVE_JOB_01
Description: Save job functionality
Input: job id
Expected Result: Job stored in saved jobs list

## Integration Testing

Modules Tested Together

Authentication + User Management
Employer Module + Job Module
Job Seeker Module + Application Module

## UI Testing

Pages Tested

Login Page
Register Page
Dashboard
Browse Jobs
Apply Job Page
Resume Builder
Profile Page

## Tools Used

JUnit4
Spring Boot Test
Manual Browser Testing

## Test Environment

Operating System: Windows 10
IDE: Eclipse
Database: MySQL
Browser: Google Chrome
