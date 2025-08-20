# Database Connection Testing Guide

This guide will help you test and verify the SQL Server connection and GL reference codes API integration.

## 🚀 Quick Start

### 1. Start the Application
```bash
mvn spring-boot:run
```

### 2. Run Automated Tests
**Windows PowerShell:**
```powershell
./test-database-connection.ps1
```

**Windows Command Prompt:**
```cmd
test-database-connection.bat
```

### 3. Web Interface Testing
Open your browser and navigate to:
```
http://localhost:8080/database-test
```
*Note: You may need to log in with admin credentials*

## 📋 Test Endpoints

| Endpoint | Description | Purpose |
|----------|-------------|---------|
| `/api/test/mysql-connection` | Test MySQL connection | Verify primary database connectivity |
| `/api/test/sqlserver-connection` | Test SQL Server connection | Verify TasGlobalDB connectivity |
| `/api/test/sqlserver-query` | Test SQL Server queries | Verify basic SQL operations |
| `/api/test/stored-procedure-check` | Check stored procedure | Verify GET_GLREF_CODES exists |
| `/api/test/direct-stored-procedure` | Execute stored procedure | Test direct procedure execution |
| `/api/test/glref-codes-api` | Test GL codes API | Verify service layer functionality |
| `/api/test/glref-codes-comprehensive` | Comprehensive API test | Test all GL codes methods |
| `/api/test/connection-info` | Connection information | Get detailed connection details |

## 🔧 What Each Test Validates

### 1. MySQL Connection Test
- ✅ Primary database connectivity
- ✅ Database metadata retrieval
- ✅ Connection pool status

### 2. SQL Server Connection Test
- ✅ TasGlobalDB connectivity (124.43.199.76:1497)
- ✅ Authentication with sa/m@18953789
- ✅ Database version and driver information

### 3. SQL Server Query Test
- ✅ Basic SQL query execution
- ✅ System information retrieval
- ✅ Current timestamp functionality

### 4. Stored Procedure Check
- ✅ GET_GLREF_CODES procedure existence
- ✅ Procedure metadata and definition
- ✅ Database schema validation

### 5. Direct Stored Procedure Execution
- ✅ Direct EXEC GET_GLREF_CODES execution
- ✅ Result set structure validation
- ✅ Data retrieval performance

### 6. GL Codes API Test
- ✅ Service layer functionality
- ✅ Data transformation (DB → DTO)
- ✅ Error handling and logging

### 7. Comprehensive API Test
- ✅ All service methods (getAllGlRefCodes, getActiveGlRefCodes, etc.)
- ✅ Search functionality
- ✅ Category filtering
- ✅ Statistics calculation

## 📊 Expected Results

### Successful Connection
```json
{
  "status": "SUCCESS",
  "connected": true,
  "databaseProductName": "Microsoft SQL Server",
  "databaseProductVersion": "15.00.2000",
  "url": "jdbc:sqlserver://124.43.199.76:1497;databaseName=TasGlobalDB",
  "username": "sa"
}
```

### Successful Stored Procedure
```json
{
  "status": "SUCCESS",
  "storedProcedureExists": true,
  "procedureCount": 1,
  "procedureDetails": {
    "name": "GET_GLREF_CODES",
    "create_date": "2024-01-01",
    "modify_date": "2024-01-01"
  }
}
```

### Successful API Test
```json
{
  "status": "SUCCESS",
  "recordCount": 150,
  "executionTimeMs": 245,
  "hasData": true,
  "activeRecords": 120,
  "inactiveRecords": 30,
  "uniqueCategories": 8
}
```

## 🚨 Troubleshooting

### Common Issues

#### 1. SQL Server Connection Failed
**Error:** `Connection refused` or `Login failed`

**Solutions:**
- Verify SQL Server is running on 124.43.199.76:1497
- Check firewall settings
- Validate credentials (sa/m@18953789)
- Ensure SQL Server allows remote connections

#### 2. Stored Procedure Not Found
**Error:** `Could not find stored procedure 'GET_GLREF_CODES'`

**Solutions:**
- Verify procedure exists in TasGlobalDB
- Check procedure name spelling
- Ensure user has EXECUTE permissions

#### 3. Authentication Failed
**Error:** `Access denied` or `Unauthorized`

**Solutions:**
- Log in with admin credentials
- Check user roles in the application
- Verify Spring Security configuration

#### 4. Timeout Errors
**Error:** `Connection timeout` or `Read timeout`

**Solutions:**
- Check network connectivity
- Increase connection timeout settings
- Verify SQL Server performance

### Configuration Check

Verify your `application.properties` has the correct SQL Server configuration:
```properties
# Microsoft SQL Server Database Connection (Alternative)
# spring.datasource.url=jdbc:sqlserver://124.43.199.76:1497;databaseName=TasGlobalDB;encrypt=false;trustServerCertificate=true
# spring.datasource.username=sa
# spring.datasource.password=m@18953789
# spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

## 📈 Performance Benchmarks

### Expected Performance Metrics
- **Connection establishment:** < 2 seconds
- **Stored procedure execution:** < 500ms
- **API response time:** < 1 second
- **Data retrieval (100 records):** < 300ms

### Performance Testing
The test endpoints include execution time measurements:
```json
{
  "executionTimeMs": 245,
  "recordCount": 150
}
```

## 🔄 Continuous Testing

### Automated Testing Schedule
1. Run tests after each deployment
2. Weekly connection health checks
3. Monthly performance benchmarking
4. Immediate testing after configuration changes

### Monitoring Integration
The health check endpoints can be integrated with monitoring tools:
- `/api/test/sqlserver-connection` - Connection health
- `/api/test/glref-codes-api` - API functionality
- `/api/glref-codes/health` - Service health check

## 📝 Test Results Interpretation

### Success Indicators
- ✅ All connection tests return `"status": "SUCCESS"`
- ✅ Stored procedure exists and is executable
- ✅ API returns data within performance thresholds
- ✅ No authentication or authorization errors

### Warning Signs
- ⚠️ Slow response times (> 2 seconds)
- ⚠️ Intermittent connection failures
- ⚠️ Empty result sets when data expected
- ⚠️ High error rates in comprehensive tests

### Critical Issues
- ❌ Complete connection failures
- ❌ Authentication errors
- ❌ Stored procedure not found
- ❌ Service layer exceptions

## 🛠️ Next Steps After Testing

1. **If all tests pass:** GL codes API is ready for production use
2. **If connection issues:** Review network and database configuration
3. **If API issues:** Check service layer implementation and error handling
4. **If performance issues:** Optimize queries and connection pooling

## 📞 Support

For issues with database testing:
1. Check application logs for detailed error messages
2. Verify database server status and connectivity
3. Review Spring Boot configuration
4. Test direct database connection outside the application

---

**Note:** This testing framework provides comprehensive validation of the SQL Server integration and GL reference codes API functionality. Regular testing ensures reliable operation in production environments.
