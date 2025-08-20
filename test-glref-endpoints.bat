@echo off
echo.
echo ================================================
echo   GL REFERENCE CODE FUNCTIONALITY TEST SCRIPT
echo ================================================
echo.
echo Testing GL Reference Code endpoints...
echo.

echo 1. Testing SQL Server Connection...
curl -X GET http://localhost:8080/api/glref-codes/test-connection
echo.
echo.

echo 2. Testing Active GL Codes for Dropdown (Authenticated)...
curl -u admin:admin123 -X GET http://localhost:8080/api/glref-codes/active-for-dropdown
echo.
echo.

echo 3. Testing All Active GL Codes (Admin Access)...
curl -u admin:admin123 -X GET http://localhost:8080/api/glref-codes/active
echo.
echo.

echo Test completed!
pause
