# Role-Permission Matrix for GlobalVen

## Overview
This document provides a detailed matrix of permissions for each user role in the GlobalVen system. Use this as a reference for implementing and verifying access controls.

## Permission Legend
- ✅ Full Access (Create, Read, Update, Delete)
- 📖 Read Only
- ✏️ Create/Update Only
- 🗑️ Delete Only
- ❌ No Access
- ⚡ Special Conditions Apply

## Detailed Permission Matrix

### User Management Module

| Operation | SUPER_ADMIN | ADMIN | MANAGER | HR | FINANCE | USER |
|-----------|-------------|--------|---------|-----|---------|------|
| **Users** |
| View User List | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| View User Details | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| Create User | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| Update User | ✅ | ⚡¹ | ❌ | ❌ | ❌ | ❌ |
| Delete User | ✅ | ⚡¹ | ❌ | ❌ | ❌ | ❌ |
| Change User Role | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |
| Reset Password | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| View Audit Logs | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |

¹ *Cannot modify or delete SUPER_ADMIN accounts*

### Employee Management Module

| Operation | SUPER_ADMIN | ADMIN | MANAGER | HR | FINANCE | USER |
|-----------|-------------|--------|---------|-----|---------|------|
| **Employee Records** |
| View Employee List | ✅ | ✅ | 📖 | ✅ | ❌ | ❌ |
| View Employee Details | ✅ | ✅ | ⚡² | ✅ | ❌ | ❌ |
| Create Employee | ✅ | ✅ | ❌ | ✅ | ❌ | ❌ |
| Update Employee | ✅ | ✅ | ❌ | ✅ | ❌ | ❌ |
| Delete Employee | ✅ | ✅ | ❌ | ✅ | ❌ | ❌ |
| **Employee Documents** |
| Upload Documents | ✅ | ✅ | ❌ | ✅ | ❌ | ❌ |
| View Documents | ✅ | ✅ | ⚡² | ✅ | ❌ | ❌ |
| Delete Documents | ✅ | ✅ | ❌ | ✅ | ❌ | ❌ |
| **Salary Information** |
| View Salary | ✅ | ✅ | ❌ | ✅ | ✅ | ❌ |
| Update Salary | ✅ | ✅ | ❌ | ✅ | ❌ | ❌ |

² *Managers can only view employees in their department*

### Financial Operations Module

| Operation | SUPER_ADMIN | ADMIN | MANAGER | HR | FINANCE | USER |
|-----------|-------------|--------|---------|-----|---------|------|
| **Transfers** |
| View Transfer List | ✅ | ✅ | 📖 | ❌ | ✅ | ❌ |
| View Transfer Details | ✅ | ✅ | 📖 | ❌ | ✅ | ❌ |
| Create Transfer | ✅ | ✅ | ❌ | ❌ | ✅ | ❌ |
| Update Transfer | ✅ | ✅ | ❌ | ❌ | ✅ | ❌ |
| Delete Transfer | ✅ | ✅ | ❌ | ❌ | ✅ | ❌ |
| Approve Transfer | ✅ | ✅ | ❌ | ❌ | ⚡³ | ❌ |
| **Exchange Rates** |
| View Rates | ✅ | ✅ | 📖 | ❌ | ✅ | 📖 |
| Create Rate | ✅ | ✅ | ❌ | ❌ | ✅ | ❌ |
| Update Rate | ✅ | ✅ | ❌ | ❌ | ✅ | ❌ |
| Delete Rate | ✅ | ✅ | ❌ | ❌ | ✅ | ❌ |
| **Transactions** |
| View All Transactions | ✅ | ✅ | ⚡⁴ | ❌ | ✅ | ❌ |
| Create Transaction | ✅ | ✅ | ❌ | ❌ | ✅ | ❌ |
| Update Transaction | ✅ | ✅ | ❌ | ❌ | ✅ | ❌ |
| Delete Transaction | ✅ | ✅ | ❌ | ❌ | ⚡⁵ | ❌ |

³ *Finance can approve transfers up to a certain limit*
⁴ *Managers can view transactions for their department*
⁵ *Finance can delete only within 24 hours of creation*

### Category Management Module

| Operation | SUPER_ADMIN | ADMIN | MANAGER | HR | FINANCE | USER |
|-----------|-------------|--------|---------|-----|---------|------|
| View Categories | ✅ | ✅ | 📖 | 📖 | 📖 | 📖 |
| Create Category | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| Update Category | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| Delete Category | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |

### Reports Module

| Operation | SUPER_ADMIN | ADMIN | MANAGER | HR | FINANCE | USER |
|-----------|-------------|--------|---------|-----|---------|------|
| **System Reports** |
| User Activity Report | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| System Audit Report | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| **HR Reports** |
| Employee Report | ✅ | ✅ | ⚡⁶ | ✅ | ❌ | ❌ |
| Attendance Report | ✅ | ✅ | ⚡⁶ | ✅ | ❌ | ❌ |
| Leave Report | ✅ | ✅ | ⚡⁶ | ✅ | ❌ | ❌ |
| **Financial Reports** |
| Transaction Report | ✅ | ✅ | ⚡⁶ | ❌ | ✅ | ❌ |
| Transfer Report | ✅ | ✅ | ⚡⁶ | ❌ | ✅ | ❌ |
| Exchange Rate History | ✅ | ✅ | 📖 | ❌ | ✅ | ❌ |
| Budget Report | ✅ | ✅ | ⚡⁶ | ❌ | ✅ | ❌ |
| **Custom Reports** |
| Generate Custom Report | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| Schedule Reports | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |

⁶ *Managers can generate reports only for their department*

### Dashboard Access

| Component | SUPER_ADMIN | ADMIN | MANAGER | HR | FINANCE | USER |
|-----------|-------------|--------|---------|-----|---------|------|
| System Overview | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| User Statistics | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| Employee Statistics | ✅ | ✅ | 📖 | ✅ | ❌ | ❌ |
| Financial Summary | ✅ | ✅ | 📖 | ❌ | ✅ | ❌ |
| Department Summary | ✅ | ✅ | ⚡⁷ | ⚡⁷ | ⚡⁷ | ❌ |
| Recent Activities | ✅ | ✅ | ⚡⁷ | ⚡⁷ | ⚡⁷ | ❌ |

⁷ *Shows only relevant department/module data*

### System Settings

| Setting | SUPER_ADMIN | ADMIN | MANAGER | HR | FINANCE | USER |
|---------|-------------|--------|---------|-----|---------|------|
| **General Settings** |
| Company Information | ✅ | 📖 | ❌ | ❌ | ❌ | ❌ |
| System Configuration | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |
| Email Templates | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| **Security Settings** |
| Password Policy | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |
| Session Settings | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |
| IP Whitelist | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |
| **Module Settings** |
| HR Settings | ✅ | ✅ | ❌ | ✏️ | ❌ | ❌ |
| Finance Settings | ✅ | ✅ | ❌ | ❌ | ✏️ | ❌ |
| **Backup & Restore** |
| Create Backup | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |
| Restore Backup | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |

## Special Permissions

### Cross-Module Permissions

| Scenario | Roles Allowed | Conditions |
|----------|---------------|------------|
| View Employee Financial Data | SUPER_ADMIN, ADMIN, FINANCE | For payroll processing |
| Modify User Department | SUPER_ADMIN, ADMIN, HR | When employee changes department |
| Emergency Access Override | SUPER_ADMIN | With audit log entry |
| Bulk Operations | SUPER_ADMIN, ADMIN | With confirmation |
| Data Export | SUPER_ADMIN, ADMIN | Specific modules only for others |

### Time-Based Restrictions

| Operation | Time Restriction | Applicable Roles |
|-----------|------------------|------------------|
| Delete Financial Records | Within 24 hours | FINANCE |
| Modify Submitted Reports | Within 48 hours | All except USER |
| Change Critical Settings | Business hours only | SUPER_ADMIN |

### Approval Workflows

| Action | Initiator | Approver |
|--------|-----------|----------|
| Large Transfer (>$10,000) | FINANCE | ADMIN or SUPER_ADMIN |
| User Role Change | ADMIN | SUPER_ADMIN |
| Mass Data Update | Any authorized | Next higher role |
| System Setting Change | ADMIN | SUPER_ADMIN |

## Implementation Notes

### Frontend Considerations
1. Hide UI elements based on permissions
2. Disable buttons for unauthorized actions
3. Show appropriate error messages
4. Implement client-side permission checks

### Backend Enforcement
1. Validate permissions on every API call
2. Implement middleware for permission checking
3. Log all permission denials
4. Return appropriate HTTP status codes (403 Forbidden)

### Database Level
1. Consider row-level security for sensitive data
2. Implement database views for role-based data filtering
3. Audit all data access

## Permission Inheritance

### Role Hierarchy
```
SUPER_ADMIN
    └── ADMIN
            └── MANAGER
                    └── HR (specialized)
                    └── FINANCE (specialized)
                            └── USER
```

**Note**: HR and FINANCE are specialized roles with specific module access, not hierarchical under MANAGER.

## Compliance Notes

1. **Segregation of Duties**: HR cannot access financial operations, Finance cannot access employee personal data
2. **Audit Trail**: All permission usage must be logged
3. **Regular Review**: Permission matrix should be reviewed quarterly
4. **Principle of Least Privilege**: Users should have minimum necessary permissions

---

*Last Updated: [Current Date]*
*Version: 1.0*
