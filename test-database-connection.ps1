# PowerShell script to test database connections
# Run this after starting the Spring Boot application

Write-Host "Testing Database Connections for GlobalVen Application" -ForegroundColor Green
Write-Host "=======================================================" -ForegroundColor Green

$baseUrl = "http://localhost:8080/api/test"

# Function to make HTTP requests and display results
function Test-Endpoint {
    param(
        [string]$endpoint,
        [string]$description
    )
    
    Write-Host "`n$description" -ForegroundColor Yellow
    Write-Host "Endpoint: $endpoint" -ForegroundColor Gray
    
    try {
        $response = Invoke-RestMethod -Uri $endpoint -Method GET -ErrorAction Stop
        Write-Host "✓ SUCCESS" -ForegroundColor Green
        
        if ($response.status -eq "SUCCESS") {
            Write-Host "Status: $($response.status)" -ForegroundColor Green
            
            # Show key information based on endpoint type
            if ($response.connected -ne $null) {
                Write-Host "Connected: $($response.connected)" -ForegroundColor Cyan
            }
            if ($response.databaseProductName) {
                Write-Host "Database: $($response.databaseProductName)" -ForegroundColor Cyan
            }
            if ($response.recordCount -ne $null) {
                Write-Host "Records: $($response.recordCount)" -ForegroundColor Cyan
            }
            if ($response.executionTimeMs -ne $null) {
                Write-Host "Execution Time: $($response.executionTimeMs)ms" -ForegroundColor Cyan
            }
        } else {
            Write-Host "Status: $($response.status)" -ForegroundColor Red
            if ($response.error) {
                Write-Host "Error: $($response.error)" -ForegroundColor Red
            }
        }
    }
    catch {
        Write-Host "✗ FAILED" -ForegroundColor Red
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
        
        if ($_.Exception.Response) {
            $statusCode = $_.Exception.Response.StatusCode.value__
            Write-Host "Status Code: $statusCode" -ForegroundColor Red
        }
    }
}

# Check if application is running
Write-Host "`nChecking if application is running..." -ForegroundColor Yellow
try {
    $healthCheck = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -Method GET -ErrorAction SilentlyContinue
    Write-Host "✓ Application is running" -ForegroundColor Green
}
catch {
    try {
        $homeCheck = Invoke-WebRequest -Uri "http://localhost:8080" -Method GET -ErrorAction Stop
        Write-Host "✓ Application is running (home page accessible)" -ForegroundColor Green
    }
    catch {
        Write-Host "✗ Application does not appear to be running on localhost:8080" -ForegroundColor Red
        Write-Host "Please start the Spring Boot application first using: mvn spring-boot:run" -ForegroundColor Yellow
        exit 1
    }
}

# Run tests
Write-Host "`nRunning Database Connection Tests..." -ForegroundColor Yellow

Test-Endpoint "$baseUrl/mysql-connection" "1. MySQL Connection Test"
Test-Endpoint "$baseUrl/sqlserver-connection" "2. SQL Server Connection Test"
Test-Endpoint "$baseUrl/sqlserver-query" "3. SQL Server Query Test"
Test-Endpoint "$baseUrl/stored-procedure-check" "4. Stored Procedure Check"
Test-Endpoint "$baseUrl/direct-stored-procedure" "5. Direct Stored Procedure Execution"
Test-Endpoint "$baseUrl/glref-codes-api" "6. GL Reference Codes API Test"
Test-Endpoint "$baseUrl/glref-codes-comprehensive" "7. Comprehensive GL Codes Test"
Test-Endpoint "$baseUrl/connection-info" "8. Connection Information"

Write-Host "`n=======================================================" -ForegroundColor Green
Write-Host "Database Connection Tests Completed!" -ForegroundColor Green
Write-Host "`nTo view detailed test results in a web interface:" -ForegroundColor Yellow
Write-Host "Open: http://localhost:8080/database-test" -ForegroundColor Cyan
Write-Host "`nNote: You may need to log in with admin credentials to access the test endpoints." -ForegroundColor Gray
