# User Management System

This document describes the User Management System added to the GlobalVen application, providing comprehensive user administration and role-based access control.

## Features

### User Management
- **Create Users**: Add new users with comprehensive profile information
- **Update Users**: Modify user details, roles, and status
- **Delete Users**: Remove users from the system
- **Search & Filter**: Find users by name, role, status, or department
- **Bulk Operations**: Quick status changes for multiple users

### Role-Based Access Control
The system supports the following user roles:

1. **Super Administrator**: Full system access and user management
2. **Administrator**: System administration with limited user management
3. **Manager**: Department management and reporting access
4. **HR**: Human resources management and employee oversight
5. **Finance**: Financial operations and reporting access
6. **User**: Basic system access with limited permissions

### User Status Management
Users can have the following statuses:

- **Active**: Full system access
- **Inactive**: Limited or no system access
- **Suspended**: Temporarily disabled account
- **Pending**: Awaiting approval or activation
- **Locked**: Security-locked account

## Technical Implementation

### Database Schema

The system uses a `users` table with the following structure:

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role ENUM('USER', 'ADMIN', 'SUPER_ADMIN', 'MANAGER', 'HR', 'FINANCE'),
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING', 'LOCKED'),
    phone_number VARCHAR(20),
    profile_image VARCHAR(255),
    last_login DATETIME,
    created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    department VARCHAR(50),
    employee_id BIGINT,
    -- Additional indexes and constraints
);
```

### API Endpoints

The User Management API provides the following endpoints:

#### User CRUD Operations
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

#### User Queries
- `GET /api/users/username/{username}` - Get user by username
- `GET /api/users/role/{role}` - Get users by role
- `GET /api/users/status/{status}` - Get users by status
- `GET /api/users/department/{department}` - Get users by department
- `GET /api/users/search/name?name={name}` - Search users by name
- `GET /api/users/active` - Get active users
- `GET /api/users/admins` - Get admin users
- `GET /api/users/statistics` - Get user statistics

#### User Operations
- `PUT /api/users/{id}/status` - Update user status
- `PUT /api/users/{id}/role` - Update user role
- `GET /api/users/check/username/{username}` - Check username availability
- `GET /api/users/check/email/{email}` - Check email availability

### Frontend Interface

The user management interface (`/users`) provides:

#### Dashboard
- Real-time statistics showing total, active, admin, and inactive users
- Visual cards with color-coded metrics
- Quick overview of system user distribution

#### User Table
- Comprehensive user listing with avatar, details, and status
- Role and status badges with color coding
- Search and filtering capabilities
- Sortable columns for better data organization

#### User Operations
- **Add User Modal**: Comprehensive form for creating new users
- **Edit User Modal**: Update existing user information
- **View User Modal**: Detailed user profile display
- **Quick Actions**: Dropdown menu for rapid status changes
- **Bulk Operations**: Multiple user selection and batch operations

#### Search and Filtering
- **Text Search**: Search by name or username
- **Role Filter**: Filter by user role
- **Status Filter**: Filter by user status
- **Department Filter**: Filter by department
- **Combined Filters**: Multiple filter combinations

## Security Considerations

### Password Management
- Passwords should be hashed using strong algorithms (bcrypt, scrypt, or Argon2)
- Implement password complexity requirements
- Consider password history to prevent reuse
- Implement password expiration policies

### Access Control
- Implement proper authentication middleware
- Use role-based authorization for sensitive operations
- Log all administrative actions for audit trails
- Implement session management and timeout policies

### Data Protection
- Sanitize all user inputs
- Implement proper validation for all fields
- Use HTTPS for all communications
- Implement proper error handling without exposing sensitive information

## Usage Instructions

### Accessing User Management
1. Navigate to the application
2. Click on "Master Settings" in the navigation menu
3. Select "User Management" from the dropdown

### Creating a New User
1. Click the "Add User" button
2. Fill in all required fields (marked with *)
3. Select appropriate role and status
4. Optionally link to an existing employee record
5. Click "Create User"

### Managing User Roles
1. Find the user in the table
2. Click the edit button (pencil icon)
3. Update the role in the dropdown
4. Save changes

### Quick Status Changes
1. Click the three-dots menu next to a user
2. Select the desired status change:
   - Activate
   - Deactivate
   - Suspend
3. Confirm the action

### Searching Users
1. Use the search box to find users by name
2. Use the filter dropdowns to narrow results:
   - Filter by role
   - Filter by status
   - Filter by department
3. Combine multiple filters for precise results

## Default Admin Accounts

The system comes with pre-configured admin accounts:

- **Username**: `admin`, **Role**: Super Admin, **Department**: IT
- **Username**: `hradmin`, **Role**: HR, **Department**: HR
- **Username**: `financeadmin`, **Role**: Finance, **Department**: Finance
- **Username**: `testuser`, **Role**: User, **Department**: IT

⚠️ **Security Warning**: Change default passwords immediately in production!

## Integration with Employee Management

The user management system can be linked with the existing employee management:

- Link users to employee records via `employee_id`
- Synchronize department information
- Maintain consistent user data across systems
- Enable single sign-on capabilities

## Best Practices

### User Administration
1. **Principle of Least Privilege**: Assign minimum necessary permissions
2. **Regular Audits**: Review user accounts and permissions regularly
3. **Inactive Account Management**: Disable accounts for departed employees
4. **Role Segregation**: Separate administrative and operational roles

### System Maintenance
1. **Regular Backups**: Backup user data and configurations
2. **Monitor Activities**: Track user login patterns and activities
3. **Update Policies**: Keep security policies current
4. **Documentation**: Maintain current user management procedures

## Troubleshooting

### Common Issues
1. **Username Already Exists**: Check for duplicate usernames in the system
2. **Email Already Exists**: Verify email uniqueness across all users
3. **Role Assignment Errors**: Ensure proper permissions for role changes
4. **Status Update Failures**: Check for system constraints or business rules

### Support
For technical support or questions about the user management system:
1. Check application logs for detailed error messages
2. Verify database connectivity and permissions
3. Review API endpoint responses for specific error codes
4. Consult system administrator for advanced troubleshooting

## Related Documentation

For complete implementation details and authorization guidelines, please refer to:

1. **[User Management and Authorization Guide](USER_MANAGEMENT_AND_AUTHORIZATION.md)**
   - Comprehensive overview of the authorization system
   - User roles and permissions details
   - Security best practices

2. **[Role-Permission Matrix](ROLE_PERMISSION_MATRIX.md)**
   - Detailed permission matrix for all roles
   - Module-specific access controls
   - Special permission rules

3. **[API Authorization Guide](API_AUTHORIZATION_GUIDE.md)**
   - Complete API endpoint documentation
   - Authorization requirements for each endpoint
   - Request/response formats

4. **[Implementation Guide](USER_MANAGEMENT_IMPLEMENTATION_GUIDE.md)**
   - Step-by-step implementation instructions
   - Code examples and configurations
   - Testing and deployment checklist

---

*This user management system provides a robust foundation for user administration and can be extended with additional features like single sign-on, multi-factor authentication, and advanced reporting as needed.*



