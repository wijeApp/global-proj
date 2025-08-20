Write-Host "Running SQL script to create transfers_main table..." -ForegroundColor Green

try {
    # Assuming MySQL is in the PATH, otherwise specify the full path to mysql.exe
    $mysqlOutput = mysql -u sa -pmyRoot@123 globalschema < create_transfers_table.sql 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Table created successfully!" -ForegroundColor Green
    } else {
        Write-Host "Error creating table. MySQL output:" -ForegroundColor Red
        Write-Host $mysqlOutput -ForegroundColor Red
    }
} catch {
    Write-Host "Error executing MySQL command: $_" -ForegroundColor Red
    Write-Host "Make sure MySQL is installed and in your PATH" -ForegroundColor Yellow
}

Write-Host "Press any key to continue..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
