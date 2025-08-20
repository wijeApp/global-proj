# API Authorization Guide

## Overview
This document provides detailed authorization requirements for all API endpoints in the GlobalVen system. Each endpoint is documented with its required roles, request/response formats, and special authorization rules.

## Authentication Requirements

### Request Headers
All protected endpoints require the following header:
```
Authorization: Bearer <token>
OR
Cookie: JSESSIONID=<session-id>
```

### Response Codes
- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - No valid authentication
- `403 Forbidden` - Authenticated but lacks permission
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## API Endpoints by Module

### 1. Authentication Module

#### Login
```
POST /api/auth/login
Authorization: None (Public)

Request Body:
{
    "usernameOrEmail": "string",
    "password": "string"
}

Response:
{
    "token": "string",
    "type": "Bearer",
    "id": 1,
    "username": "string",
    "email": "string",
    "roles": ["ROLE_ADMIN"]
}
```

#### Register
```
POST /api/auth/register
Authorization: None (Public)

Request Body:
{
    "username": "string",
    "email": "string",
    "password": "string",
    "firstName": "string",
    "lastName": "string"
}

Response:
{
    "message": "User registered successfully"
}
```

#### Logout
```
POST /api/auth/logout
Authorization: Required (Any authenticated user)

Response:
{
    "message": "Logged out successfully"
}
```

### 2. User Management Module

#### Get All Users
```
GET /api/users
Authorization: ADMIN, SUPER_ADMIN

Query Parameters:
- page (optional): Page number (default: 0)
- size (optional): Page size (default: 20)
- sort (optional): Sort field (default: id)

Response:
[
    {
        "id": 1,
        "username": "string",
        "email": "string",
        "firstName": "string",
        "lastName": "string",
        "role": "ADMIN",
        "status": "ACTIVE",
        "department": "string",
        "lastLogin": "2024-01-01T10:00:00"
    }
]
```

#### Get User by ID
```
GET /api/users/{id}
Authorization: ADMIN, SUPER_ADMIN

Response:
{
    "id": 1,
    "username": "string",
    "email": "string",
    "firstName": "string",
    "lastName": "string",
    "role": "ADMIN",
    "status": "ACTIVE",
    "phoneNumber": "string",
    "department": "string",
    "employeeId": 1,
    "createdDate": "2024-01-01T10:00:00",
    "lastLogin": "2024-01-01T10:00:00"
}
```

#### Create User
```
POST /api/users
Authorization: ADMIN, SUPER_ADMIN

Request Body:
{
    "username": "string",
    "email": "string",
    "password": "string",
    "firstName": "string",
    "lastName": "string",
    "role": "USER",
    "status": "ACTIVE",
    "phoneNumber": "string",
    "department": "string",
    "employeeId": 1
}

Response: Same as Get User by ID
```

#### Update User
```
PUT /api/users/{id}
Authorization: ADMIN, SUPER_ADMIN
Special Rule: ADMIN cannot update SUPER_ADMIN users

Request Body: Same as Create User (password optional)
Response: Same as Get User by ID
```

#### Delete User
```
DELETE /api/users/{id}
Authorization: SUPER_ADMIN only

Response:
{
    "message": "User deleted successfully"
}
```

#### Update User Status
```
PUT /api/users/{id}/status
Authorization: ADMIN, SUPER_ADMIN

Request Body:
{
    "status": "ACTIVE|INACTIVE|SUSPENDED|LOCKED"
}

Response: Same as Get User by ID
```

#### Update User Role
```
PUT /api/users/{id}/role
Authorization: SUPER_ADMIN only

Request Body:
{
    "role": "USER|ADMIN|SUPER_ADMIN|MANAGER|HR|FINANCE"
}

Response: Same as Get User by ID
```

### 3. Employee Management Module

#### Get All Employees
```
GET /api/employees
Authorization: HR, ADMIN, SUPER_ADMIN, MANAGER
Special Rule: MANAGER sees only their department

Query Parameters:
- department (optional): Filter by department
- status (optional): Filter by status
- search (optional): Search by name

Response:
[
    {
        "id": 1,
        "firstName": "string",
        "lastName": "string",
        "email": "string",
        "position": "string",
        "department": "string",
        "status": "ACTIVE",
        "hireDate": "2024-01-01",
        "salary": 50000.00
    }
]
```

#### Get Employee by ID
```
GET /api/employees/{id}
Authorization: HR, ADMIN, SUPER_ADMIN, MANAGER
Special Rule: MANAGER can only view employees in their department

Response:
{
    "id": 1,
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "phone": "string",
    "position": "string",
    "department": "string",
    "status": "ACTIVE",
    "hireDate": "2024-01-01",
    "salary": 50000.00,
    "address": "string",
    "emergencyContact": "string"
}
```

#### Create Employee
```
POST /api/employees
Authorization: HR, ADMIN, SUPER_ADMIN

Request Body:
{
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "phone": "string",
    "position": "string",
    "department": "string",
    "hireDate": "2024-01-01",
    "salary": 50000.00,
    "address": "string"
}

Response: Same as Get Employee by ID
```

#### Update Employee
```
PUT /api/employees/{id}
Authorization: HR, ADMIN, SUPER_ADMIN

Request Body: Same as Create Employee
Response: Same as Get Employee by ID
```

#### Delete Employee
```
DELETE /api/employees/{id}
Authorization: HR, ADMIN, SUPER_ADMIN

Response:
{
    "message": "Employee deleted successfully"
}
```

### 4. Financial Operations Module

#### Get All Transfers
```
GET /api/transfers
Authorization: FINANCE, ADMIN, SUPER_ADMIN, MANAGER
Special Rule: MANAGER sees only department-related transfers

Query Parameters:
- fromDate (optional): Start date filter
- toDate (optional): End date filter
- status (optional): Transfer status
- type (optional): Transfer type

Response:
[
    {
        "id": 1,
        "transferType": "OUTBOUND",
        "amount": 1000.00,
        "currency": "USD",
        "beneficiaryName": "string",
        "beneficiaryAccount": "string",
        "status": "COMPLETED",
        "transferDate": "2024-01-01T10:00:00",
        "employeeId": 1
    }
]
```

#### Create Transfer
```
POST /api/transfers
Authorization: FINANCE, ADMIN, SUPER_ADMIN

Request Body:
{
    "transferType": "INBOUND|OUTBOUND",
    "amount": 1000.00,
    "currency": "USD",
    "beneficiaryName": "string",
    "beneficiaryAccount": "string",
    "beneficiaryBank": "string",
    "purpose": "string",
    "employeeId": 1
}

Response:
{
    "id": 1,
    "transferType": "OUTBOUND",
    "amount": 1000.00,
    "currency": "USD",
    "status": "PENDING",
    "referenceNumber": "TRF2024010001",
    "createdDate": "2024-01-01T10:00:00"
}
```

#### Update Transfer
```
PUT /api/transfers/{id}
Authorization: FINANCE, ADMIN, SUPER_ADMIN
Special Rule: FINANCE can only update if status is PENDING

Request Body: Same as Create Transfer
Response: Same as Create Transfer response
```

#### Delete Transfer
```
DELETE /api/transfers/{id}
Authorization: FINANCE, ADMIN, SUPER_ADMIN
Special Rule: FINANCE can delete only within 24 hours

Response:
{
    "message": "Transfer deleted successfully"
}
```

#### Get Exchange Rates
```
GET /api/rates
Authorization: Any authenticated user

Query Parameters:
- currency (optional): Filter by currency
- date (optional): Rates for specific date

Response:
[
    {
        "id": 1,
        "currency": "USD",
        "buyRate": 1.20,
        "sellRate": 1.25,
        "effectiveDate": "2024-01-01",
        "active": true
    }
]
```

#### Create/Update Rate
```
POST /api/rates
PUT /api/rates/{id}
Authorization: FINANCE, ADMIN, SUPER_ADMIN

Request Body:
{
    "currency": "USD",
    "buyRate": 1.20,
    "sellRate": 1.25,
    "effectiveDate": "2024-01-01"
}

Response: Same as Get Exchange Rates item
```

### 5. Dashboard and Statistics

#### Get Dashboard Statistics
```
GET /api/dashboard/stats
Authorization: Any authenticated user
Special Rule: Data filtered based on user role

Response:
{
    "userStats": {
        "total": 100,
        "active": 85,
        "inactive": 15
    },
    "employeeStats": {
        "total": 500,
        "byDepartment": {
            "IT": 50,
            "HR": 30,
            "Finance": 40
        }
    },
    "financialStats": {
        "totalTransfers": 1000,
        "pendingTransfers": 50,
        "totalAmount": 1000000.00
    }
}
```

### 6. Reports Module

#### Generate User Report
```
GET /api/reports/users
Authorization: ADMIN, SUPER_ADMIN

Query Parameters:
- format (optional): pdf|excel|csv (default: pdf)
- fromDate (optional): Start date
- toDate (optional): End date

Response: File download or JSON data
```

#### Generate Employee Report
```
GET /api/reports/employees
Authorization: HR, ADMIN, SUPER_ADMIN, MANAGER
Special Rule: MANAGER gets only department data

Query Parameters:
- department (optional): Filter by department
- format (optional): pdf|excel|csv
- type: summary|detailed|payroll

Response: File download or JSON data
```

#### Generate Financial Report
```
GET /api/reports/financial
Authorization: FINANCE, ADMIN, SUPER_ADMIN, MANAGER
Special Rule: MANAGER gets limited financial data

Query Parameters:
- type: transfers|transactions|summary
- period: daily|weekly|monthly|yearly
- format (optional): pdf|excel|csv

Response: File download or JSON data
```

## Special Authorization Rules

### 1. Department-Based Access
Managers can only access data for their assigned department:
```javascript
// Pseudo-code for department check
if (user.role === 'MANAGER' && resource.department !== user.department) {
    return 403; // Forbidden
}
```

### 2. Time-Based Restrictions
Some operations have time limitations:
```javascript
// Delete transfer within 24 hours
if (user.role === 'FINANCE' && 
    transfer.createdDate < (now - 24hours)) {
    return 403; // Forbidden
}
```

### 3. Amount-Based Restrictions
Financial operations may have amount limits:
```javascript
// Large transfers need higher approval
if (transfer.amount > 10000 && user.role === 'FINANCE') {
    transfer.status = 'PENDING_APPROVAL';
    // Requires ADMIN or SUPER_ADMIN approval
}
```

### 4. Self-Service Restrictions
Users cannot modify their own critical attributes:
```javascript
// Cannot change own role
if (targetUserId === currentUser.id && 
    updateFields.includes('role')) {
    return 403; // Forbidden
}
```

## Error Response Format

All error responses follow this format:
```json
{
    "timestamp": "2024-01-01T10:00:00",
    "status": 403,
    "error": "Forbidden",
    "message": "You do not have permission to access this resource",
    "path": "/api/users/1"
}
```

## Testing Authorization

### Test Scenarios
1. **Unauthenticated Access**: Try accessing protected endpoints without token
2. **Insufficient Permissions**: Use USER role to access ADMIN endpoints
3. **Cross-Department Access**: MANAGER accessing other department's data
4. **Time-Based Tests**: Try deleting old financial records as FINANCE
5. **Self-Modification**: Try users changing their own roles

### Example Test Cases

#### Test Case 1: User Access Control
```bash
# Should return 403 Forbidden
curl -H "Authorization: Bearer <user-token>" \
     GET http://localhost:8080/api/users
```

#### Test Case 2: Manager Department Restriction
```bash
# Should only return IT department employees
curl -H "Authorization: Bearer <it-manager-token>" \
     GET http://localhost:8080/api/employees
```

#### Test Case 3: Finance Time Restriction
```bash
# Should return 403 if transfer is older than 24 hours
curl -H "Authorization: Bearer <finance-token>" \
     DELETE http://localhost:8080/api/transfers/old-transfer-id
```

## Implementation Checklist

- [ ] Implement authentication filter/middleware
- [ ] Add role checking to each endpoint
- [ ] Implement department-based filtering
- [ ] Add time-based restrictions
- [ ] Implement amount-based approvals
- [ ] Add comprehensive error handling
- [ ] Log all authorization failures
- [ ] Implement rate limiting
- [ ] Add API documentation
- [ ] Create integration tests

---

*Last Updated: [Current Date]*
*Version: 1.0*
