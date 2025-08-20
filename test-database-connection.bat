@echo off
echo Testing Database Connections for GlobalVen Application
echo =======================================================
echo.

set BASE_URL=http://localhost:8080/api/test

echo Checking if application is running...
curl -s http://localhost:8080 > nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Application does not appear to be running on localhost:8080
    echo Please start the Spring Boot application first using: mvn spring-boot:run
    pause
    exit /b 1
)
echo SUCCESS: Application is running
echo.

echo Running Database Connection Tests...
echo.

echo 1. MySQL Connection Test
echo Endpoint: %BASE_URL%/mysql-connection
curl -s "%BASE_URL%/mysql-connection" | findstr /C:"status"
echo.

echo 2. SQL Server Connection Test
echo Endpoint: %BASE_URL%/sqlserver-connection
curl -s "%BASE_URL%/sqlserver-connection" | findstr /C:"status"
echo.

echo 3. SQL Server Query Test
echo Endpoint: %BASE_URL%/sqlserver-query
curl -s "%BASE_URL%/sqlserver-query" | findstr /C:"status"
echo.

echo 4. Stored Procedure Check
echo Endpoint: %BASE_URL%/stored-procedure-check
curl -s "%BASE_URL%/stored-procedure-check" | findstr /C:"status"
echo.

echo 5. Direct Stored Procedure Execution
echo Endpoint: %BASE_URL%/direct-stored-procedure
curl -s "%BASE_URL%/direct-stored-procedure" | findstr /C:"status"
echo.

echo 6. GL Reference Codes API Test
echo Endpoint: %BASE_URL%/glref-codes-api
curl -s "%BASE_URL%/glref-codes-api" | findstr /C:"status"
echo.

echo 7. Comprehensive GL Codes Test
echo Endpoint: %BASE_URL%/glref-codes-comprehensive
curl -s "%BASE_URL%/glref-codes-comprehensive" | findstr /C:"overallStatus"
echo.

echo 8. Connection Information
echo Endpoint: %BASE_URL%/connection-info
curl -s "%BASE_URL%/connection-info" | findstr /C:"status"
echo.

echo =======================================================
echo Database Connection Tests Completed!
echo.
echo To view detailed test results in a web interface:
echo Open: http://localhost:8080/database-test
echo.
echo Note: You may need to log in with admin credentials to access the test endpoints.
echo.
pause
