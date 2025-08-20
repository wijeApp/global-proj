# GlobalVen - Enterprise Management System

## Overview
GlobalVen is a comprehensive enterprise management system built with Spring Boot, designed to handle employee management, financial operations, and administrative tasks with robust user authentication and role-based access control.

## Features
- **User Management System** with role-based access control (RBAC)
- **Employee Management** - Complete CRUD operations for employee records
- **Financial Operations** - Transfer management, exchange rates, and transaction tracking
- **Category Management** - Organize and categorize business operations
- **Reporting System** - Generate various business reports
- **Dashboard** - Real-time statistics and analytics

## Technology Stack
- **Backend**: Spring Boot 3.5.4, Java 24
- **Frontend**: Thymeleaf, Bootstrap 5, JavaScript
- **Database**: MySQL
- **Build Tool**: Maven
- **Authentication**: Session-based (JWT-ready architecture)

## User Management and Authorization

### User Roles
The system implements six distinct user roles with hierarchical permissions:

1. **SUPER_ADMIN** - Complete system control
2. **ADMIN** - Administrative access with some restrictions
3. **MANAGER** - Read-only access to most modules with reporting capabilities
4. **HR** - Human Resources management access
5. **FINANCE** - Financial operations access
6. **USER** - Basic read-only access

### Key Security Features
- Password encryption using BCrypt
- Role-based access control (RBAC)
- Session management with timeout
- Audit logging for sensitive operations
- Data-level security based on department/role

For detailed authorization information, see [USER_MANAGEMENT_AND_AUTHORIZATION.md](USER_MANAGEMENT_AND_AUTHORIZATION.md)

## Getting Started

### Prerequisites
- Java 24 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### Database Setup
1. Create MySQL database:
```sql
CREATE DATABASE globalschema;
```

2. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Run SQL scripts in order:
```bash
mysql -u your_username -p globalschema < create_globalschema.sql
mysql -u your_username -p globalschema < create_users_table.sql
mysql -u your_username -p globalschema < create_transfers_table.sql
mysql -u your_username -p globalschema < add_rate_name_column.sql
```

### Running the Application

#### Using Maven
```bash
./mvnw spring-boot:run
```

#### Using Java
```bash
./mvnw clean package
java -jar target/globalven-0.0.1-SNAPSHOT.jar
```

### Default Login Credentials
After initial setup, use these credentials (change immediately in production):

| Role | Username | Password |
|------|----------|----------|
| Super Admin | admin | admin123 |
| HR Admin | hradmin | hr123 |
| Finance Admin | financeadmin | finance123 |
| Test User | testuser | user123 |

## Project Structure
```
globalven/
├── src/
│   ├── main/
│   │   ├── java/com/tas/global/globalven/
│   │   │   ├── controller/     # REST API endpoints
│   │   │   ├── service/        # Business logic
│   │   │   ├── repository/     # Data access layer
│   │   │   └── *.java          # Entity classes
│   │   └── resources/
│   │       ├── templates/      # Thymeleaf templates
│   │       ├── static/         # CSS, JS, images
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## API Documentation

### Authentication Endpoints
- `POST /api/auth/login` - User authentication
- `POST /api/auth/logout` - User logout

### User Management (Admin/Super Admin only)
- `GET /api/users` - List all users
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Employee Management
- `GET /api/employees` - List employees (HR, Admin, Manager)
- `POST /api/employees` - Create employee (HR, Admin)
- `PUT /api/employees/{id}` - Update employee (HR, Admin)
- `DELETE /api/employees/{id}` - Delete employee (HR, Admin)

### Financial Operations
- `GET /api/transfers` - List transfers (Finance, Admin, Manager)
- `POST /api/transfers` - Create transfer (Finance, Admin)
- `PUT /api/transfers/{id}` - Update transfer (Finance, Admin)
- `DELETE /api/transfers/{id}` - Delete transfer (Finance, Admin)

## Security Considerations

### Password Policy
- Minimum 8 characters
- Must contain uppercase, lowercase, number, and special character
- Passwords are encrypted using BCrypt
- Password expiration every 90 days (configurable)

### Session Management
- 30-minute inactivity timeout
- Secure session tokens
- Single session per user (configurable)

### Best Practices
1. Change all default passwords immediately
2. Use HTTPS in production
3. Enable two-factor authentication for admin accounts
4. Regular security audits
5. Keep audit logs for compliance

## Development

### Adding New Modules
1. Create entity class in main package
2. Create repository interface
3. Implement service layer
4. Create REST controller
5. Add authorization annotations
6. Update documentation

### Testing
```bash
./mvnw test
```

### Building for Production
```bash
./mvnw clean package -Pprod
```

## Contributing
1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Documentation
- [User Management Guide](USER_MANAGEMENT_README.md)
- [Authorization Documentation](USER_MANAGEMENT_AND_AUTHORIZATION.md)
- [API Reference](docs/API_REFERENCE.md) *(to be created)*
- [Development Guide](docs/DEVELOPMENT.md) *(to be created)*

## Support
For issues and questions:
1. Check the documentation
2. Review existing issues
3. Create a new issue with detailed information

## License
This project is proprietary software. All rights reserved.

---
*Last updated: [Current Date]*
