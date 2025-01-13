@echo off

cd ExpenseAPI
start "" /B mvnw.cmd clean spring-boot:run
set BACKEND_PID=%!

cd ..

echo Waiting for backend server to start...
:WAIT_FOR_BACKEND
curl --head --silent --fail http://localhost:8080/expense/recent >nul 2>nul
if %errorlevel% neq 0 (
    timeout /t 1 >nul
    echo .
    goto WAIT_FOR_BACKEND
)

echo Backend server is up. Starting frontend...

cd frontend
gradlew.bat run

taskkill /PID %BACKEND_PID% /F