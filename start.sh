#!/bin/bash

cd ./ExpenseAPI
chmod +x ./mvnw
./mvnw clean spring-boot:run >> /dev/null &
BACKEND_PID=$!


echo -n "Waiting for backend server to start..."
until $(curl --output /dev/null --silent --head --fail http://localhost:8080/expense/recent); do
    printf '.'
    sleep 1
done

echo -e "\nBackend server is up. Starting frontend..."

cd ../frontend
chmod +x ./gradlew
./gradlew run


kill $BACKEND_PID
