@echo off
echo Running SQL script to create transfers_main table...
mysql -u sa -pmyRoot@123 globalschema < create_transfers_table.sql
if %ERRORLEVEL% EQU 0 (
  echo Table created successfully!
) else (
  echo Error creating table. Check MySQL connection and credentials.
)
pause
