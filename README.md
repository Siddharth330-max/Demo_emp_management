# Employee Leave Management System

A Spring Boot REST API project for managing employees and their leave requests.

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- MySQL
- Maven
- Postman

## Features

### Employee Management
- Create an employee
- Get employee by ID
- List all employees
- Update employee details

### Leave Management
- Submit leave request
- View leave request
- View employee leave history
- Cancel pending leave
- Validate leave dates
- Prevent overlapping leave requests

### Approval Workflow
- Approve pending leave
- Reject pending leave
- Prevent self-approval
- Record manager, decision and action time

### Leave Balance
- 20 paid leave days per employee per year
- Deduct balance after approval
- Check insufficient leave balance
- Weekends are not counted as leave days

## API Endpoints

```text
GET    /emp
GET    /emp/{id}
POST   /emp
PUT    /emp/{id}

POST   /leave
GET    /leave/{id}
GET    /leave/employee/{employeeId}
PUT    /leave/{id}/cancel
PUT    /leave/{id}/approve/{managerId}
PUT    /leave/{id}/reject/{managerId}
